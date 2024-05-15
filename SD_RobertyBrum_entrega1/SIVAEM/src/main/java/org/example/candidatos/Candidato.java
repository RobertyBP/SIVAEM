package org.example.candidatos;

import java.util.UUID;

public class Candidato {
    private String operacao;
    private String nome;
    private String email;
    private String senha;
    private UUID token;

    public Candidato(String operacao, String email, String nome,String senha) {
        this.operacao = operacao;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
    }
    public Candidato(String operacao, String email, String senha) {
        this.operacao = operacao;
        this.email = email;
        this.senha = senha;
    }
    public Candidato(String operacao, String email) {
        this.operacao = operacao;
        this.email = email;
    }
    public Candidato(String operacao, UUID token) {
        this.operacao = operacao;
        this.token = token;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
