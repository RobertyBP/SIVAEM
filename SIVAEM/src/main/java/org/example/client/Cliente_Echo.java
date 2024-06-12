package org.example.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.candidatos.*;

import org.example.menus.MenuGeral;


import java.io.*;
import java.net.*;


public class Cliente_Echo {

    static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {

        System.out.println("informe o ip ");
        String serverHostname = stdIn.readLine();
        System.out.println("informe a porta ");
        int porta = Integer.parseInt(stdIn.readLine());

        if (args.length > 0)
            serverHostname = args[0];
        System.out.println("Attemping to connect to host " +
                serverHostname + " on port " + porta);

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, porta);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHostname);
            System.exit(1);
        }
        candidato(out, in, echoSocket);

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }

    public static void candidato(PrintWriter out, BufferedReader in, Socket echoSocket) throws IOException {
        String userInput;
        String token = null;
        System.out.println(" 1-login 2-cadastrar 5-sair");
        while ((userInput = stdIn.readLine()) != null) {
            switch (userInput) {
                case "1":
                    System.out.println("Login");
                    System.out.println("email:");
                    String email = stdIn.readLine();
                    System.out.println("senha");
                    String senha = stdIn.readLine();
                    Login log = new Login("loginCandidato", email, senha);
                    final var objectMapper = new ObjectMapper();
                    final var json = objectMapper.writeValueAsString(log);
                    System.out.println(json);
                    out.println(json);
                    String inputLine;
                    if ((inputLine = in.readLine()) != null) {
                        StringBuilder jsonBuilder = new StringBuilder();
                        jsonBuilder.append(inputLine);
                        String receivedJson = jsonBuilder.toString();

                        ObjectMapper objectMapperR = new ObjectMapper();
                        JsonNode jsonNode = objectMapperR.readTree(receivedJson);
                        token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;
                    }
                    if (token != null) {
                        MenuGeral.menuGeral(stdIn, out, in, token, echoSocket);
                        candidato(out, in, echoSocket);
                    } else {
                        System.out.println("client: " + inputLine);
                        System.out.println("foi detectado um retorno nulo");
                        System.out.println("revise seus dados");
                        candidato(out, in, echoSocket);
                    }

                    break;
                case "2":
                    System.out.println("Cadastrar CANDIDATO");
                    System.out.println("nome:");
                    String nome = stdIn.readLine();
                    System.out.println("email:");
                    String emailC = stdIn.readLine();
                    System.out.println("senha");
                    String senhaC = stdIn.readLine();
                    Candidato cad = new Candidato("cadastrarCandidato", nome, emailC, senhaC);
                    final var objectMapperC = new ObjectMapper();
                    final String jsonC = objectMapperC.writeValueAsString(cad);
                    System.out.println(jsonC);
                    out.println(jsonC);

                    String inputLineC;
                    if ((inputLineC = in.readLine()) != null) {
                        StringBuilder jsonBuilder = new StringBuilder();
                        jsonBuilder.append(inputLineC);
                        String receivedJson = jsonBuilder.toString();

                        ObjectMapper objectMapperR = new ObjectMapper();
                        JsonNode jsonNode = objectMapperR.readTree(receivedJson);
                        token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;
                    }
                    if (token != null) {
                        MenuGeral.menuGeral(stdIn, out, in, token, echoSocket);
                        candidato(out, in, echoSocket);
                    } else {
                        System.out.println("client: " + inputLineC);
                        System.out.println("foi detectado um retorno nulo");
                        System.out.println("revise seus dados");
                        candidato(out, in, echoSocket);
                    }
                    break;
                case "5":
                    System.out.println("fechando cliente....");
                    out.close();
                    in.close();
                    stdIn.close();
                    echoSocket.close();
                    break;
                default:
                    System.out.println("error ");
                    break;
            }
        }
    }
}
