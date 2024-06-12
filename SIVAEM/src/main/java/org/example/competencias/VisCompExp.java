package org.example.competencias;

public class VisCompExp {
    private String operacao;
    private String email;
    private String token;

    public VisCompExp(String operacao, String email, String token) {
        this.operacao = operacao;
        this.email = email;
        this.token = token;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
