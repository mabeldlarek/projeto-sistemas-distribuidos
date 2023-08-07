package com.example.smaiccc_entrega_4;

import com.example.smaiccc_entrega_4.autenticacao.TokenServidor;
import com.example.smaiccc_entrega_4.model.Usuario;
import com.example.smaiccc_entrega_4.validacao.ValidaDadosUsuario;
import org.json.JSONObject;

public class Login {

    private  JSONObject objLogin;
    private final String operacao = "Login";
    private Usuario usuario = null;
    private TokenServidor tokenServidor = null;

    public Login(JSONObject objLogin) {
        this.objLogin = objLogin;
        tokenServidor = new TokenServidor();
    }

    public String validarLogin(){
        ValidaDadosUsuario validaDadosUsuario = new ValidaDadosUsuario(objLogin);
        usuario = validaDadosUsuario.verificarCadastroExistente();
        String resposta = gerarRespostaLogin(this.usuario);
        return resposta;
    }

    private String gerarRespostaLogin(Usuario usuario){
        String resposta = "";
        if (usuario == (null)) {
           resposta = "{\"operacao\": 2, \"status\": \"Erro ao realizar " + operacao + "\"}";
        } else {
            tokenServidor.incluirTokenNovoCliente(usuario.getId());
            resposta = "{\"operacao\": 2,\"status\":\"OK\",\"token\":\""+ tokenServidor.getToken(usuario.getId())+ "\",\"nome\":\"" + usuario.getNome()+ "\",\"id\":" + usuario.getId() + "}";
        }
        return resposta;
    }

    public String realizaLogout(){
        ValidaDadosUsuario validaDadosUsuario = new ValidaDadosUsuario(objLogin);
        boolean loginAtivo = validaDadosUsuario.verificarLoginAtivo();

        if(loginAtivo){
            tokenServidor.encerrarToken(objLogin.getInt("id"));
        }
        return gerarRespostaLogout(loginAtivo);
    }

    private String gerarRespostaLogout(boolean loginAtivo){
        String resposta = "";
        if (!loginAtivo) {
            resposta = "{\"operacao\": 9, \"status\": \"Erro ao realizar " + "Logout" + "\"}";
        } else {
            resposta = "{\"operacao\": 9,\"status\":\"OK\"}";
        }
        return resposta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

}
