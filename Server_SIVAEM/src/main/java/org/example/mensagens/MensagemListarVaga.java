package org.example.mensagens;

import java.util.List;

public class MensagemListarVaga {
    private String operacao;
    private List<Vaga> vagas;
    private int status;

    public MensagemListarVaga(String operacao, List<Vaga> vagas, int status) {
        this.operacao = operacao;
        this.vagas = vagas;
        this.status = status;
    }

    public static class Vaga {
        private String nomeVaga;
        private int idVaga;

        public Vaga(String nomeVaga, int idVaga) {
            this.nomeVaga = nomeVaga;
            this.idVaga = idVaga;
        }

        public String getNomeVaga() {
            return nomeVaga;
        }


        public int getIdVaga() {
            return idVaga;
        }


    }
    public String getOperacao() {
        return operacao;
    }

    public List<Vaga> getVagas() {
        return vagas;
    }

    public int getStatus() {
        return status;
    }
}
