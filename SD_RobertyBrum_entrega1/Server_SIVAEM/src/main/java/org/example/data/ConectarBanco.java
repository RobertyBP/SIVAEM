package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectarBanco {
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/sivaem";
    static final String USUARIO = "root";
    static final String SENHA = "";

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USUARIO, SENHA);
    }
}