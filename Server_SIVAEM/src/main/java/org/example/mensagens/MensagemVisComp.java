package org.example.mensagens;

import java.util.List;

public class MensagemVisComp {
    private String operacao;
    private List<Comp> competenciaExperiencia;
    private int status;

    public MensagemVisComp(String operacao, List<Comp> competenciaExperiencia, int status) {
        this.operacao = operacao;
        this.competenciaExperiencia = competenciaExperiencia;
        this.status = status;
    }

    public static class Comp {
        private String competencia;
        private int experiencia;

        public Comp(String competencia, int experiencia) {
            this.competencia = competencia;
            this.experiencia = experiencia;
        }

        public String getCompetencia() {
            return competencia;
        }

        public int getExperiencia() {
            return experiencia;
        }
    }

    public String getOperacao() {
        return operacao;
    }

    public List<Comp> getCompetenciaExperiencia() {
        return competenciaExperiencia;
    }

    public int getStatus() {
        return status;
    }
}
