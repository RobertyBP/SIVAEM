package org.example.empresas;

public class AtlEmpresa {
    private String operacao;
    private String email;
    private String razaoSocial;
    private String cnpj;
    private String senha;
    private String descricao;
    private String ramo;
    private String token;

    public AtlEmpresa(String operacao, String email, String razaoSocial, String cnpj, String senha, String descricao, String ramo, String token) {
        this.operacao = operacao;
        this.email = email;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.senha = senha;
        this.descricao = descricao;
        this.ramo = ramo;
        this.token = token;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getEmail() {
        return email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getSenha() {
        return senha;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getRamo() {
        return ramo;
    }

    public String getToken() {
        return token;
    }
}
