package org.example.mensagens;

import java.util.UUID;

public class MensagemPADRAO {
    String operacao;
    int status;
    String mensagem;

    public MensagemPADRAO(String operacao, int status, String mensagem) {
        this.operacao = operacao;
        this.status = status;
        this.mensagem = mensagem;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }



}
