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

public class Empresa {
    public static void tratamentoEmpresa(String action, JsonNode jsonNode,
                                         PrintWriter out, String inputLine, Socket clientSocket) {
        String token;
        try {
            switch (action) {
                case "loginEmpresa":
                    System.out.println(action);
                    System.out.println("Serverlogin: " + inputLine);
                    String emailL = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String senhaL = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;

                    if (Validacoes.validate(emailL, senhaL, action)) {

                        token = Token.capturaToken(emailL, senhaL, action);

                        MensagemTOKEN men = new MensagemTOKEN(action, 200, token);
                        final var objectMapperL = new ObjectMapper();
                        final String jsonL = objectMapperL.writeValueAsString(men);
                        System.out.println("login ok");
                        System.out.println(jsonL);
                        out.println(jsonL);
                    } else {
                        System.out.println("login falhou");
                        MensagemPADRAO men = new MensagemPADRAO(action, 404, "login ou senha incorretos");
                        final var objectMapperL = new ObjectMapper();
                        final String jsonL = objectMapperL.writeValueAsString(men);
                        out.println(jsonL);
                    }
                    break;
                case "cadastrarEmpresa":
                    System.out.println(action);
                    System.out.println("ServerCad: " + inputLine);

                    String razaoSocialC = jsonNode.get("razaoSocial") != null ? jsonNode.get("razaoSocial").asText() : null;
                    String emailC = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String cnpjC = jsonNode.get("cnpj") != null ? jsonNode.get("cnpj").asText() : null;
                    String senhaC = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;
                    String desC = jsonNode.get("descricao") != null ? jsonNode.get("descricao").asText() : null;
                    String ramoC = jsonNode.get("ramo") != null ? jsonNode.get("ramo").asText() : null;

                    if (!Validacoes.compararEmail(emailC, action)) {
                        if (!Validacoes.iaValidCNPJ(cnpjC)) {
                            token = String.valueOf(Token.criarToken());

                            try (Connection con = ConectarBanco.obterConexao()) {
                                String sql = "INSERT INTO empresa (razao_social, ramo, descricao, email, token, senha ,cnpj) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement pstmt = con.prepareStatement(sql);

                                pstmt.setString(1, razaoSocialC);
                                pstmt.setString(2, ramoC);
                                pstmt.setString(3, desC);
                                pstmt.setString(4, emailC);
                                pstmt.setString(5, token);
                                pstmt.setString(6, senhaC);
                                pstmt.setString(7, cnpjC);

                                pstmt.executeUpdate();
                                System.out.println("Dados inseridos com sucesso!");

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            MensagemTOKEN men1 = new MensagemTOKEN(action, 201, token);
                            final var objectMapperC = new ObjectMapper();
                            final String jsonC = objectMapperC.writeValueAsString(men1);
                            System.out.println(jsonC);
                            out.println(jsonC);
                        } else {
                            System.out.println("cnpj " + cnpjC + "já em uso");
                            MensagemPADRAO men2 = new MensagemPADRAO(action, 422, "cnpj já cadastrado");
                            final var objectMapperA = new ObjectMapper();
                            final String jsonA = objectMapperA.writeValueAsString(men2);
                            System.out.println(jsonA);
                            out.println(jsonA);
                        }
                    } else {
                        System.out.println("formato de email " + emailC + "invalido");
                        MensagemPADRAO men2 = new MensagemPADRAO(action, 422, "formato de e-mail invalido ou email já cadastrado");
                        final var objectMapperA = new ObjectMapper();
                        final String jsonA = objectMapperA.writeValueAsString(men2);
                        System.out.println(jsonA);
                        out.println(jsonA);
                    }
                    break;
                case "atualizarEmpresa":
                    System.out.println(action);
                    System.out.println("ServerAT: " + inputLine);

                    String razaoSocialA = jsonNode.get("razaoSocial") != null ? jsonNode.get("razaoSocial").asText() : null;
                    String emailA = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String cnpjA = jsonNode.get("cnpj") != null ? jsonNode.get("cnpj").asText() : null;
                    String senhaA = jsonNode.get("senha") != null ? jsonNode.get("senha").asText() : null;
                    String desA = jsonNode.get("descricao") != null ? jsonNode.get("descricao").asText() : null;
                    String ramoA = jsonNode.get("ramo") != null ? jsonNode.get("ramo").asText() : null;
                    String tokenA = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action,emailA,tokenA)) {
                        try (Connection con = ConectarBanco.obterConexao()) {
                            String sql = "UPDATE empresa SET razao_social = ?, ramo = ?, descricao = ?, senha = ?, cnpj = ? WHERE email = ?";
                            PreparedStatement pstmt = con.prepareStatement(sql);

                            pstmt.setString(1, razaoSocialA);
                            pstmt.setString(2, ramoA);
                            pstmt.setString(3, desA);
                            pstmt.setString(4, senhaA);
                            pstmt.setString(5, cnpjA);
                            pstmt.setString(6, emailA);


                            int linhasAfetadas = pstmt.executeUpdate();
                            if (linhasAfetadas > 0) {
                                System.out.println("Dados do candidato com email " + emailA + " atualizados com sucesso!");
                                MensagemLogout men3 = new MensagemLogout(action, 201);
                                final var objectMapperA = new ObjectMapper();
                                final String jsonA = objectMapperA.writeValueAsString(men3);
                                System.out.println(jsonA);
                                out.println(jsonA);
                            } else {
                                System.out.println("Nenhum candidato encontrado com o email " + emailA);
                                MensagemPADRAO men2 = new MensagemPADRAO(action, 404, "e-mail não encontrado");
                                final var objectMapperA = new ObjectMapper();
                                final String jsonA = objectMapperA.writeValueAsString(men2);
                                System.out.println(jsonA);
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
                case "visualizarEmpresa":
                    System.out.println(action);
                    System.out.println("ServerVis: " + inputLine);

                    String emailVis = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String tokenV = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;
                    ResultSet rs;
                    if (Token.validarToken(action,emailVis,tokenV)) {
                        try (Connection con = ConectarBanco.obterConexao()) {
                            String sql = "SELECT razao_social, ramo, descricao, senha ,cnpj FROM empresa WHERE email = ?";
                            PreparedStatement pstmt = con.prepareStatement(sql);
                            pstmt.setString(1, emailVis);
                            rs = pstmt.executeQuery();
                            String rS, cnpj, senha, desc, ramo;
                            if (rs.next()) {
                                rS = rs.getString("razao_social");
                                cnpj = rs.getString("cnpj");
                                senha = rs.getString("senha");
                                desc = rs.getString("descricao");
                                ramo = rs.getString("ramo");

                                System.out.println("Dados da Empresa com email " + emailVis + " encontrados!");
                                MensagemVisEmpresa men3 = new MensagemVisEmpresa(action, 201, rS, cnpj, senha, desc, ramo);
                                final var objectMapperA = new ObjectMapper();
                                final String jsonA = objectMapperA.writeValueAsString(men3);
                                System.out.println(jsonA);
                                out.println(jsonA);
                            } else {
                                System.out.println("Nenhuma Empresa encontrado com o email " + emailVis);
                                MensagemPADRAO men2 = new MensagemPADRAO(action, 404, "e-mail não encontrado");
                                final var objectMapperV = new ObjectMapper();
                                final String jsonV = objectMapperV.writeValueAsString(men2);
                                System.out.println(jsonV);
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
                case "apagarEmpresa":
                    System.out.println(action);
                    System.out.println("ServerApg: " + inputLine);

                    String emailAp = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String tokenAp = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action,emailAp,tokenAp)) {
                        try (Connection con = ConectarBanco.obterConexao()) {
                            String sql = "DELETE FROM empresa WHERE email = ?";
                            PreparedStatement pstmt = con.prepareStatement(sql);

                            pstmt.setString(1, emailAp);

                            int linhasAfetadas = pstmt.executeUpdate();
                            if (linhasAfetadas > 0) {
                                System.out.println("Empresa com email " + emailAp+ " excluído com sucesso!");
                                MensagemLogout men3 = new MensagemLogout(action, 201);
                                final var objectMapperA = new ObjectMapper();
                                final String jsonA = objectMapperA.writeValueAsString(men3);
                                System.out.println(jsonA);
                                out.println(jsonA);
                            } else {
                                System.out.println("Nenhuma Empresa encontrado com o email " + emailAp);
                                MensagemPADRAO men2 = new MensagemPADRAO(action, 404, "e-mail não encontrado");
                                final var objectMapperV = new ObjectMapper();
                                final String jsonV = objectMapperV.writeValueAsString(men2);
                                System.out.println(jsonV);
                                out.println(jsonV);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException("Erro ao excluir Empresa: " + e.getMessage());
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
