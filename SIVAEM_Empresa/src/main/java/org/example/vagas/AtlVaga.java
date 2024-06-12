package org.example.vagas;

import java.util.List;

public class AtlVaga {

    private String operacao;
    private int idVaga;
    private String nome;
    private String email;
    private int faixaSalarial;
    private String descricao;
    private boolean estado;
    private List<String> competencias;
    private String token;

    public AtlVaga(String operacao, int idVaga,  String nome, String email,
                   int faixa_salarial, String descricao, boolean estado, List<String> competencias, String token) {
        this.operacao = operacao;
        this.idVaga = idVaga;
        this.nome = nome;
        this.email = email;
        this.faixaSalarial = faixa_salarial;
        this.descricao = descricao;
        this.estado = estado;
        this.competencias = competencias;
        this.token = token;
    }

    public List<String> getCompetencias() {
        return competencias;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEstado() {
        return estado;
    }

    public int getFaixasalarial() {
        return faixaSalarial;
    }

    public int getIdVaga() {
        return idVaga;
    }

    public String getNome() {
        return nome;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getToken() {
        return token;
    }
}
