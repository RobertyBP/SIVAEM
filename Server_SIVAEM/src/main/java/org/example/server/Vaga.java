package org.example.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.data.ConectarBanco;
import org.example.mensagens.MensagemFiltrarVaga;
import org.example.mensagens.MensagemListarVaga;
import org.example.mensagens.MensagemPADRAO;
import org.example.mensagens.MensagemVisualizarVaga;
import org.example.token.Token;
import org.example.functions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Vaga {
    public static void tratamentoVaga(String action, JsonNode jsonNode, PrintWriter out, String inputLine, Socket clientSocket) {
        String token;
        int idEmpresa;
        try {
            switch (action) {
                case "cadastrarVaga":
                    System.out.println(action + inputLine);

                    String nome = jsonNode.get("nome") != null ? jsonNode.get("nome").asText() : null;
                    String email = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    int faixaSalarial = jsonNode.get("faixaSalarial") != null ? jsonNode.get("faixaSalarial").asInt() : 0 ;
                    String descricao = jsonNode.get("descricao") != null ? jsonNode.get("descricao").asText() : null;
                    boolean estado = jsonNode.get("estado") != null ? jsonNode.get("estado").asBoolean() : false;
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action, email, token)) {

                        idEmpresa = Validacoes.capturarId(action, email);

                        if (idEmpresa != -1) {
                            try (Connection con = ConectarBanco.obterConexao()) {
                                // Inserir vaga na tabela vaga
                                String sql = "INSERT INTO vaga (nome, faixa_salarial, descricao, diposicao, id_empresa) VALUES (?, ?, ?, ?, ?)";
                                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                                pstmt.setString(1, nome);
                                pstmt.setInt(2, faixaSalarial);
                                pstmt.setString(3, descricao);
                                pstmt.setBoolean(4, estado);
                                pstmt.setInt(5, idEmpresa);

                                int linhasAfetadas = pstmt.executeUpdate();
                                if (linhasAfetadas > 0) {
                                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                                    int vagaId = -1;
                                    if (generatedKeys.next()) {
                                        vagaId = generatedKeys.getInt(1);
                                    }

                                    if (vagaId != -1) {
                                        for (JsonNode competenciaNode : jsonNode.get("competencias")) {
                                            String competencia = competenciaNode.asText();
                                            int competenciaId = Validacoes.capturarCompetenciaId(competencia);
                                            if (competenciaId != -1) {
                                                String insertVagaCompetenciaSQL = "INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (?, ?)";
                                                PreparedStatement insertVagaCompetenciaStmt = con.prepareStatement(insertVagaCompetenciaSQL);
                                                insertVagaCompetenciaStmt.setInt(1, vagaId);
                                                insertVagaCompetenciaStmt.setInt(2, competenciaId);
                                                insertVagaCompetenciaStmt.executeUpdate();
                                            }
                                        }

                                        MensagemPADRAO msg = new MensagemPADRAO(action, 201, "Vaga cadastrada com sucesso");
                                        final var objectMapper = new ObjectMapper();
                                        final String json = objectMapper.writeValueAsString(msg);
                                        System.out.println(json);
                                        out.println(json);
                                    }
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Empresa não encontrada");
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);
                        }
                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "visualizarVaga":
                    System.out.println(action + inputLine);

                    int idVagaV = jsonNode.get("idVaga") != null ? jsonNode.get("idVaga").asInt() : 0;
                    String emailV = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    ResultSet rs;
                    if (Token.validarToken(action, emailV, token)) {
                        idEmpresa = Validacoes.capturarId(action, emailV);
                        if (idEmpresa != -1) {
                            try (Connection con = ConectarBanco.obterConexao()) {
                                // Recuperar vaga da tabela vaga
                                String getVagaSQL = "SELECT faixa_salarial, descricao, diposicao FROM vaga WHERE id_vaga = ? AND id_empresa = ?";
                                PreparedStatement getVagaStmt = con.prepareStatement(getVagaSQL);
                                getVagaStmt.setInt(1, idVagaV);
                                getVagaStmt.setInt(2, idEmpresa);
                                rs = getVagaStmt.executeQuery();

                                if (rs.next()) {
                                    int faixaSalarialV = rs.getInt("faixa_salarial");
                                    String descricaoV = rs.getString("descricao");
                                    boolean estadoV = rs.getBoolean("diposicao");

                                    // Recuperar competências da vaga
                                    String getCompetenciasSQL = "SELECT c.competencia FROM competencia c INNER JOIN vaga_competencia vc ON c.id_competencia = vc.id_competencia WHERE vc.id_vaga = ?";
                                    PreparedStatement getCompetenciasStmt = con.prepareStatement(getCompetenciasSQL);
                                    getCompetenciasStmt.setInt(1, idVagaV);
                                    ResultSet competenciasRS = getCompetenciasStmt.executeQuery();

                                    List<String> competencias = new ArrayList<>();
                                    while (competenciasRS.next()) {
                                        competencias.add(competenciasRS.getString("competencia"));
                                    }

                                    MensagemVisualizarVaga msg = new MensagemVisualizarVaga(action, faixaSalarialV, descricaoV, estadoV, competencias,201);
                                    final var objectMapper = new ObjectMapper();
                                    final String json = objectMapper.writeValueAsString(msg);
                                    System.out.println(json);
                                    out.println(json);
                                } else {
                                    MensagemPADRAO msg = new MensagemPADRAO(action, 404, "Vaga não encontrada");
                                    final var objectMapper = new ObjectMapper();
                                    final String json = objectMapper.writeValueAsString(msg);
                                    System.out.println(json);
                                    out.println(json);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            MensagemPADRAO msg = new MensagemPADRAO(action, 404, "Empresa não encontrada");
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);
                        }
                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 404, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "listarVagas":
                    System.out.println(action + inputLine);

                    String emailL = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action, emailL, token)) {
                        idEmpresa = Validacoes.capturarId(action, emailL);
                        if (idEmpresa != -1) {
                            try (Connection con = ConectarBanco.obterConexao()) {
                                // Recuperar lista de vagas da tabela vaga
                                String getVagasSQL = "SELECT id_vaga, nome FROM vaga WHERE id_empresa = ?";
                                PreparedStatement getVagasStmt = con.prepareStatement(getVagasSQL);
                                getVagasStmt.setInt(1, idEmpresa);
                                rs = getVagasStmt.executeQuery();

                                List<MensagemListarVaga.Vaga> vagas = new ArrayList<>();
                                while (rs.next()) {
                                    int idVaga = rs.getInt("id_vaga");
                                    String nomeVaga = rs.getString("nome");
                                    vagas.add(new MensagemListarVaga.Vaga(nomeVaga, idVaga));
                                }

                                MensagemListarVaga msg = new MensagemListarVaga(action, vagas, 201);
                                final var objectMapper = new ObjectMapper();
                                final String json = objectMapper.writeValueAsString(msg);
                                System.out.println(json);
                                out.println(json);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Empresa não encontrada");
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);
                        }

                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "atualizarVaga":
                    System.out.println(action + inputLine);

                    int idVagaA = jsonNode.get("idVaga") != null ? jsonNode.get("idVaga").asInt() : 0;
                    String emailA = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    String nomeA = jsonNode.get("nome") != null ? jsonNode.get("nome").asText() : null;
                    int faixaSalarialA = jsonNode.get("faixaSalarial") != null ? jsonNode.get("faixaSalarial").asInt() : 0;
                    String descricaoA = jsonNode.get("descricao") != null ? jsonNode.get("descricao").asText() : null;
                    boolean estadoA = jsonNode.get("estado") != null ? jsonNode.get("estado").asBoolean() : false;
                    List<String> competencias = List.of(jsonNode.get("competencias").toString().split(","));
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action, emailA, token)) {
                        try (Connection con = ConectarBanco.obterConexao()) {
                            con.setAutoCommit(false);

                            // Atualizar os dados da vaga
                            String sqlUpdateVaga = "UPDATE vaga SET nome = ?, faixa_salarial = ?, descricao = ?, diposicao = ? WHERE id_vaga = ?";
                            PreparedStatement stmtUpdateVaga = con.prepareStatement(sqlUpdateVaga);
                            stmtUpdateVaga.setString(1, nomeA);
                            stmtUpdateVaga.setInt(2, faixaSalarialA);
                            stmtUpdateVaga.setString(3, descricaoA);
                            stmtUpdateVaga.setBoolean(4, estadoA);
                            stmtUpdateVaga.setInt(5, idVagaA);

                            int linhasAfetadas = stmtUpdateVaga.executeUpdate();

                            if (linhasAfetadas > 0) {
                                // Remover competências existentes
                                String sqlDeleteCompetencias = "DELETE FROM vaga_competencia WHERE id_vaga = ?";
                                PreparedStatement stmtDeleteCompetencias = con.prepareStatement(sqlDeleteCompetencias);
                                stmtDeleteCompetencias.setInt(1, idVagaA);
                                stmtDeleteCompetencias.executeUpdate();

                                // Inserir novas competências
                                String sqlInsertCompetencia = "INSERT INTO vaga_competencia (id_vaga, id_competencia) VALUES (?, ?)";
                                PreparedStatement stmtInsertCompetencia = con.prepareStatement(sqlInsertCompetencia);
                                for (String competencia : competencias) {
                                    int idCompetencia = Validacoes.capturarCompetenciaId(competencia.trim());
                                    stmtInsertCompetencia.setInt(1, idVagaA);
                                    stmtInsertCompetencia.setInt(2, idCompetencia);
                                    stmtInsertCompetencia.addBatch();
                                }
                                stmtInsertCompetencia.executeBatch();

                                con.commit();

                                MensagemPADRAO msg = new MensagemPADRAO(action, 201, "Vaga atualizada com sucesso");
                                final var objectMapper = new ObjectMapper();
                                final String json = objectMapper.writeValueAsString(msg);
                                System.out.println(json);
                                out.println(json);
                            } else {
                                con.rollback();
                                MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Erro ao atualizar a vaga");
                                final var objectMapper = new ObjectMapper();
                                final String json = objectMapper.writeValueAsString(msg);
                                System.out.println(json);
                                out.println(json);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Erro ao atualizar a vaga");
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);
                        }
                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "apagarVaga":
                    System.out.println(action + inputLine);

                    int idVagaAp = jsonNode.get("idVaga") != null ? jsonNode.get("idVaga").asInt() : 0;
                    String emailAp = jsonNode.get("email") != null ? jsonNode.get("email").asText() : null;
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarToken(action, emailAp, token)) {
                        idEmpresa = Validacoes.capturarId(action, emailAp);
                        if (idEmpresa != -1) {
                            try (Connection con = ConectarBanco.obterConexao()) {

                                String sqlDelComp = "DELETE FROM vaga_competencia WHERE id_vaga = ?";
                                PreparedStatement stmtDelComp = con.prepareStatement(sqlDelComp);
                                stmtDelComp.setInt(1, idVagaAp);
                                stmtDelComp.executeUpdate();

                                String sqlDelVaga = "DELETE FROM vaga WHERE id_vaga = ? AND id_empresa = ?";
                                PreparedStatement stmtDelVaga = con.prepareStatement(sqlDelVaga);
                                stmtDelVaga.setInt(1, idVagaAp);
                                stmtDelVaga.setInt(2, idEmpresa);

                                int linhasAfetadas = stmtDelVaga.executeUpdate();

                                if (linhasAfetadas > 0) {
                                    MensagemPADRAO msg = new MensagemPADRAO(action, 201, "Vaga apagada com sucesso");
                                    final var objectMapper = new ObjectMapper();
                                    final String json = objectMapper.writeValueAsString(msg);
                                    System.out.println(json);
                                    out.println(json);
                                } else {
                                    MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Erro ao apagar a vaga");
                                    final var objectMapper = new ObjectMapper();
                                    final String json = objectMapper.writeValueAsString(msg);
                                    System.out.println(json);
                                    out.println(json);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Erro ao apagar a vaga");
                                final var objectMapper = new ObjectMapper();
                                final String json = objectMapper.writeValueAsString(msg);
                                System.out.println(json);
                                out.println(json);
                            }
                        } else {
                            MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Empresa não encontrada");
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);
                        }
                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                case "filtrarVagas":
                    System.out.println(action + inputLine);

                    JsonNode filtrosNode = jsonNode.get("filtros");
                    JsonNode competenciasNode = filtrosNode.get("competencias");
                    String tipo = filtrosNode.get("tipo").asText() != null ? filtrosNode.get("tipo").asText() : null;
                    token = jsonNode.get("token") != null ? jsonNode.get("token").asText() : null;

                    if (Token.validarTokenF(action, token)) {
                        try (Connection con = ConectarBanco.obterConexao()) {
                            StringBuilder sqlBuilder = new StringBuilder(
                                    "SELECT v.id_vaga, v.nome, v.faixa_salarial, v.descricao, v.diposicao, e.email, " +
                                            "GROUP_CONCAT(c.competencia SEPARATOR ',') AS competencias " +
                                            "FROM vaga v " +
                                            "INNER JOIN vaga_competencia vc ON v.id_vaga = vc.id_vaga " +
                                            "INNER JOIN competencia c ON vc.id_competencia = c.id_competencia " +
                                            "INNER JOIN empresa e ON v.id_empresa = e.id_empresa " +
                                            "WHERE "
                            );

                            // Construção da consulta SQL com base no tipo
                            if (tipo.equalsIgnoreCase("OR")) {
                                sqlBuilder.append("c.competencia IN (");
                                for (int i = 0; i < competenciasNode.size(); i++) {
                                    sqlBuilder.append("?");
                                    if (i < competenciasNode.size() - 1) {
                                        sqlBuilder.append(", ");
                                    }
                                }
                                sqlBuilder.append(")");


                                sqlBuilder.append(" GROUP BY v.id_vaga");

                                PreparedStatement stmt = con.prepareStatement(sqlBuilder.toString());
                                int index = 1;
                                for (JsonNode competencia : competenciasNode) {
                                    stmt.setString(index++, competencia.asText());
                                }
                                if (tipo.equalsIgnoreCase("AND")) {
                                    stmt.setInt(index, competenciasNode.size());
                                }

                                rs = stmt.executeQuery();

                                List<MensagemFiltrarVaga.Vaga> vagas = new ArrayList<>();
                                while (rs.next()) {
                                    int idVaga = rs.getInt("id_vaga");
                                    String nomeM = rs.getString("nome");
                                    int faixaSalarialM = rs.getInt("faixa_salarial");
                                    String descricaoM = rs.getString("descricao");
                                    boolean estadoM = rs.getBoolean("diposicao");
                                    String emailM = rs.getString("email");
                                    String competenciasStr = rs.getString("competencias");
                                    List<String> competenciasList = Arrays.asList(competenciasStr.split(","));

                                    vagas.add(new MensagemFiltrarVaga.Vaga(idVaga, nomeM, faixaSalarialM, descricaoM, estadoM, competenciasList, emailM));
                                }

                                MensagemFiltrarVaga msg = new MensagemFiltrarVaga(action, vagas,201);
                                final var objectMapper = new ObjectMapper();
                                final String json = objectMapper.writeValueAsString(msg);
                                System.out.println(json);
                                out.println(json);
                            }
                            if (tipo.equalsIgnoreCase("AND")) {
                                sqlBuilder.append("c.competencia IN (");
                                for (int i = 0; i < competenciasNode.size(); i++) {
                                    sqlBuilder.append("?");
                                    if (i < competenciasNode.size() - 1) {
                                        sqlBuilder.append(", ");
                                    }
                                }
                                sqlBuilder.append(") GROUP BY v.id_vaga HAVING COUNT(DISTINCT c.competencia) = ?");


                            PreparedStatement stmt = con.prepareStatement(sqlBuilder.toString());
                            int index = 1;
                            for (JsonNode competencia : competenciasNode) {
                                stmt.setString(index++, competencia.asText());
                            }
                            if (tipo.equalsIgnoreCase("AND")) {
                                stmt.setInt(index, competenciasNode.size());
                            }

                            rs = stmt.executeQuery();

                            List<MensagemFiltrarVaga.Vaga> vagas = new ArrayList<>();
                            while (rs.next()) {
                                int idVaga = rs.getInt("id_vaga");
                                String nomeM = rs.getString("nome");
                                int faixaSalarialM = rs.getInt("faixa_salarial");
                                String descricaoM = rs.getString("descricao");
                                boolean estadoM = rs.getBoolean("diposicao");
                                String emailM = rs.getString("email");
                                String competenciasStr = rs.getString("competencias");
                                List<String> competenciasList = Arrays.asList(competenciasStr.split(","));

                                vagas.add(new MensagemFiltrarVaga.Vaga(idVaga, nomeM, faixaSalarialM, descricaoM, estadoM, competenciasList, emailM));
                            }

                            MensagemFiltrarVaga msg = new MensagemFiltrarVaga(action, vagas, 201);
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);}
                        } catch (SQLException e) {
                            e.printStackTrace();
                            MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Erro ao filtrar vagas");
                            final var objectMapper = new ObjectMapper();
                            final String json = objectMapper.writeValueAsString(msg);
                            System.out.println(json);
                            out.println(json);
                        }
                    } else {
                        MensagemPADRAO msg = new MensagemPADRAO(action, 422, "Token inválido");
                        final var objectMapper = new ObjectMapper();
                        final String json = objectMapper.writeValueAsString(msg);
                        System.out.println(json);
                        out.println(json);
                    }
                    break;
                default:
                    System.out.println("Ação não reconhecida");
                    break;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
