package org.example.mensagens;

import java.util.UUID;

public class MensagemTOKEN {
    String operacao;

    int status;
    String token;
    public MensagemTOKEN(String operacao, int status, String token) {
        this.operacao = operacao;
        this.status = status;
        this.token = token;
    }
    public MensagemTOKEN(String operacao, String token) {
        this.operacao = operacao;
        this.token = token;
    }
    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

