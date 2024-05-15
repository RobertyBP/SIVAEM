package org.example.mensagens;

public class MensagemVizualizacao {
    String operacao;
    int status;
    String nome;

    String senha;

    public MensagemVizualizacao(String operacao, int status, String nome, String senha) {
        this.operacao = operacao;
        this.status = status;
        this.nome = nome;
        this.senha = senha;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
