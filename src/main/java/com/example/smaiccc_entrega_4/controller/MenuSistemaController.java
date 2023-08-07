package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.TransicaoTela;
import com.example.smaiccc_entrega_4.alerts.Dialogs;
import com.example.smaiccc_entrega_4.cliente.Cliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoErroComunicacaoServidor;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloCliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoRespostaNulaServidor;
import com.example.smaiccc_entrega_4.validacao.ValidaRespostaServidor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuSistemaController <T> implements Initializable {
    @FXML
    private Button btSair;

    @FXML
    private Pane root;

    @FXML
    private Pane paneConteudo;

    @FXML
    private Text textErroServer;

    private JSONObject autenticacaoLoginArmazenada;
    private Cliente cliente;

    private String dadosGerados;

    private TransicaoTela tt;
    private String endereco;
    private int porta;

    @FXML
    protected void realizarLogout(ActionEvent event) {
        setDadosGerados("{\"operacao\": 9,\"id\":"+ autenticacaoLoginArmazenada.getInt("id") + ",\"token\":" +"\"" + autenticacaoLoginArmazenada.getString("token") + "\"}");
        verificarRespostaServidor(iniciarComunicacaoServidor());
    }

    @FXML
    protected void reportarIncidente(ActionEvent event) {
        IncidenteCadastroController ic = new IncidenteCadastroController(autenticacaoLoginArmazenada, porta, endereco);
        mudarConteudo("/com/example/smaiccc_entrega_4/cadastro-incidentes.fxml", (T) ic);
    }

    @FXML
    protected void listarIncidente(ActionEvent event) {
        IncidenteListagemController ic = new IncidenteListagemController(porta, endereco);
        mudarConteudo("/com/example/smaiccc_entrega_4/incidentes-listagem.fxml", (T) ic);
    }

    @FXML
    protected void listarIncidenteUsuario(ActionEvent event) {
        IncidenteUsuarioListagemController ic = new IncidenteUsuarioListagemController(porta, endereco, autenticacaoLoginArmazenada);
        mudarConteudo("/com/example/smaiccc_entrega_4/incidentes-usuario-listagem.fxml", (T) ic);
    }

    @FXML
    protected void atualizarDados(ActionEvent event) {
        UsuarioEditarController ic = new UsuarioEditarController(autenticacaoLoginArmazenada, porta, endereco);
        mudarConteudo("/com/example/smaiccc_entrega_4/cadastro-atualizar.fxml", (T) ic);
    }

    @FXML
    protected void excluirConta(ActionEvent event) {
        UsuarioExcluirController ic = new UsuarioExcluirController(autenticacaoLoginArmazenada, porta, endereco);
        mudarConteudo("/com/example/smaiccc_entrega_4/cadastro-excluir.fxml", (T) ic);
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
            if(resposta.getInt("operacao") == 9) {
                Dialogs dialogs = new Dialogs(3);
                dialogs.exibirInformacao();
                MenuSistemaPadraoController ms = new MenuSistemaPadraoController(porta, endereco);
                retornarTelaInicial("/com/example/smaiccc_entrega_4/menusistemapadrao.fxml", (T) ms);
            }
        }
        else{
            textErroServer.setText(resposta.getString("status"));
        }
    }

    private void chamarTela(String caminho, T controller){ //recebe o caminho e o controller a ser enviado para "transicao de tela
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(caminho));
        fxmlloader.setController(controller);
        tt = new TransicaoTela();
        tt.iniciarStage(fxmlloader, obterTelaAtual());
    }

    private void retornarTelaInicial(String caminho, T controller){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            loader.setController(controller);
            Pane paneOriginal = loader.load();
            root.getChildren().add(paneOriginal);
        } catch (IOException e) {
            System.out.println("Erro na transição de tela");
        }
    }

    private void mudarConteudo(String caminho, T controller){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            loader.setController(controller);
            Pane paneOriginal = loader.load();
            paneConteudo.getChildren().add(paneOriginal);
        } catch (IOException e) {
            System.out.println("Erro na transição de tela");
        }
    }

    private Stage obterTelaAtual(){
        Stage st = (Stage) root.getScene().getWindow();
        return st;
    }
    public MenuSistemaController(JSONObject autenticacaoLoginArmazenada, int numPorta, String ip) {
        this.porta = numPorta;
        this.endereco = ip;
        this.autenticacaoLoginArmazenada = autenticacaoLoginArmazenada;
        this.cliente = new Cliente(numPorta, ip);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void setDadosGerados(String dadosGerados) {
        this.dadosGerados = dadosGerados;
    }

    public String getDadosGerados() {
        return dadosGerados;
    }
}
