package org.example.mensagens;

import java.util.List;

public class MensagemVisualizarVaga {

    private String operacao;
    private int  faixaSalarial;
    private String descricao;
    private boolean estado;
    private List<String> competencias;
    private int status;

    public MensagemVisualizarVaga(String operacao, int faixaSalarial, String descricao, boolean estado, List<String> competencias, int status) {
        this.operacao = operacao;
        this.faixaSalarial = faixaSalarial;
        this.descricao = descricao;
        this.estado = estado;
        this.competencias = competencias;
        this.status = status;
    }

    public String getOperacao() {
        return operacao;
    }


    public int getFaixaSalarial() {
        return faixaSalarial;
    }



    public String getDescricao() {
        return descricao;
    }


    public boolean getEstado() {
        return estado;
    }


    public List<String> getCompetencias() {
        return competencias;
    }

}