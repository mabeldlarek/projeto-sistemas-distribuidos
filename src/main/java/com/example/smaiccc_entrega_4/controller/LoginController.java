package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.TransicaoTela;
import com.example.smaiccc_entrega_4.autenticacao.CriptografiaCesar;
import com.example.smaiccc_entrega_4.cliente.Cliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoErroComunicacaoServidor;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloCliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoRespostaNulaServidor;
import com.example.smaiccc_entrega_4.validacao.ValidaDadosUsuario;
import com.example.smaiccc_entrega_4.validacao.ValidaRespostaServidor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;


import java.net.URL;
import java.util.ResourceBundle;

public class LoginController<T> implements Initializable {
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Button btLogar;
    @FXML
    private Text textErroServer;
    @FXML
    private Pane root;
    private Cliente cliente;
    private String dadosGerados;
    private TransicaoTela tt;
    private int numPorta;
    private String ip;

    public LoginController(int numPorta, String ip) {
        this.numPorta = numPorta;
        this.ip = ip;
        this.cliente = new Cliente(numPorta, ip);
    }

    public String getDadosGerados() {
        return dadosGerados;
    }

    private void setDadosGerados(String dadosGerados) {
        this.dadosGerados = dadosGerados;
    }

    private void realizarLogin(){
        String senha = new CriptografiaCesar(txtSenha.getText()).getSenhaCriptografada();
        setDadosGerados("{\"operacao\": 2,\"senha\":"+"\""+ senha + "\",\"email\":" +"\"" + txtEmail.getText() + "\"}");
        ValidaDadosUsuario validaDadosUsuario = new ValidaDadosUsuario(new JSONObject(getDadosGerados()));
        if(validaDadosUsuario.validaDadosLoginUsuario())
            verificarRespostaServidor(iniciarComunicacaoServidor());
        else
            textErroServer.setText(validaDadosUsuario.getErro());
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
            textErroServer.setText("Erro objeto nulo");
            resposta = null;
        } catch (ExcecaoRespostaNulaServidor e) {
            textErroServer.setText(e.getMessage());
            resposta = null;
        }
        catch (RuntimeException e) {
            textErroServer.setText("Erro durante a execução");
            resposta = null;
        } catch (ExcecaoErroComunicacaoServidor e) {
            textErroServer.setText(e.getMessage());
            resposta = null;
        }
        return resposta;
    }

    private boolean respostaIsValida(JSONObject resposta){
        if(resposta == (null))
            return false;
        else {
            ValidaRespostaServidor validaRespostaServidor = new ValidaRespostaServidor(resposta.toString());
            if (validaRespostaServidor.getErro().equals("SErro")) {
                return true;
            } else
                textErroServer.setText("Erro: resposta do servidor é inválida");
            return false;
        }
    }

    private void verificarRespostaServidor(JSONObject resposta){
        if(resposta != null) {
            if (resposta.getString("status").equals("OK")) {
                obterTelaAtual().getOwner().hide();
                MenuSistemaController ms = new MenuSistemaController(resposta, numPorta, ip);
                chamarTela("/com/example/smaiccc_entrega_4/menusistema.fxml", (T) ms);
                obterTelaAtual().close();
            }
            else{
                textErroServer.setText(resposta.getString("status"));
            }
        }

    }

    private void chamarTela(String caminho, T controller){ //recebe o caminho e o controller a ser enviado para "transicao de tela
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(caminho));
        fxmlloader.setController(controller);
        tt = new TransicaoTela();
        tt.iniciarStage(fxmlloader, obterTelaAtual()); // da pra passar dados de uma tela pra outra (controller) atraves do construtor dela
    }

    private Stage obterTelaAtual(){
        Stage st = (Stage) root.getScene().getWindow();
        return st;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btLogar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                realizarLogin();
            }
        });

    }

    @FXML
    protected void clickLogar(ActionEvent event) {

    }
}
