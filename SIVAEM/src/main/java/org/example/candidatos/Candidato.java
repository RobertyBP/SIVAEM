package org.example.candidatos;

import java.util.UUID;

public class Candidato {
    private String operacao;
    private String nome;
    private String email;
    private String senha;

    public Candidato(String operacao, String nome, String email, String senha) {
        this.operacao = operacao;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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

}
