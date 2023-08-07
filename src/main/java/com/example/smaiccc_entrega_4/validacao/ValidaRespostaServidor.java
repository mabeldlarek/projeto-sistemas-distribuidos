package com.example.smaiccc_entrega_4.validacao;

import org.json.JSONArray;
import org.json.JSONObject;

public class ValidaRespostaServidor {

    private JSONObject jsonResposta;
    private String erro;

    public ValidaRespostaServidor(String resposta) {
        jsonResposta = new JSONObject(resposta);
        this.erro = "SErro";
        verificaRespostaOperacao();
    }

    private void verificaRespostaOperacao(){
       boolean isValido = false;
       isValido = verificaRespostaStatusOperacao();
       int operacao = 0;
       if(isValido) {
           operacao = jsonResposta.getInt("operacao");
           if (operacao == 1 || operacao == 3 || operacao == 6 || operacao == 7 || operacao == 8 || operacao == 9) {
               isValido = verificaRespostaStatusOperacao();
           }
           else if (operacao == 2) {
               isValido = verificaRespostaLogin();
           }
           else if (operacao == 4 || operacao == 5) {
               isValido = verificaRespostaListagem();
           }
           else setErro("Operação inválida");
       }

    }

    private boolean verificaRespostaStatusOperacao(){
        if(jsonResposta.isNull("status") || jsonResposta.getString("status").isEmpty() ||
                jsonResposta.isNull("operacao")) {
            setErro("Resposta do servidor é inválida. Erro em: status/operação");
            return false;
        }
        return true;
    }

    private boolean verificaRespostaListagem() {
       if(jsonResposta.getString("status").equals("OK")) {
           if (jsonResposta.isNull("incidentes")) {
               setErro("Resposta do servidor é inválida. Erro em: lista de incidentes");
               return false;
           }
           if (jsonResposta.getJSONArray("incidentes").isEmpty())
               return true;
           else return verificarListaIncidentes();
       }
       return true;
    }

    private boolean verificarListaIncidentes(){
        return carregarJson();
    }

    private boolean carregarJson() {
        JSONArray incidentesArray = jsonResposta.getJSONArray("incidentes");
        boolean isValida = false;
            for (int i = 0; i < incidentesArray.length(); i++) {
                JSONObject jsonIncidente = incidentesArray.getJSONObject(i);
                ValidaDadosIncidente validaDadosIncidente = new ValidaDadosIncidente();
                validaDadosIncidente.setObj(jsonIncidente);
                if(validaDadosIncidente.validarData()
                        && validaDadosIncidente.validarHora() &&
                validaDadosIncidente.validarEstado() &&
                        validaDadosIncidente.validarCidade()
                        && validaDadosIncidente.validarRua()
                && validaDadosIncidente.validarBairro()
                && !jsonIncidente.isNull("tipo_incidente")
                && !jsonIncidente.isNull("id_incidente")) {
                    if(validaDadosIncidente.validarIncidente())
                        isValida = true;
                    else {
                        setErro("Resposta do servidor é inválida. Erro em: lista de incidentes");
                        break;
                    }
                }
                else {
                    setErro("Resposta do servidor é inválida. Erro em: lista de incidentes");
                    isValida = false;
                    break;
                }
            }
            return isValida;
    }

    private boolean verificaRespostaLogin(){
        ValidaDadosUsuario validaDadosUsuario = new ValidaDadosUsuario(jsonResposta);
        if(jsonResposta.getString("status").equals("OK")) {
            if (jsonResposta.isNull("token") || jsonResposta.getString("token").isEmpty() ||
                    !validaDadosUsuario.validarNome() ||
                    jsonResposta.isNull("id")) {
                    setErro("Resposta do servidor é inválida. Erro em: dados do usuário");
                    return false;
            }
        }
       return true;
    }

    public JSONObject getJsonResposta() {
        return jsonResposta;
    }

    public void setJsonResposta(JSONObject jsonResposta) {
        this.jsonResposta = jsonResposta;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
}
