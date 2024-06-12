package org.example.empresas;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Logout {
    private String operacao;
    private String token;

    public Logout(String operacao, String token) {
        this.operacao = operacao;
        this.token = token;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getToken() {
        return token;
    }
    public static boolean logout(String token, PrintWriter out, BufferedReader in, Socket echoSocket) throws IOException {
        System.out.println("logout");
        Logout logout = new Logout("logout", token);
        final var objectMapperLo = new ObjectMapper();
        final String jsonLo = objectMapperLo.writeValueAsString(logout);
        System.out.println(jsonLo);

        out.println(jsonLo);

        System.out.println("client: " + in.readLine());
        return false;
    }
}
