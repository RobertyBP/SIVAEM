package org.example.vagas;

public class VisVaga {
    private String operacao;
    private int idVaga;
    private String email;
    private String token;

    public VisVaga(String operacao, int idVaga, String email, String token) {
        this.operacao = operacao;
        this.idVaga = idVaga;
        this.email = email;
        this.token = token;
    }

    public String getOperacao() {
        return operacao;
    }

    public int getIdVaga() {
        return idVaga;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
