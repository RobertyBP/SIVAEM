package org.example.token;

import org.example.data.ConectarBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Token {
    public static UUID criarToken() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }
    public static boolean validarTokenF(String action, String token) {
        System.out.println("filtroooooo loko");
        if (action.equals("filtrarVagas")) {
            try (Connection conn = ConectarBanco.obterConexao()) {
                String sql = "SELECT * FROM empresa e , candidato c WHERE e.token = ? OR c.token = ? ";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, token);

                    stmt.setString(2, token);

                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next(); // Retorna true se encontrar um registro com o token correspondente
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean validarToken(String action, String email, String token) {
        if((action.equals("Logout") && email.equals("default"))){
            try (Connection conn = ConectarBanco.obterConexao()) {
                String sql ="SELECT * FROM empresa e , candidato c WHERE e.token = ? OR c.token = ? ";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, token);

                    stmt.setString(2, token);

                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next(); // Retorna true se encontrar um registro com o token correspondente
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (action.contains("Candidato") || action.contains("CompetenciaExperiencia")) {
            try (Connection conn = ConectarBanco.obterConexao()) {
                String sql = "SELECT * FROM candidato WHERE email = ? AND token = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, token);

                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next(); // Retorna true se encontrar um registro com o token correspondente
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (action.contains("Empresa") || action.contains("Vaga")) {
            System.out.println(email+" "+token);
            try (Connection conn = ConectarBanco.obterConexao()) {
                String sql =  "SELECT * FROM empresa WHERE email = ? AND token = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, token);

                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next(); // Retorna true se encontrar um registro com o token correspondente
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String capturaToken(String email, String password, String action) {
        if (action.contains("Candidato")) {
            try (Connection con = ConectarBanco.obterConexao()) {
                String sql = "SELECT token FROM candidato WHERE email = ? AND senha = ?";

                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, email);
                    pstmt.setString(2, password);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getString("token");
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (Connection con = ConectarBanco.obterConexao()) {
                String sql = "SELECT token FROM empresa WHERE email = ? AND senha = ?";

                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, email);
                    pstmt.setString(2, password);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getString("token");
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
