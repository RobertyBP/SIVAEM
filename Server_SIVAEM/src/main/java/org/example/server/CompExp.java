package org.example.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.data.ConectarBanco;

import org.example.functions.CompetenciaExperiencia;
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
import java.util.ArrayList;
import java.util.List;

public class CompExp {
    public static void tratamentoCompExp(String action, JsonNode jsonNode, PrintWriter out, String inputLine, Socket clientSocket) {
        String token;
        ResultSet rs;
        int idCompetencia, idCandidato;
        try {
            switch (action) {

                case "cadastrarCompetenciaExperiencia":
                    System.out.println("Server: " + inputLine);

                    String email = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;
                    JsonNode competenciasNode = jsonNode.get("competenciaExperiencia");

                    if (Token.validarToken(action, email, token)) {
                        idCandidato = Validacoes.capturarId(action, email);
                        if (idCandidato != -1) {
                            try (Connection con = ConectarBanco.obterConexao()) {
                                for (JsonNode competenciaNode : competenciasNode) {
                                    String competencia = competenciaNode.get("competencia").asText();
                                    int experiencia = competenciaNode.get("experiencia").asInt();

                                    idCompetencia = Validacoes.capturarCompetenciaId(competencia);

                                    String sql = "INSERT INTO candidato_competencia (tempo, id_candidato, id_competencia) VALUES (?, ?, ?)";
                                    PreparedStatement pstmt = con.prepareStatement(sql);
                                    pstmt.setInt(1, experiencia);
                                    pstmt.setInt(2, idCandidato);
                                    pstmt.setInt(3, idCompetencia);
                                    pstmt.executeUpdate();
                                }

                                MensagemPADRAO men = new MensagemPADRAO(action, 201, "Competencia/Experiencia cadastrada com sucesso");
                                final var objectMapper = new ObjectMapper();
                                final String json = objectMapper.writeValueAsString(men);
                                out.println(json);

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            MensagemPADRAO msg = new MensagemPADRAO(action, 404, "Candidato não encontrada");
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);
                        }
                    } else {
                        MensagemPADRAO men = new MensagemPADRAO(action, 401, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(men);
                        out.println(json);
                    }
                    break;
                case "visualizarCompetenciaExperiencia":
                    System.out.println(action + inputLine);

                    String emailL = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action, emailL, token)) {
                        idCandidato = Validacoes.capturarId(action, emailL);
                        if (idCandidato != -1) {
                            try (Connection con = ConectarBanco.obterConexao()) {
                                // Recuperar lista de competências e experiências do candidato
                                String getxpSQL = "SELECT cc.id_competencia, cc.tempo, c.competencia " +
                                        "FROM candidato_competencia cc " +
                                        "JOIN competencia c ON cc.id_competencia = c.id_competencia " +
                                        "WHERE cc.id_candidato = ?";
                                PreparedStatement getxpStmt = con.prepareStatement(getxpSQL);
                                getxpStmt.setInt(1, idCandidato);
                                rs = getxpStmt.executeQuery();

                                List<MensagemVisComp.Comp> competenciasExperiencias = new ArrayList<>();
                                while (rs.next()) {
                                    String comp = rs.getString("competencia");
                                    int exp = rs.getInt("tempo");
                                    competenciasExperiencias.add(new MensagemVisComp.Comp(comp, exp));
                                }

                                if (!competenciasExperiencias.isEmpty()) {
                                    MensagemVisComp msg = new MensagemVisComp(action, competenciasExperiencias, 201);
                                    final var objectMapper = new ObjectMapper();
                                    final String json = objectMapper.writeValueAsString(msg);
                                    System.out.println(json);
                                    out.println(json);
                                } else {
                                    MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Nenhuma competência/experiência encontrada.");
                                    final var objectMapper = new ObjectMapper();
                                    final String json = objectMapper.writeValueAsString(msg);
                                    System.out.println(json);
                                    out.println(json);
                                }

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            MensagemPADRAO msg = new MensagemPADRAO(action, 404, "Candidato não encontrada");
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);
                        }

                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 403, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "atualizarCompetenciaExperiencia":
                    System.out.println(action);

                    String emailA = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    JsonNode competenciaExperienciaNode = jsonNode.get("competenciaExperiencia");
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;
                    if (Token.validarToken(action, emailA, token)) {

                        List<CompetenciaExperiencia> competenciaExperienciaList = new ArrayList<>();
                        for (JsonNode node : competenciaExperienciaNode) {
                            String competencia = node.get("competencia").asText();
                            int experiencia = node.get("experiencia").asInt();
                            competenciaExperienciaList.add(new CompetenciaExperiencia(competencia, experiencia));
                        }

                        try (Connection con = ConectarBanco.obterConexao()) {
                            for (CompetenciaExperiencia ce : competenciaExperienciaList) {
                                String query = "UPDATE candidato_competencia cc " +
                                        "JOIN candidato c ON cc.id_candidato = c.id_candidato " +
                                        "JOIN competencia comp ON cc.id_competencia = comp.id_competencia " +
                                        "SET cc.tempo = ? " +
                                        "WHERE c.email = ? AND comp.competencia = ?";
                                try (PreparedStatement stmt = con.prepareStatement(query)) {
                                    stmt.setInt(1, ce.getExperiencia());
                                    stmt.setString(2, emailA);
                                    stmt.setString(3, ce.getCompetencia());
                                    stmt.executeUpdate();

                                    int linhasAfetadas = stmt.executeUpdate();

                                    if (linhasAfetadas > 0) {
                                        System.out.println("Competência/Experiência atualizada com sucesso");
                                        MensagemPADRAO mensagem = new MensagemPADRAO(action, 201, "Competencia/Experiencia atualizada com sucesso");
                                        final var objectMapper = new ObjectMapper();
                                        final String json = objectMapper.writeValueAsString(mensagem);
                                        System.out.println(json);
                                        out.println(json);
                                    } else {
                                        System.out.println("Erro ao atualizar Competência/Experiência");
                                        MensagemPADRAO mensagem = new MensagemPADRAO(action, 422, "Erro ao atualizar Competência/Experiência");
                                        final var objectMapper = new ObjectMapper();
                                        final String json = objectMapper.writeValueAsString(mensagem);
                                        out.println(json);
                                    }
                                }

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();

                        }

                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 403, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "apagarCompetenciaExperiencia":
                    System.out.println(action);

                    String emailAp = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    JsonNode competenciaExperienciaNodeAp = jsonNode.get("competenciaExperiencia");
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action, emailAp, token)) {
                        try (Connection con = ConectarBanco.obterConexao()) {
                            for (JsonNode node : competenciaExperienciaNodeAp) {
                                String competencia = node.get("competencia").asText();
                                String query = "DELETE cc FROM candidato_competencia cc " +
                                        "JOIN candidato c ON cc.id_candidato = c.id_candidato " +
                                        "JOIN competencia comp ON cc.id_competencia = comp.id_competencia " +
                                        "WHERE c.email = ? AND comp.competencia = ?";
                                try (PreparedStatement stmt = con.prepareStatement(query)) {
                                    stmt.setString(1, emailAp);
                                    stmt.setString(2, competencia);
                                    int linhasAfetadas = stmt.executeUpdate();

                                    if (linhasAfetadas > 0) {
                                        System.out.println("Competência/Experiência apagada com sucesso");
                                        MensagemPADRAO mensagem = new MensagemPADRAO(action, 201, "Competencia/Experiencia apagada com sucesso");
                                        final var objectMapper = new ObjectMapper();
                                        final String json = objectMapper.writeValueAsString(mensagem);
                                        System.out.println(json);
                                        out.println(json);
                                    } else {
                                        System.out.println("Erro ao apagar Competência/Experiência");
                                        MensagemPADRAO mensagem = new MensagemPADRAO(action, 422, "");
                                        final var objectMapper = new ObjectMapper();
                                        final String json = objectMapper.writeValueAsString(mensagem);
                                        out.println(json);
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
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
                    out.println(inputLine + " defaultok");
                    break;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

