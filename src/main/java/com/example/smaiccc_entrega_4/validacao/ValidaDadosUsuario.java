package com.example.smaiccc_entrega_4.validacao;


import com.example.smaiccc_entrega_4.autenticacao.CriptografiaCesar;
import com.example.smaiccc_entrega_4.autenticacao.TokenServidor;
import com.example.smaiccc_entrega_4.dao.UsuarioDAO;
import com.example.smaiccc_entrega_4.model.Usuario;
import org.json.JSONObject;

public class ValidaDadosUsuario {
    private String operacao;
    private String erro;
    private UsuarioDAO usuarioDAO;
    private JSONObject obj;

    public ValidaDadosUsuario(JSONObject obj) {
        this.obj = obj;
        operacao = String.valueOf(obj.getInt("operacao"));
        erro = "SErro";
    }

    public ValidaDadosUsuario() {

    }

    public boolean validarNome(){
        if(obj.getString("nome").length() < 50 && obj.getString("nome").length() > 3 ){
            return true;
        }
        setErro("Nome inválido");
        return false;
    }

    public boolean validarEmail(){
        if(obj.getString("email").matches("^[A-Za-z0-9._%+-]{3,50}@[A-Za-z0-9.-]{3,10}$")){
            return true;
        }
        setErro("E-mail inválido");
        return false;
    }

    public boolean validarSenha(){
        CriptografiaCesar cc = new CriptografiaCesar(obj.getString("senha"));
        String senha = cc.obterSenhaDescriptografada();
        if(senha.matches("^[a-zA-Z0-9]{5,10}+$")){
            return true;
        }
        setErro("Senha inválida");
        return false;
    }

    public int verificarEmailExistente(){
        usuarioDAO = new UsuarioDAO();
        int i = usuarioDAO.getUsuarioPorEmail(obj.getString("email"));
        if(i == 1){
            setErro("E-mail já existe");
        }
        return i;
    }

    public int verificarEmailExistenteEdicao(){
        usuarioDAO = new UsuarioDAO();
        int i = usuarioDAO.getUsuarioPorEmailEdicao(obj.getString("email"), obj.getInt("id"));
        if(i == 1){
            setErro("E-mail já existe");
        }
        return i;
    }

    public int verificarSenhaUsuario(){
        usuarioDAO = new UsuarioDAO();
        int i = usuarioDAO.getSenhaPorId(obj.getString("senha"), obj.getInt("id"));
        if(i == 1){
            setErro("Senha incorreta");
        }
        return i;
    }


    public Usuario verificarCadastroExistente() {
        Usuario usuario = null;
        if (validarEmail() && validarSenha()) {
            usuarioDAO = new UsuarioDAO();
            usuario = usuarioDAO.getUsuarioPorEmailSenha(obj.getString("email"), obj.getString("senha"));
            return usuario;
        }

        return usuario;
    }

    public boolean verificarLoginAtivo() {
        TokenServidor tk = new TokenServidor();
        if(tk.getToken(String.valueOf(obj.getInt("id"))) != null ||
                tk.getToken(String.valueOf(obj.getInt("id"))) != "")
            return true;
        else {
            setErro("Falha na autenticação do usuário");
            return false;
        }
    }

    public boolean validaDadosCadastroUsuario(){
        if(validarNome() && validarSenha() && validarEmail()) return true;
        else return false;
    }

    public boolean validaDadosLoginUsuario(){
        if(validarSenha() && validarEmail()) return true;
        else return false;
    }

    public String getErro() {
        return erro;
    }

    private void setErro(String erro) {
        this.erro = erro;
    }
}
