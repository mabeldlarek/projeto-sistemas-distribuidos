package com.example.smaiccc_entrega_4.exceptions;

public class ExcecaoRespostaNulaServidor extends Exception {
    public ExcecaoRespostaNulaServidor() {
        super("resposta do servidor é nula");
    }
}
