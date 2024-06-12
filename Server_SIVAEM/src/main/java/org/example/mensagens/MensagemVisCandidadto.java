package org.example.mensagens;

public class MensagemVisCandidadto {
    String operacao;
    int status;
    String nome;
    String senha;
    public MensagemVisCandidadto(String operacao, int status, String nome, String senha) {
        this.operacao = operacao;
        this.status = status;
        this.nome = nome;
        this.senha = senha;
    }

    public String getOperacao() {
        return operacao;
    }

    public int getStatus() {
        return status;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }
}
