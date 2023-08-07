package com.example.smaiccc_entrega_4.exceptions;

public class ExcecaoJsonInvalido extends Exception {

    public ExcecaoJsonInvalido(String e) {
        super("Erro JsonInvalido:" + e);
    }

}
