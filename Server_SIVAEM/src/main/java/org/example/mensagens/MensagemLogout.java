package org.example.mensagens;

public class MensagemLogout {
    String operacao;
    int status;
    public MensagemLogout(String operacao, int status) {
        this.operacao = operacao;
        this.status = status;
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
}
