package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.alerts.Dialogs;
import com.example.smaiccc_entrega_4.autenticacao.CriptografiaCesar;
import com.example.smaiccc_entrega_4.cliente.Cliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoErroComunicacaoServidor;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloCliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoRespostaNulaServidor;
import com.example.smaiccc_entrega_4.validacao.ValidaDadosUsuario;
import com.example.smaiccc_entrega_4.validacao.ValidaRespostaServidor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.IOException;

public class UsuarioExcluirController {
    @FXML
    private TextField txtSenha;
    @FXML
    private Button btExcluir;
    @FXML
    private Text txtErro;
    @FXML
    private Pane root;
    private JSONObject autenticacaoLoginArmazenada;
    private Cliente cliente;
    private String dadosGerados;
    private int porta;
    private String endereco;

    public UsuarioExcluirController(JSONObject autenticacaoLoginArmazenada, int porta, String endereco) {
        this.porta = porta;
        this.endereco = endereco;
        this.cliente = new Cliente(porta, endereco);
        this.autenticacaoLoginArmazenada = autenticacaoLoginArmazenada;
    }

    @FXML
    protected void excluirCadastro(ActionEvent event) {
        Dialogs dialogs = new Dialogs(8);
        String senha = new CriptografiaCesar(txtSenha.getText()).getSenhaCriptografada();
        if(txtSenha.getText().length() > 0) {
            if (dialogs.exibirConfirmacao()) {
                setDadosGerados("{\"operacao\": 8,\"id\":" + autenticacaoLoginArmazenada.getInt("id") + ",\"senha\": \"" + senha + "\",\"token\":" + "\"" + autenticacaoLoginArmazenada.getString("token") + "\"}");
                ValidaDadosUsuario validaDadosUsuario = new ValidaDadosUsuario(new JSONObject(getDadosGerados()));
                if (validaDadosUsuario.validarSenha())
                    verificarRespostaServidor(iniciarComunicacaoServidor());
                else txtErro.setText(validaDadosUsuario.getErro());
            }
        } else txtErro.setText("Senha deve ser informada");

    }

    private JSONObject iniciarComunicacaoServidor(){
        JSONObject resposta = new JSONObject();
        try{
            cliente.iniciarCliente();
            JSONObject jsonListagem = new JSONObject(getDadosGerados());
            resposta = cliente.enviarDadosServidor(jsonListagem);
            System.out.println("ENVIADO: " + jsonListagem.toString());
            cliente.encerrarSocket();
            if(respostaIsValida(resposta)){
                System.out.println("RECEBIDO: " + resposta.toString());
            }
        } catch (ExcecaoObjetoNuloCliente e) {
            txtErro.setText("Erro objeto nulo");
            resposta = null;
        } catch (ExcecaoRespostaNulaServidor e) {
            txtErro.setText(e.getMessage());
            resposta = null;
        }
        catch (RuntimeException e) {
            txtErro.setText("Erro durante a execução");
            resposta = null;
        } catch (ExcecaoErroComunicacaoServidor e) {
            txtErro.setText(e.getMessage());
            resposta = null;
        }
        return resposta;
    }

    private boolean respostaIsValida(JSONObject resposta){
        if(resposta.equals(null))
            return false;
        else {
            ValidaRespostaServidor validaRespostaServidor = new ValidaRespostaServidor(resposta.toString());
            if (validaRespostaServidor.getErro().equals("SErro")) {
                return true;
            } else
                txtErro.setText("Erro: resposta do servidor é inválida");
            return false;
        }
    }
    private void verificarRespostaServidor(JSONObject resposta){
        if(resposta.getString("status").equals("OK")){
            Dialogs dialogs = new Dialogs(8);
            dialogs.exibirInformacao();
            retornarTelaInicial("/com/example/smaiccc_entrega_4/menusistemapadrao.fxml");
        }
        else{
            txtErro.setText(resposta.getString("status"));
        }
    }

    private void retornarTelaInicial(String caminho)  {
        MenuSistemaPadraoController ms = new MenuSistemaPadraoController(porta, endereco);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
        loader.setController(ms);
        try {
            Pane paneOriginal = loader.load();
            Pane paneTelaPai = (Pane) root.getScene().getRoot().lookup("#root");
            if (paneTelaPai != null) {
                paneTelaPai.getChildren().clear();
                paneTelaPai.getChildren().add(paneOriginal);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setDadosGerados(String dadosGerados) {
        this.dadosGerados = dadosGerados;
    }

    public String getDadosGerados() {
        return dadosGerados;
    }
}
