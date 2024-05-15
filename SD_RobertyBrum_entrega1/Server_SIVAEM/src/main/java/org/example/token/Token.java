package org.example.token;

import org.example.data.ConectarBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Token {
    public static UUID criarToken(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }
    public static String capturaToken(String email, String password){
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
        return null;
    }
}
