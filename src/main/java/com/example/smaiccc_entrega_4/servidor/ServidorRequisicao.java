package com.example.smaiccc_entrega_4.servidor;

import com.example.smaiccc_entrega_4.Login;
import com.example.smaiccc_entrega_4.autenticacao.TokenServidor;
import com.example.smaiccc_entrega_4.dao.IncidenteDAO;
import com.example.smaiccc_entrega_4.dao.UsuarioDAO;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoJsonInvalido;
import com.example.smaiccc_entrega_4.validacao.ValidaDadosIncidente;
import com.example.smaiccc_entrega_4.validacao.ValidaDadosUsuario;
import com.example.smaiccc_entrega_4.validacao.ValidaPedidoCliente;
import org.json.JSONObject;


public class ServidorRequisicao {
    private ValidaDadosUsuario validaDadosUsuario;
    private ValidaDadosIncidente validacao;
    private String resposta;
    private JSONObject obj;

    public ServidorRequisicao(String json) throws ExcecaoJsonInvalido {
        ValidaPedidoCliente validaPedidoCliente = new ValidaPedidoCliente(json);
        obj = new JSONObject(json);
    }

    public ServidorRequisicao() {

    }

    public JSONObject getRespostaJSON(){
        JSONObject resposta = realizarConsultaOperacaoJSON();
        if(resposta == null){
            return new JSONObject("{\"operacao\":" + obj.getInt("operacao") + ", \"status\": \"Erro: resposta nula\"}");
        }
        return resposta;
    }

    private JSONObject realizarConsultaOperacaoJSON() {
        String s ="";
        switch (obj.getInt("operacao")) {
            case 1 -> {
                s = realizarCadastro();
            }
            case 2 -> {
                s= realizarLogin();
            }
            case 3 -> {
                s= alterarCadastroUsuario();
            }
            case 4 -> {
                s= listarIncidente();
            }
            case 5-> {
                s= listarIncidenteUsuario();
            }
            case 6-> {
                s= removerIncidenteUsuario();
            }
            case 8 -> {
                s = removerCadastroUsuario();
            }
            case 7 -> {
                s = reportarIncidente();
            }
            case 9 -> {
                s = realizarLogout();
            }
        }
        return new JSONObject(s);
    }

    public String realizarCadastro(){
        validaDadosUsuario = new ValidaDadosUsuario(obj);
        if(validaDadosUsuario.validarNome() && validaDadosUsuario.validarEmail() &&
                validaDadosUsuario.validarSenha()) {
            if (0 == validaDadosUsuario.verificarEmailExistente()) {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuarioDAO.inserir(obj);
                if(usuarioDAO.getErroBanco()=="")
                    return "{\"operacao\": 1, \"status\": \"OK\"}";
                else
                    return "{\"operacao\": 1, \"status\": \"Erro\"}";
            }
        }
        return "{\"operacao\": 1, \"status\": \"" + validaDadosUsuario.getErro() + "\"}";
    }

    public String alterarCadastroUsuario() {
        validaDadosUsuario = new ValidaDadosUsuario(obj);
        if (validaDadosUsuario.verificarLoginAtivo()) {
            if (validaDadosUsuario.validaDadosCadastroUsuario()) {
                if (0 == validaDadosUsuario.verificarEmailExistenteEdicao()) {
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    usuarioDAO.editar(obj);
                    if(usuarioDAO.getErroBanco()=="")
                        return "{\"operacao\": 3, \"status\": \"OK\"}";
                    else
                        return "{\"operacao\": 3, \"status\": \"Erro\"}";
                }
            }
        }
        return "{\"operacao\": 3, \"status\": \"" + validaDadosUsuario.getErro() + "\"}";
    }

    public String listarIncidente(){
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(obj);
        if(validacao.validarData() && validacao.validarCidade() && validacao.validarEstado()){
            IncidenteDAO incidenteDAO = new IncidenteDAO();
            JSONObject jsonBanco = incidenteDAO.consultarListaIncidentes(obj);
            if(jsonBanco!=null)
                return  jsonBanco.toString();
        }
        return "{\"operacao\": 4, \"status\": \"" + validacao.getErro()+ "\"}";
    }

    public String listarIncidenteUsuario(){
        validaDadosUsuario = new ValidaDadosUsuario(obj);
        if (validaDadosUsuario.verificarLoginAtivo()) {
            IncidenteDAO incidenteDAO = new IncidenteDAO();
            JSONObject jsonBanco = incidenteDAO.consultarListaIncidentesUsuario(obj);
            if(jsonBanco!=null)
                return  jsonBanco.toString();
        }
        return "{\"operacao\": 5, \"status\": \"" + validacao.getErro()+ "\"}";
    }

    public String removerIncidenteUsuario(){
        validaDadosUsuario = new ValidaDadosUsuario(obj);
        if (validaDadosUsuario.verificarLoginAtivo()) {
            IncidenteDAO incidenteDAO = new IncidenteDAO();
            incidenteDAO.removerIncidente(obj);
            if(incidenteDAO.getErro() == null)
                return "{\"operacao\": 6, \"status\": \"OK\"}";
            else
                return "{\"operacao\": 6, \"status\": \"Erro\"}";
        }
        return "{\"operacao\": 6, \"status\": \"" + validaDadosUsuario.getErro() + "\"}";
    }

    public String reportarIncidente(){
        validacao = new ValidaDadosIncidente(obj);
        if(validacao.validaDadosCadastroIncidente()) {
            if(validacao.verificarLoginAtivo()) {
                IncidenteDAO incidenteDAO = new IncidenteDAO();
                incidenteDAO.inserir(obj);
                return "{\"operacao\": 7, \"status\": \"OK\"}";
            }
        }

        return "{\"operacao\": 7, \"status\": \"" + validacao.getErro() + "\"}";
    }

    public String removerCadastroUsuario(){
        TokenServidor tokenServidor = new TokenServidor();
        validaDadosUsuario = new ValidaDadosUsuario(obj);
        if (validaDadosUsuario.verificarLoginAtivo()) {
            System.out.println("teste");
            if (1 == validaDadosUsuario.verificarSenhaUsuario()) {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuarioDAO.remover(obj);
                System.out.println("erro banco:" + usuarioDAO.getErroBanco());
                if (usuarioDAO.getErroBanco() == "") {
                    tokenServidor.encerrarToken(obj.getInt("id"));
                    return "{\"operacao\": 8, \"status\": \"OK\"}";
                }
                else
                    return "{\"operacao\": 8, \"status\": \"Erro\"}";
            }
        }
        return "{\"operacao\": 8, \"status\": \"" + validaDadosUsuario.getErro() + "\"}";
}

    public String realizarLogin(){
        Login login = new Login(obj);
        String resposta = login.validarLogin();
        return resposta;
    }

    public String realizarLogout(){
        Login login = new Login(obj);
        return login.realizaLogout();
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }
}
