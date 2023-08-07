package com.example.smaiccc_entrega_4.validacao;

import com.example.smaiccc_entrega_4.exceptions.ExcecaoJsonInvalido;
import org.json.JSONException;
import org.json.JSONObject;

public class ValidaPedidoCliente {

    private String pedido;
    private int operacao;
    private JSONObject jsonPedido;

    public ValidaPedidoCliente(String pedido) throws ExcecaoJsonInvalido {
        this.pedido = pedido;
        validarFormatoJson();
        this.operacao = obterChaveOperacao();
        validarChaves();
    }

    private void validarFormatoJson() throws ExcecaoJsonInvalido {
        try {
            jsonPedido = new JSONObject(pedido);
        } catch (JSONException e) {
            throw new ExcecaoJsonInvalido(e.getMessage());
        }
    }

    private void validarChaves() throws ExcecaoJsonInvalido {
        boolean resultado = false;
        if(operacao == 1)
            resultado = validarChavesCadastro();
        if(operacao == 2)
            resultado = validarChavesLogin();
        if(operacao == 3)
            resultado = validarChavesAlteracao();
        if(operacao == 4)
            resultado = validarChavesListagem();
        if(operacao == 5)
            resultado = validarChavesListagemUsuario();
        if(operacao == 6)
            resultado = validarChavesIncidenteExclusao();
        if(operacao == 7)
            resultado = validarChavesIncidenteCadastro();
        if(operacao == 8)
            resultado = validarChavesExclusao();
        if(operacao == 9)
            resultado = validarChavesLogout();

        if(resultado) return;
        else throw new ExcecaoJsonInvalido("Chave(s) inválida(s)");
    }

    private int obterChaveOperacao() throws ExcecaoJsonInvalido {
        if(jsonPedido.has("operacao")) return jsonPedido.getInt("operacao");
        else throw new ExcecaoJsonInvalido("Chave operação não encontrada");
    }

    private boolean validarChavesCadastro(){
       return jsonPedido.has("nome") &&
                jsonPedido.has("senha") &&
                jsonPedido.has("email");
    }

    private boolean validarChavesLogin(){
        return  jsonPedido.has("senha") &&
                jsonPedido.has("email");
    }

    private boolean validarChavesLogout(){
        return  jsonPedido.has("id") &&
                jsonPedido.has("token");
    }

    private boolean validarChavesAlteracao(){
        return jsonPedido.has("token") &&
                jsonPedido.has("senha") &&
                jsonPedido.has("email") &&
                jsonPedido.has("id") &&
                jsonPedido.has("nome");
    }

    private boolean validarChavesExclusao(){
        return jsonPedido.has("id") &&
                jsonPedido.has("senha") &&
                jsonPedido.has("token");
    }

    private boolean validarChavesIncidenteCadastro(){
        return jsonPedido.has("data") &&
                jsonPedido.has("hora") &&
                jsonPedido.has("estado") &&
                jsonPedido.has("cidade") &&
                jsonPedido.has("bairro") &&
                jsonPedido.has("rua") &&
                jsonPedido.has("tipo_incidente") &&
                jsonPedido.has("token") &&
                jsonPedido.has("id");
    }

    private boolean validarChavesListagem(){
        return jsonPedido.has("data") &&
                jsonPedido.has("estado") &&
                jsonPedido.has("cidade");
    }

    private boolean validarChavesListagemUsuario(){
        return jsonPedido.has("token") &&
                jsonPedido.has("id");
    }

    private boolean validarChavesIncidenteExclusao(){
        return jsonPedido.has("token") &&
                jsonPedido.has("id") &&
                jsonPedido.has("id_incidente");
    }
}
