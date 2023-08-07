package com.example.smaiccc_entrega_4.exceptions;

public class ExcecaoErroComunicacaoServidor extends Exception {
    public ExcecaoErroComunicacaoServidor(String e) {
        super("Erro de comunicação com o servidor: " + e);
    }
}
