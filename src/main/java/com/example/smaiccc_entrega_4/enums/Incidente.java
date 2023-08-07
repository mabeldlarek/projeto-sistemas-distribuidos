package com.example.smaiccc_entrega_4.enums;

public enum Incidente {
    INCIDENTE_1("Alagamento", 1),
    INCIDENTE_2("Deslizamento", 2),
    INCIDENTE_3("Acidente de carro", 3),
    INCIDENTE_4("Obstrução da via", 4),
    INCIDENTE_5("Fissura da via", 5),
    INCIDENTE_6("Pista em obras", 6),
    INCIDENTE_7("Lentidão na pista", 7),
    INCIDENTE_8("Animais na pista", 8),
    INCIDENTE_9("Nevoeiro", 9),
    INCIDENTE_10("Tromba d'água", 10);

    private final String descricao;
    private final int num;

    Incidente(String descricao, int num) {
        this.descricao = descricao;
        this.num = num;
    }

    public String getDescricaoByNum(int num) {
        for (Incidente incidente : values()) {
            if (incidente.getNum() == num) {
                return incidente.getDescricao();
            }
        }
        return null;
    }
    public String getDescricao() {
        return descricao;
    }
    public int getNum() {
        return num;
    }
}