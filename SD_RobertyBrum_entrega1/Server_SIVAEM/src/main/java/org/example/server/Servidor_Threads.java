package org.example.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.data.ConectarBanco;
import org.example.functions.Validacoes;
import org.example.mensagens.MensagemPADRAO;
import org.example.mensagens.MensagemTOKEN;
import org.example.mensagens.MensagemVizualizacao;
import org.example.token.Token;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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

                    String token;
                    switch (action) {
                        case "loginCandidato":
                            System.out.println("Serverlogin: " + inputLine);
                            String emailL = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                            String senhaL = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;

                            if (Validacoes.validate(emailL, senhaL)) {

                                token = Token.capturaToken(emailL,senhaL);

                                MensagemTOKEN men = new MensagemTOKEN("loginCandidato",200,token);
                                final var objectMapperL = new ObjectMapper();
                                final String jsonL = objectMapperL.writeValueAsString(men);

                                out.println(jsonL);
                            }else {
                                System.out.println("login falhou");
                                MensagemPADRAO men = new MensagemPADRAO("loginCandidato", 200, "login ou senha incorretos");
                                final var objectMapperL = new ObjectMapper();
                                final String jsonL = objectMapperL.writeValueAsString(men);
                                out.println(jsonL);
                            }
                            break;
                        case "logout":
                            System.out.println("Serverlogout: " + inputLine);
                            String tokenL = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;
                            MensagemPADRAO men = new MensagemPADRAO("logout", 204);
                            final var objectMapperL = new ObjectMapper();
                            final String jsonL = objectMapperL.writeValueAsString(men);
                            out.println(jsonL);
                            System.out.println("logout realizado");
                            clientSocket.close();
                            break;
                        case "cadastrarCandidato":
                            System.out.println("ServerCad: " + inputLine);

                            String nomeC = jsonNode.get("nome") != null ? jsonNode.get("nome").asText() : null;
                            String emailC = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                            String senhaC = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;
                            token = String.valueOf(Token.criarToken());
                            try (Connection con = ConectarBanco.obterConexao()) {
                                String sql = "INSERT INTO candidato (nome, email, senha, token) VALUES (?, ?, ?, ?)";
                                PreparedStatement pstmt = con.prepareStatement(sql);

                                pstmt.setString(1, nomeC);
                                pstmt.setString(2, emailC);
                                pstmt.setString(3, senhaC);
                                pstmt.setString(4, token);

                                pstmt.executeUpdate();
                                System.out.println("Dados inseridos com sucesso!");

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            MensagemTOKEN men1 = new MensagemTOKEN("cadastrarCandidato",200,token);
                            final var objectMapperC = new ObjectMapper();
                            final String jsonC = objectMapperC.writeValueAsString(men1);
                            out.println(jsonC);
                            break;
                        case "atualizarCandidato":
                            System.out.println("ServerAT: " + inputLine);

                            String novoNome = jsonNode.get("nome") != null ? jsonNode.get("nome").asText() : null;
                            String email = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                            String novaSenha = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;

                            try (Connection con = ConectarBanco.obterConexao()) {
                                String sql = "UPDATE candidato SET nome = ?, senha = ? WHERE email = ?";
                                PreparedStatement pstmt = con.prepareStatement(sql);

                                pstmt.setString(1, novoNome);
                                pstmt.setString(2, novaSenha);
                                pstmt.setString(3, email);

                                int linhasAfetadas = pstmt.executeUpdate();
                                if (linhasAfetadas > 0) {
                                    System.out.println("Dados do candidato com email " + email + " atualizados com sucesso!");
                                    MensagemPADRAO men3 = new MensagemPADRAO("atualizarCandidato",200);
                                    final var objectMapperA = new ObjectMapper();
                                    final String jsonA = objectMapperA.writeValueAsString(men3);
                                    out.println(jsonA);
                                } else {
                                    System.out.println("Nenhum candidato encontrado com o email " + email);
                                    MensagemPADRAO men2 = new MensagemPADRAO("atualizarCandidato",404,"e-mail não encontrado");
                                    final var objectMapperA = new ObjectMapper();
                                    final String jsonA = objectMapperA.writeValueAsString(men2);
                                    out.println(jsonA);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException("Erro ao atualizar candidato: " + e.getMessage());
                            }

                            break;
                        case "visualizarCandidato":
                            System.out.println("ServerVis: " + inputLine);

                            String emailVisualizar = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;

                            ResultSet rs;
                            try (Connection con = ConectarBanco.obterConexao()) {
                                String sql = "SELECT nome, senha FROM candidato WHERE email = ?";
                                PreparedStatement pstmt = con.prepareStatement(sql);
                                pstmt.setString(1, emailVisualizar);
                                rs = pstmt.executeQuery();
                                String nome, senha, rJson;
                                if (rs.next()){
                                    nome=rs.getString("nome");
                                    senha=rs.getString("senha");

                                    System.out.println("Dados do candidato com email " + emailVisualizar + " encontrados!");
                                    MensagemVizualizacao men3 = new MensagemVizualizacao("visualizarCandidato",200,nome,senha);
                                    final var objectMapperA = new ObjectMapper();
                                    final String jsonA = objectMapperA.writeValueAsString(men3);
                                    out.println(jsonA);
                                } else {
                                    System.out.println("Nenhum candidato encontrado com o email " + emailVisualizar);
                                    MensagemPADRAO men2 = new MensagemPADRAO("visualizarCandidato",404,"e-mail não encontrado");
                                    final var objectMapperV = new ObjectMapper();
                                    final String jsonV = objectMapperV.writeValueAsString(men2);
                                    out.println(jsonV);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException("Erro ao visualizar candidato: " + e.getMessage());
                            }
                            break;
                        case "apagarCandidato":
                            System.out.println("ServerCad: " + inputLine);

                            String emailApagar = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;

                            try (Connection con = ConectarBanco.obterConexao()) {
                                String sql = "DELETE FROM candidato WHERE email = ?";
                                PreparedStatement pstmt = con.prepareStatement(sql);

                                pstmt.setString(1, emailApagar);

                                int linhasAfetadas = pstmt.executeUpdate();
                                if (linhasAfetadas > 0) {
                                    System.out.println("Candidato com email " + emailApagar + " excluído com sucesso!");
                                    MensagemPADRAO men3 = new MensagemPADRAO("apagarCandidato",200);
                                    final var objectMapperA = new ObjectMapper();
                                    final String jsonA = objectMapperA.writeValueAsString(men3);
                                    out.println(jsonA);
                                } else {
                                    System.out.println("Nenhum candidato encontrado com o email " + emailApagar);
                                    MensagemPADRAO men2 = new MensagemPADRAO("apagarCandidato",404,"e-mail não encontrado");
                                    final var objectMapperV = new ObjectMapper();
                                    final String jsonV = objectMapperV.writeValueAsString(men2);
                                    out.println(jsonV);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException("Erro ao excluir candidato: " + e.getMessage());
                            }
                            break;
                        default:
                            System.out.println("Server: " + inputLine);
                            out.println(inputLine + "defaultok");

                            break;
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