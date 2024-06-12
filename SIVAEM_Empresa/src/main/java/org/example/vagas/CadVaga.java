package org.example.vagas;
import java.util.List;

public class CadVaga {
    private String operacao;
    private String nome;
    private String email;
    private int faixaSalarial;
    private String descricao;
    private boolean estado;
    private List<String> competencias;
    private String token;

    public CadVaga(String operacao, String nome, String email, int faixaSalarial,
                   String descricao, boolean estado,  List<String> competencias, String token) {
        this.operacao = operacao;
        this.nome = nome;
        this.email = email;
        this.faixaSalarial = faixaSalarial;
        this.descricao = descricao;
        this.estado = estado;
        this.competencias = competencias;
        this.token = token;

    }

    public String getOperacao() {
        return operacao;
    }

    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }

    public int getFaixaSalarial() {
        return faixaSalarial;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<String> getCompetencias() {
        return competencias;
    }

    public boolean isEstado() {
        return estado;
    }

    public String getToken() {
        return token;
    }
}
