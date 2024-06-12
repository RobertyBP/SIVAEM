package org.example.vagas;

public class LisVaga {
    private String operacao;
    private String email;
    private String token;

    public LisVaga(String operacao, String email, String token) {
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
