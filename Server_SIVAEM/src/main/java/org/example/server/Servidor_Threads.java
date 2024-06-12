package org.example.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.mensagens.MensagemLogout;
import org.example.mensagens.MensagemPADRAO;
import org.example.token.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;


public class Servidor_Threads extends Thread {
    protected Socket clientSocket;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        int porta = 22222;
        System.out.println("IP " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("PORTA "+ porta);

        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    new Servidor_Threads(serverSocket.accept());
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: 10008.");
                System.exit(1);
            }
        }
    }

    private Servidor_Threads(Socket clientSoc) {
        clientSocket = clientSoc;
        start();
    }

    public void run() {
        System.out.println("New Communication Thread Started");

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append(inputLine);

                if (inputLine.endsWith("}")) {
                    String receivedJson = jsonBuilder.toString();

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(receivedJson);
                    String action = jsonNode.get("operacao").asText();

                    if(action.contains("Candidato")){
                        Candidato.tratamentoCandidato(action, jsonNode, out, inputLine, clientSocket);
                    }if(action.contains("Empresa")){
                        Empresa.tratamentoEmpresa(action, jsonNode, out, inputLine, clientSocket);
                    }if(action.contains("Vaga")){
                        Vaga.tratamentoVaga(action, jsonNode, out, inputLine, clientSocket);
                    }if(action.contains("CompetenciaExperiencia")){
                        CompExp.tratamentoCompExp(action, jsonNode, out, inputLine, clientSocket);
                    }if (action.equals("logout")){
                        String emailD ="default";
                        System.out.println(action+ inputLine);
                        String tokenL = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;
                        if (Token.validarToken(action,emailD,tokenL)) {
                            MensagemLogout men = new MensagemLogout("logout", 204);
                            final var objectMapperL = new ObjectMapper();
                            final String jsonL = objectMapperL.writeValueAsString(men);
                            out.println(jsonL);
                            System.out.println("logout realizado");
                            out.close();
                            in.close();
                            clientSocket.close();
                        }
                    }
                }
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            System.exit(1);
        }
    }
}