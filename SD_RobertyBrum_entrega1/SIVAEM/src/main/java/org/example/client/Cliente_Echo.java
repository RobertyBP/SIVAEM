package org.example.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.candidatos.Candidato;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.UUID;

public class Cliente_Echo {

    static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws IOException {

        System.out.println("informe o ip ");
        String serverHostname = stdIn.readLine();
        System.out.println("informe a porta ");
        int porta = Integer.parseInt(stdIn.readLine());

        if (args.length > 0)
            serverHostname = args[0];
        System.out.println ("Attemping to connect to host " +
                serverHostname + " on port " + porta );

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

        String userInput;
        String token = null;
        System.out.println(" 1 login 2 cadastrar");
        while ((userInput = stdIn.readLine()) != null) {
            switch (userInput){
                case "1":
                    System.out.println("Login");
                    System.out.println("email:");
                    String email = stdIn.readLine();
                    System.out.println("senha");
                    String senha = stdIn.readLine();
                    Candidato log = new Candidato("loginCandidato",email, senha);
                    final var objectMapper = new ObjectMapper();
                    final var json = objectMapper.writeValueAsString(log);
                    out.println(json);
                    String inputLine;
                    if((inputLine = in.readLine()) != null) {
                        StringBuilder jsonBuilder = new StringBuilder();
                        jsonBuilder.append(inputLine);
                        String receivedJson = jsonBuilder.toString();

                        ObjectMapper objectMapperR = new ObjectMapper();
                        JsonNode jsonNode = objectMapperR.readTree(receivedJson);
                        token = jsonNode.get("token").asText();
                    }
                    if(token != null){
                        menu_candidato(out,in, token,echoSocket);
                    }

                    break;
                case "2":
                    System.out.println("Cadastrar CANDIDATO");
                    System.out.println("nome:");
                    String nome = stdIn.readLine();
                    System.out.println("email:");
                    String emailC = stdIn.readLine();
                    System.out.println("senha");
                    String  senhaC = stdIn.readLine();
                    Candidato cad = new Candidato("cadastrarCandidato",nome, emailC, senhaC);
                    final var objectMapperC = new ObjectMapper();
                    final String jsonC = objectMapperC.writeValueAsString(cad);
                    out.println(jsonC);
                    String inputLineC;
                    if((inputLineC = in.readLine()) != null) {
                        StringBuilder jsonBuilder = new StringBuilder();
                        jsonBuilder.append(inputLineC);
                        String receivedJson = jsonBuilder.toString();

                        ObjectMapper objectMapperR = new ObjectMapper();
                        JsonNode jsonNode = objectMapperR.readTree(receivedJson);
                        token = jsonNode.get("token").asText();
                    }
                    if(token != null){
                        menu_candidato(out,in, token,echoSocket);
                    }
                    break;
                default:
                    System.out.println("erro nessa bagaçã");
                    break;
            }
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }
    public static void menu_candidato(PrintWriter out, BufferedReader in, String token, Socket echoSocket) throws IOException {
        boolean flag = true;
        while (flag) {
            System.out.println("1-visualizar, 2-editar, 3-apagar 5-logout");
            int op = Integer.parseInt(stdIn.readLine());

                switch (op) {
                    case 1:
                        System.out.println("visualizar");
                        System.out.println("email:");
                        String emailC = stdIn.readLine();

                        Candidato cad = new Candidato("visualizarCandidato", emailC);
                        final var objectMapperC = new ObjectMapper();
                        final String jsonC = objectMapperC.writeValueAsString(cad);
                        out.println(jsonC);
                        System.out.println("client: " + in.readLine());
                        break;
                    case 2:
                        System.out.println("atualizar");
                        System.out.println("email:");
                        String emailE = stdIn.readLine();
                        System.out.println("nome:");
                        String nomeE = stdIn.readLine();
                        System.out.println("senha");
                        String senhaE = stdIn.readLine();
                        Candidato cad1 = new Candidato("atualizarCandidato", emailE, nomeE, senhaE);
                        final var objectMapperE = new ObjectMapper();
                        final String jsonE = objectMapperE.writeValueAsString(cad1);
                        out.println(jsonE);
                        System.out.println("client: " + in.readLine());
                        break;
                    case 3:
                        System.out.println("apagar");
                        System.out.println("email:");
                        String emailA = stdIn.readLine();

                        Candidato cad2 = new Candidato("apagarCandidato", emailA);
                        final var objectMapperA = new ObjectMapper();
                        final String jsonA = objectMapperA.writeValueAsString(cad2);
                        out.println(jsonA);
                        System.out.println("client: " + in.readLine());
                        break;
                    case 5:
                        flag = false;
                        System.out.println("logout");
                        Candidato cad3 = new Candidato("logoutCandidato", UUID.fromString(token));
                        final var objectMapperL = new ObjectMapper();
                        final String jsonL = objectMapperL.writeValueAsString(cad3);
                        out.println(jsonL);
                        System.out.println("client: " + in.readLine());
                        echoSocket.close();

                        break;
                    default:
                        System.out.println("error");
                        break;
                }
        }

    }
    public static String receberDados(BufferedReader in) throws IOException {
        String inputLine;
        if((inputLine = in.readLine()) != null){
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append(inputLine);
            String receivedJson = jsonBuilder.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(receivedJson);
            String token = jsonNode.get("token").asText();
            System.out.println("client: " + in.readLine());
            return  token ;
        }
        return  null;
    }
}
