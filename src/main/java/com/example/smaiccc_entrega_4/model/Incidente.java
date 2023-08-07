package com.example.smaiccc_entrega_4.model;

public class Incidente {
    private int id_incidente;
    private String data;
    private String estado;
    private String cidade;
    private String bairro;
    private String hora;
    private String rua;
    private int tipo_incidente;

    public Incidente(String data, String estado, String cidade, String bairro, String hora, String rua, int tipo_incidente, int id_incidente) {
        this.id_incidente = id_incidente;
        this.data = data;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.hora = hora;
        this.rua = rua;
        this.tipo_incidente = tipo_incidente;
    }

    public String getData() {
        return data;
    }

    public String getEstado() {
        return estado;
    }

    public String getCidade() {
        return cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public String getHora() {
        return hora;
    }

    public String getRua() {
        return rua;
    }

    public int getTipo_incidente() {
        return tipo_incidente;
    }

    public int getId_incidente() {
        return id_incidente;
    }
}
