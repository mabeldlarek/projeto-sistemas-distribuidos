package com.example.smaiccc_entrega_4.autenticacao;

public class CriptografiaCesar {
    private String senhaOriginal;
    private String senhaCripografada;

    public CriptografiaCesar(String senha) {
        this.senhaOriginal = senha;
        senhaCripografada = criptografar();
    }

    private String criptografar(){
            StringBuilder senhaCifrada = new StringBuilder();
            for (int i = 0; i < senhaOriginal.length(); i++) {
                int codigo = (int) senhaOriginal.charAt(i) + 2 % 256;
                senhaCifrada.append((char) codigo);
            }
            return senhaCifrada.toString();
    }

    private String descriptografar() {
        StringBuilder senhaDescriptografada = new StringBuilder();
        for (int i = 0; i < senhaCripografada.length(); i++) {
            int codigo = ((int) senhaCripografada.charAt(i) - 2 + 256) % 256;
            senhaDescriptografada.append((char) codigo);
        }
        return senhaDescriptografada.toString();
    }

    public String obterSenhaCriptografada(){
        senhaCripografada = criptografar();
        return senhaCripografada;
    }

    public String obterSenhaDescriptografada(){
        senhaOriginal = descriptografar();
        return senhaOriginal;
    }

    public String getSenhaOriginal() {
        return senhaOriginal;
    }

    public void setSenhaOriginal(String senhaOriginal) {
        this.senhaOriginal = senhaOriginal;
    }

    public String getSenhaCriptografada() {
        return senhaCripografada;
    }

    public void setSenhaCriptografada(String senhaCripografada) {
        this.senhaCripografada = senhaCripografada;
    }
}
