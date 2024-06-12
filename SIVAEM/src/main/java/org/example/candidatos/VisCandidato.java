package org.example.candidatos;

public class VisCandidato {

    private String operacao;
    private String email;
    private String token;

    public VisCandidato(String operacao, String email, String token) {
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