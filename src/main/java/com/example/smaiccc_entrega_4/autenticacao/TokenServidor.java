package com.example.smaiccc_entrega_4.autenticacao;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TokenServidor {
    private static final Map<String, String> tokenMap = new HashMap<>();

    public TokenServidor() {

    }


    public void incluirTokenNovoCliente(String id) {
        if(getToken(id)!="" || getToken(id)!=null) {
            String token = gerarToken();
            tokenMap.put(id, token);
        }
    }

    private static String gerarToken() {
        Random random = new Random();
        String token = "";
        for (int i = 0; i < 10; i++) {
            token += (char) (random.nextInt(26) + 'a');
        }
        return token;
    }

    public Map getTokens(){
        return tokenMap;
    }

    public static String getToken(String id){
        return tokenMap.get(id);
    }

    public void encerrarToken(int id){
        tokenMap.remove(id);
    }

    public int getNumTokensAtivos(){
        return tokenMap.size();
    }


}
