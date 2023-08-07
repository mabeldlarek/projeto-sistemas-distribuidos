package com.example.smaiccc_entrega_4.exceptions;

public class ExcecaoConexaoClienteNula extends Exception {
    public ExcecaoConexaoClienteNula() {
        super("Erro no servidor : conexão é nula.");
    }
}
