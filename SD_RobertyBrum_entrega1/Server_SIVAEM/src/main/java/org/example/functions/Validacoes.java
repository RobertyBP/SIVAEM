package org.example.functions;

import org.example.data.ConectarBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacoes {
    public static boolean compararEmail(String email) {
        try (Connection conn = ConectarBanco.obterConexao()) {
            String sql = "SELECT * FROM candidato";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    if(rs.getString("email").equals(email)){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean validate(String email, String password) {
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
        try (Connection con = ConectarBanco.obterConexao()) {
            String sql = "SELECT email, senha FROM candidato WHERE email = ?";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, email);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        if(rs.getString("senha").equals(password)){
                            return true;
                        }else {
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
