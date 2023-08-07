package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.TransicaoTela;
import com.example.smaiccc_entrega_4.alerts.Dialogs;
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

public class UsuarioCadastroController<T> implements Initializable {
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private TextField txtNome;
    @FXML
    private Button btCadastrar;
    @FXML
    private Text txtErroServer;
    @FXML
    private Pane root;
    private Cliente cliente;
    private String dadosGerados;
    private TransicaoTela tt;

    public UsuarioCadastroController(int numPorta, String endereco) {
        this.cliente = new Cliente(numPorta, endereco);
    }

    public UsuarioCadastroController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clickCadastrar(event);
            }
        });

    }
    protected void realizarCadastro() {
        String senha = new CriptografiaCesar(txtSenha.getText()).getSenhaCriptografada();
        setDadosGerados("{\"nome\":\""+ txtNome.getText() + "\",\"senha\":\""+ senha +"\",\"email\":\""+ txtEmail.getText() +"\",\"operacao\":" + 1 + "}");
        ValidaDadosUsuario validaDadosUsuario = new ValidaDadosUsuario(new JSONObject(getDadosGerados()));
        if(validaDadosUsuario.validaDadosCadastroUsuario())
            verificarRespostaServidor(iniciarComunicacaoServidor());
        else
            txtErroServer.setText(validaDadosUsuario.getErro());

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
            txtErroServer.setText("Erro objeto nulo");
            resposta = null;
        } catch (ExcecaoRespostaNulaServidor e) {
            txtErroServer.setText(e.getMessage());
            resposta = null;
        }
        catch (RuntimeException e) {
            txtErroServer.setText("Erro durante a execução");
            resposta = null;
        } catch (ExcecaoErroComunicacaoServidor e) {
            txtErroServer.setText(e.getMessage());
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
                txtErroServer.setText("Erro: resposta do servidor é inválida");
            return false;
        }
    }
    private void verificarRespostaServidor(JSONObject resposta){
        if(resposta!=null) {
            if (resposta.getString("status").equals("OK")) {
                MenuSistemaPadraoController ms = new MenuSistemaPadraoController();
                Dialogs dialogs = new Dialogs(1);
                dialogs.exibirInformacao();
            } else {
                txtErroServer.setText(resposta.getString("status"));
            }
        }
    }

    private Stage obterTelaAtual(){
        Stage st = (Stage) root.getScene().getWindow();
        return st;
    }

    public String getDadosGerados() {
        return dadosGerados;
    }

    public void setDadosGerados(String dadosGerados) {
        this.dadosGerados = dadosGerados;
    }

    @FXML
    protected void clickCadastrar(ActionEvent event) {
        realizarCadastro();
    }
}
