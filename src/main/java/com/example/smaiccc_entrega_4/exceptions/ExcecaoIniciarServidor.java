package com.example.smaiccc_entrega_4.exceptions;

public class ExcecaoIniciarServidor extends Exception{
    public ExcecaoIniciarServidor(String e ) {
        super(e);
    }

    public ExcecaoIniciarServidor() {
        super("Erro no servidor : não foi possível estabelecer conexão.");
    }

}
