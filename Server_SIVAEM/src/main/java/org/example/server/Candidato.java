package org.example.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.data.ConectarBanco;
import org.example.functions.Validacoes;
import org.example.mensagens.*;
import org.example.token.Token;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Candidato {
    public static void tratamentoCandidato(String action, JsonNode jsonNode,
                                           PrintWriter out, String inputLine, Socket clientSocket) {
        String token;
        try {
            switch (action) {
                case "loginCandidato":
                    System.out.println("Serverlogin: " + inputLine);
                    String emailL = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String senhaL = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;

                    if (Validacoes.validate(emailL, senhaL, action)) {

                        token = Token.capturaToken(emailL, senhaL, action);

                        MensagemTOKEN men = new MensagemTOKEN(action, 200, token);
                        final var objectMapperL = new ObjectMapper();
                        final String jsonL = objectMapperL.writeValueAsString(men);

                        out.println(jsonL);
                    } else {
                        System.out.println("login falhou");
                        MensagemPADRAO men = new MensagemPADRAO(action, 200, "login ou senha incorretos");
                        final var objectMapperL = new ObjectMapper();
                        final String jsonL = objectMapperL.writeValueAsString(men);
                        out.println(jsonL);
                    }
                    break;
                case "cadastrarCandidato":
                    System.out.println("ServerCad: " + inputLine);

                    String nomeC = jsonNode.get("nome") != null ? jsonNode.get("nome").asText() : null;
                    String emailC = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String senhaC = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;
                    if (!Validacoes.compararEmail(emailC, action)) {
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
                        MensagemTOKEN men1 = new MensagemTOKEN(action, 200, token);
                        final var objectMapperC = new ObjectMapper();
                        final String jsonC = objectMapperC.writeValueAsString(men1);
                        out.println(jsonC);
                    } else {
                        System.out.println("formato de email " + emailC + "invalido");
                        MensagemPADRAO men2 = new MensagemPADRAO(action, 404, "formato de e-mail invalido ");
                        final var objectMapperA = new ObjectMapper();
                        final String jsonA = objectMapperA.writeValueAsString(men2);
                        System.out.println(jsonA);
                        out.println(jsonA);
                    }
                    break;
                case "atualizarCandidato":
                    System.out.println("ServerAT: " + inputLine);

                    String novoNome = jsonNode.get("nome") != null ? jsonNode.get("nome").asText() : null;
                    String email = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String novaSenha = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;
                    String tokenA = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action, email, tokenA)) {

                        try (Connection con = ConectarBanco.obterConexao()) {
                            String sql = "UPDATE candidato SET nome = ?, senha = ? WHERE email = ?";
                            PreparedStatement pstmt = con.prepareStatement(sql);

                            pstmt.setString(1, novoNome);
                            pstmt.setString(2, novaSenha);
                            pstmt.setString(3, email);

                            int linhasAfetadas = pstmt.executeUpdate();
                            if (linhasAfetadas > 0) {
                                System.out.println("Dados do candidato com email " + email + " atualizados com sucesso!");
                                MensagemLogout men3 = new MensagemLogout(action, 200);
                                final var objectMapperA = new ObjectMapper();
                                final String jsonA = objectMapperA.writeValueAsString(men3);
                                   out.println(jsonA);
                            } else {
                                System.out.println("Nenhum candidato encontrado com o email " + email);
                                MensagemPADRAO men2 = new MensagemPADRAO(action, 404, "e-mail não encontrado");
                                final var objectMapperA = new ObjectMapper();
                                final String jsonA = objectMapperA.writeValueAsString(men2);
                                out.println(jsonA);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Erro ao atualizar candidato: " + e.getMessage());
                        }
                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 403, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "visualizarCandidato":
                    System.out.println("ServerVis: " + inputLine);

                    String emailVisualizar = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String tokenV = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;
                    ResultSet rs;
                    if (Token.validarToken(action, emailVisualizar, tokenV)) {


                        try (Connection con = ConectarBanco.obterConexao()) {
                            String sql = "SELECT nome, senha FROM candidato WHERE email = ?";
                            PreparedStatement pstmt = con.prepareStatement(sql);
                            pstmt.setString(1, emailVisualizar);
                            rs = pstmt.executeQuery();
                            String nome, senha, rJson;
                            if (rs.next()) {
                                nome = rs.getString("nome");
                                senha = rs.getString("senha");

                                System.out.println("Dados do candidato com email " + emailVisualizar + " encontrados!");
                                MensagemVisCandidadto men3 = new MensagemVisCandidadto(action, 200, nome, senha);
                                final var objectMapperA = new ObjectMapper();
                                final String jsonA = objectMapperA.writeValueAsString(men3);
                                out.println(jsonA);
                            } else {
                                System.out.println("Nenhum candidato encontrado com o email " + emailVisualizar);
                                MensagemPADRAO men2 = new MensagemPADRAO(action, 404, "e-mail não encontrado");
                                final var objectMapperV = new ObjectMapper();
                                final String jsonV = objectMapperV.writeValueAsString(men2);
                                out.println(jsonV);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Erro ao visualizar candidato: " + e.getMessage());
                        }
                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 403, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "apagarCandidato":
                    System.out.println("ServerCad: " + inputLine);

                    String emailApagar = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String tokenAp = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action, emailApagar, tokenAp)) {

                        try (Connection con = ConectarBanco.obterConexao()) {
                            String sql = "DELETE FROM candidato WHERE email = ?";
                            PreparedStatement pstmt = con.prepareStatement(sql);

                            pstmt.setString(1, emailApagar);

                            int linhasAfetadas = pstmt.executeUpdate();
                            if (linhasAfetadas > 0) {
                                System.out.println("Candidato com email " + emailApagar + " excluído com sucesso!");
                                MensagemLogout men3 = new MensagemLogout(action, 200);
                                final var objectMapperA = new ObjectMapper();
                                final String jsonA = objectMapperA.writeValueAsString(men3);
                                out.println(jsonA);
                            } else {
                                System.out.println("Nenhum candidato encontrado com o email " + emailApagar);
                                MensagemPADRAO men2 = new MensagemPADRAO(action, 404, "e-mail não encontrado");
                                final var objectMapperV = new ObjectMapper();
                                final String jsonV = objectMapperV.writeValueAsString(men2);
                                out.println(jsonV);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Erro ao excluir candidato: " + e.getMessage());
                        }
                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 403, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                default:
                    System.out.println("Server: " + inputLine);
                    out.println(inputLine + "defaultok");

                    break;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
