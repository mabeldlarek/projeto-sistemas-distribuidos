package com.example.smaiccc_entrega_4.validacao;

import com.example.smaiccc_entrega_4.autenticacao.TokenServidor;
import org.json.JSONObject;

import java.time.LocalDate;

public class ValidaDadosIncidente {
    private String operacao;
    private String erro;
    private JSONObject obj;

    public ValidaDadosIncidente(JSONObject obj) {
        this.obj = obj;
        operacao = String.valueOf(obj.getInt("operacao"));
        this.erro = "SErro";
    }

    public ValidaDadosIncidente() {

    }

    public boolean validarData(){
        if(!obj.get("data").equals(null)) {
            if (obj.getString("data").matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
                if(verificarDiaDoMes(obj.getString("data")))
                return true;
            }
        }
        setErro("Data inválida");
        return false;
    }

    public boolean validarHora(){
        if(obj.getString("hora").matches("^(?:[01]\\d|2[0-3]):[0-5]\\d$")){
            return true;
        }
        setErro("Hora inválida");
        return false;
    }

    public boolean validarEstado(){
        if(obj.getString("estado").trim().matches("^[A-Z]{2}$")){
            return true;
        }
        setErro("Estado inválido");
        return false;
    }

    public boolean validarCidade(){
        if(obj.getString("cidade").trim().matches("^[A-Z'\\s]{1,50}$")){
            return true;
        }
        setErro("Cidade inválida");
        return false;
    }

    public boolean validarBairro(){
        if(obj.getString("bairro").trim().matches("^[A-Z'\\s]{1,50}$")){
            return true;
        }
        setErro("Bairro inválido");
        return false;
    }

    public boolean validarRua(){
        if(obj.getString("rua").trim().matches("^[A-Z0-9'\\s]{1,50}$")){
            return true;
        }
        setErro("Rua inválida");
        return false;
    }

    public boolean validarIncidente(){
        if(!obj.isEmpty() && !obj.isNull("tipo_incidente")) {
            if (String.valueOf(obj.getInt("tipo_incidente")).matches("^[0-9]|10$")) {
                return true;
            }
        }
        setErro("Tipo de Incidente inválido");
        return false;
    }

    public boolean validaDadosCadastroIncidente(){
        if(validarData() && validarHora() && validarEstado() && validarCidade() &&
        validarRua() && validarBairro() && validarIncidente()) return true;
        else return false;
    }

    public boolean validaDadosListagemIncidente(){
        if(validarData() && validarEstado() && validarCidade()) return true;
        else return false;
    }

    public boolean verificarLoginAtivo() {
        TokenServidor tk = new TokenServidor();
        if(tk.getToken(String.valueOf(obj.getInt("id"))) != null ||
                tk.getToken(String.valueOf(obj.getInt("id"))) != "")
            return true;
        else
            return false;
    }


    public String getErro() {
        return erro;
    }

    private void setErro(String erro) {
        this.erro = erro;
    }

    private boolean verificarDiaDoMes(String data) {
        LocalDate localDate = LocalDate.parse(data);

        int dia = localDate.getDayOfMonth();
        int mes = localDate.getMonthValue();
        int ano = localDate.getYear();

        int diasNoMes;
        switch (mes) {
            case 2:
                diasNoMes = verificarAnoBissexto(ano);
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                diasNoMes = 30;
                break;
            default:
                diasNoMes = 31;
        }
        return dia >= 1 && dia <= diasNoMes;
    }

    private int verificarAnoBissexto(int ano){
        if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0)) {
            return 29;
        } else {
            return 28;
        }
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }
}
