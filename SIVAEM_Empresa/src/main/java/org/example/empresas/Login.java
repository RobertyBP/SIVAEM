package org.example.empresas;
import java.util.UUID;

public class Login {

    private String operacao;
    private String email;
    private String senha;

    public Login(String operacao, String email, String senha) {
        this.operacao = operacao;
        this.email = email;
        this.senha = senha;
    }
    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
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
