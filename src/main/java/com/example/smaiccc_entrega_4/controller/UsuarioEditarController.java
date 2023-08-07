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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UsuarioEditarController<T>implements Initializable {
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private TextField txtNome;
    @FXML
    private Button btEditar;
    @FXML
    private Text textErroServer;
    @FXML
    private Pane root;
    private Cliente cliente;
    private String dadosGerados;
    private TransicaoTela tt;
    private JSONObject autenticacaoLoginArmazenada;
    private int porta;
    private String endereco;

    public UsuarioEditarController(JSONObject autenticacaoLoginArmazenada, int numPorta, String endereco) {
        this.porta = numPorta;
        this.endereco = endereco;
        this.cliente = new Cliente(numPorta, endereco);
        this.autenticacaoLoginArmazenada = autenticacaoLoginArmazenada;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clickEditar(event);
            }
        });

    }
    protected void realizarEdicao() {
        String senha = new CriptografiaCesar(txtSenha.getText()).getSenhaCriptografada();
        setDadosGerados(
                "{\"token\":\""+ autenticacaoLoginArmazenada.getString("token") +
                "\",\"id\":"+ autenticacaoLoginArmazenada.getInt("id") +
                ",\"nome\":\""+ txtNome.getText() +
                "\",\"email\":\""+ txtEmail.getText() +
                "\",\"senha\":\"" + senha +
                "\",\"operacao\": 3}");
        ValidaDadosUsuario validaDadosUsuario = new ValidaDadosUsuario(new JSONObject(getDadosGerados()));
        if(validaDadosUsuario.validaDadosCadastroUsuario())
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
        if(resposta.equals(null))
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
        if(resposta.getString("status").equals("OK")){
            Dialogs dialogs = new Dialogs(3);
            dialogs.exibirInformacao();
            retornarTelaInicial("/com/example/smaiccc_entrega_4/menusistemapadrao.fxml");
        }
        else{
            textErroServer.setText(resposta.getString("status"));
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
    public String getDadosGerados() {
        return dadosGerados;
    }

    public void setDadosGerados(String dadosGerados) {
        this.dadosGerados = dadosGerados;
    }

    @FXML
    protected void clickEditar(ActionEvent event) {
        realizarEdicao();
    }
}

