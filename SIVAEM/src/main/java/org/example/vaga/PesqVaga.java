package org.example.vaga;

import java.util.List;

public class PesqVaga {
    private String operacao;
    private Filtros filtros;
    private String token;

    public PesqVaga(String operacao, Filtros filtros, String token) {
        this.operacao = operacao;
        this.filtros = filtros;
        this.token = token;
    }

    public static class Filtros{

        private List<String> competencias;
        private String tipo;

        public Filtros(List<String> competencias, String tipo) {
            this.competencias = competencias;
            this.tipo = tipo;
        }

        public List<String> getCompetencias() {
            return competencias;
        }

        public String getTipo() {
            return tipo;
        }
    }

    public String getOperacao() {
        return operacao;
    }

    public Filtros getFiltros() {
        return filtros;
    }

    public String getToken() {
        return token;
    }

}
