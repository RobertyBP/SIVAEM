package org.example.functions;

import org.example.data.ConectarBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacoes {
    public static boolean compararEmail(String email, String action) {
        if (isValidEmail(email)) {
            if (action.contains("Candidato")) {
                try (Connection conn = ConectarBanco.obterConexao()) {
                    String sql = "SELECT * FROM candidato";
                    try (PreparedStatement stmt = conn.prepareStatement(sql);
                         ResultSet rs = stmt.executeQuery()) {

                        while (rs.next()) {
                            if (rs.getString("email").equals(email)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try (Connection conn = ConectarBanco.obterConexao()) {
                    String sql = "SELECT * FROM empresa";
                    try (PreparedStatement stmt = conn.prepareStatement(sql);
                         ResultSet rs = stmt.executeQuery()) {

                        while (rs.next()) {
                            if (rs.getString("email").equals(email)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return true;
        }
        return true;
    }

    public static boolean iaValidCNPJ(String cnpj) {
        if (cnpj.length() == 14) {
            try (Connection conn = ConectarBanco.obterConexao()) {
                String sql = "SELECT * FROM empresa";
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        if (rs.getString("cnpj").equals(cnpj)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return true;
        }
        return true;
    }

    public static int capturarCompetenciaId(String competencia) throws SQLException {
        int i = -1;
        try (Connection con = ConectarBanco.obterConexao()) {
            String sql = "SELECT id_competencia FROM competencia WHERE competencia = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, competencia);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id_competencia");
                } else {
                    sql = "INSERT INTO competencia (competencia) VALUES (?)";
                    try (PreparedStatement pstmt1 = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        pstmt1.setString(1, competencia);
                        pstmt1.executeUpdate();
                        rs = pstmt1.getGeneratedKeys();
                        if (rs.next()) {
                            capturarCompetenciaId(competencia);
                        } else {
                            throw new SQLException("Erro ao criar competência");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return i;
    }

    public static int capturarId(String action, String email) {
        if (action.contains("Vaga") || action.contains("Vagas")) {
            try (Connection con = ConectarBanco.obterConexao()) {
                // Recuperar id_empresa a partir do email
                String sql = "SELECT id_empresa FROM empresa WHERE email = ?";
                try (PreparedStatement getEmpresaIdStmt = con.prepareStatement(sql)) {
                    getEmpresaIdStmt.setString(1, email);
                    ResultSet rs = getEmpresaIdStmt.executeQuery();
                    int idEmpresa = -1;
                    if (rs.next()) {
                        return idEmpresa = rs.getInt("id_empresa");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (Connection con = ConectarBanco.obterConexao()) {
                // Recuperar id_empresa a partir do email
                String sql = "SELECT id_candidato FROM candidato WHERE email = ?";
                try (PreparedStatement getEmpresaIdStmt = con.prepareStatement(sql)) {
                    getEmpresaIdStmt.setString(1, email);
                    ResultSet rs = getEmpresaIdStmt.executeQuery();
                    int idCandidato = -1;
                    if (rs.next()) {
                        return idCandidato = rs.getInt("id_candidato");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return -1;
    }


    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validate(String email, String password, String action) {
        if (email == null || password == null) {
            System.out.println("Error. um ou mais campos estão faltando.");
            System.out.println("Email: " + email);
            System.out.println("Senha: " + password);

            return false;
        }
        if (!isValidEmail(email)) {
            System.out.println("Error. email não está em um formato válido.");
            return false;
        }
        if (action.contains("Candidato")) {
            try (Connection con = ConectarBanco.obterConexao()) {
                String sql = "SELECT email, senha FROM candidato WHERE email = ?";

                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, email);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            if (rs.getString("senha").equals(password)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (Connection con = ConectarBanco.obterConexao()) {
                String sql = "SELECT email, senha FROM empresa WHERE email = ?";

                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, email);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            if (rs.getString("senha").equals(password)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
