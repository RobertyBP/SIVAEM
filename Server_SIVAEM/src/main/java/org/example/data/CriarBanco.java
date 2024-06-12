package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CriarBanco {
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/sivaem";
    static final String USUARIO = "root";
    static final String SENHA = "";

    public static String candidato(){
        String table = "CREATE TABLE IF NOT EXISTS candidato " +
                "(id_candidato INT PRIMARY KEY AUTO_INCREMENT, " +
                " nome VARCHAR(50), " +
                " email VARCHAR(50),"+
                " token VARCHAR(50),"+
                " senha VARCHAR(50))";
        return table;
    }
    public static String competencia(){
        String table = "CREATE TABLE IF NOT EXISTS competencia " +
                "(id_competencia INT PRIMARY KEY AUTO_INCREMENT, " +
                " competencia VARCHAR(50))";
        return table;
    }
    public static String empresa(){
        String table = "CREATE TABLE IF NOT EXISTS empresa " +
                "(id_empresa INT PRIMARY KEY AUTO_INCREMENT, " +
                " razao_social VARCHAR(50), " +
                " ramo VARCHAR(50),"+
                " descricao VARCHAR(255),"+
                " email VARCHAR(50),"+
                " token VARCHAR(50),"+
                " senha VARCHAR(50),"+
                " cnpj VARCHAR(50))";
        return table;
    }
    public static String vaga(){
        String table = "CREATE TABLE IF NOT EXISTS vaga " +
                "(id_vaga INT PRIMARY KEY AUTO_INCREMENT, " +
                " nome VARCHAR(50),"+
                " faixa_salarial INT,"+
                " descricao VARCHAR(255)," +
                " diposicao BOOLEAN," +
                " id_empresa INT,"+
                "FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa))";
        return table;
    }
    public static String candidato_vaga(){
        String table = "CREATE TABLE IF NOT EXISTS candidato_vaga " +
                "(id_candidato_vaga INT PRIMARY KEY AUTO_INCREMENT, " +
                " visualizou BOOLEAN,"+
                " id_candidato INT ,"+
                " id_vaga INT,"+
                " FOREIGN KEY (id_vaga) REFERENCES vaga(id_vaga),"+
                " FOREIGN KEY (id_candidato) REFERENCES candidato(id_candidato))";
        return table;
    }
    public static String vaga_competencia(){
        String table = "CREATE TABLE IF NOT EXISTS vaga_competencia"+
                "(id_vaga_competencia INT PRIMARY KEY AUTO_INCREMENT,"+
                " id_vaga INT,"+
                " id_competencia INT,"+
                " FOREIGN KEY (id_vaga) REFERENCES vaga(id_vaga),"+
                " FOREIGN KEY (id_competencia) REFERENCES competencia(id_competencia))";
        return table;
    }
    public static String candidato_competencia(){
        String table = "CREATE TABLE IF NOT EXISTS candidato_competencia"+
                "(id_candidato_competencia INT PRIMARY KEY AUTO_INCREMENT,"+
                " tempo INT,"+
                " id_candidato INT,"+
                " id_competencia INT,"+
                " FOREIGN KEY (id_candidato) REFERENCES candidato(id_candidato),"+
                " FOREIGN KEY (id_competencia) REFERENCES competencia(id_competencia))";
        return table;
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            // Passo 1: Registrar o driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Passo 2: Abrir uma conexão
            System.out.println("Conectando ao banco de dados...");
            conn = DriverManager.getConnection(JDBC_URL, USUARIO, SENHA);

            // Passo 3: Criar uma instrução
            System.out.println("Criando uma tabela...");
            stmt = conn.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS SIVAEM";
            stmt.executeUpdate(sql);

            stmt = conn.createStatement();
            String tables[];
            tables = new String[7];
            tables[0] = candidato();
            tables[1] = competencia();
            tables[2] = empresa();
            tables[3] = vaga();
            tables[4] = candidato_vaga();
            tables[5] = vaga_competencia();
            tables[6] = candidato_competencia();

            for(String table:tables){
                stmt.executeUpdate(table);
                System.out.println("ok");
            }


            System.out.println("Tabelas criadas com sucesso!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // Fechar os recursos
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}