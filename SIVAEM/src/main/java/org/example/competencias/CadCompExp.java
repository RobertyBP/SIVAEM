package org.example.competencias;

import java.util.List;

public class CadCompExp {
    private String operacao;
    private String email;
    List<CompEXP> competenciaExperiencia;
    private String token;

    public CadCompExp(String operacao, String email, List<CompEXP> competenciaExperiencia, String token) {
        this.operacao = operacao;
        this.email = email;
        this.competenciaExperiencia = competenciaExperiencia;
        this.token = token;
    }

    public static class CompEXP {
        private String competencia;
        private int experiencia;

        public CompEXP(String competencia, int experiencia) {
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

    public String getEmail() {
        return email;
    }

    public List<CompEXP> getCompetenciaExperiencia() {
        return competenciaExperiencia;
    }

    public String getToken() {
        return token;
    }
}