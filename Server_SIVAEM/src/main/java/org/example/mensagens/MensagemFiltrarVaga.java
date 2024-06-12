package org.example.mensagens;

import java.util.List;

public class MensagemFiltrarVaga {
    private String operacao;
    private List<Vaga> vagas;
    private int status;

    public MensagemFiltrarVaga(String operacao, List<Vaga> vagas, int status) {
        this.operacao = operacao;
        this.vagas = vagas;
        this.status = status;
    }

    public static class Vaga {
        private int idVaga;
        private String nome;
        private int faixaSalarial;
        private String descricao;
        private boolean estado;
        private List<String> competencias;
        private String email;

        public Vaga(int idVaga, String nome, int faixaSalarial, String descricao, boolean estado, List<String> competencias, String email) {
            this.idVaga = idVaga;
            this.nome = nome;
            this.faixaSalarial = faixaSalarial;
            this.descricao = descricao;
            this.estado = estado;
            this.competencias = competencias;
            this.email = email;
        }

        public int getIdVaga() {
            return idVaga;
        }

        public String getNome() {
            return nome;
        }

        public int getFaixaSalarial() {
            return faixaSalarial;
        }

        public String getDescricao() {
            return descricao;
        }

        public boolean isEstado() {
            return estado;
        }

        public List<String> getCompetencias() {
            return competencias;
        }

        public String getEmail() {
            return email;
        }
    }

    public String getOperacao() {
        return operacao;
    }

    public List<Vaga> getVagas() {
        return vagas;
    }
}
