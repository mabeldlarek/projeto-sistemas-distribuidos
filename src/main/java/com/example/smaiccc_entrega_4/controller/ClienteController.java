package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.TransicaoTela;
import com.example.smaiccc_entrega_4.cliente.Cliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloCliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClienteController <T> implements Initializable {
    @FXML
    private TextField txtPorta;
    @FXML
    private TextField txtEndereco;
    @FXML
    private Button btIniciar;
    @FXML
    private AnchorPane root;
    private TransicaoTela tt;
    private Cliente cliente;
    @FXML
    private Text txtErro;
    @FXML
    protected void enviar(ActionEvent event) {
      try {
            verificarConexao();
            MenuSistemaPadraoController menuPadrao = new MenuSistemaPadraoController(Integer.parseInt(txtPorta.getText()), txtEndereco.getText());
            chamarTela("/com/example/smaiccc_entrega_4/menusistemapadrao.fxml", (T) menuPadrao);
        }catch (ExcecaoObjetoNuloCliente e) {
            txtErro.setText("Não é possível estabelecer conexão com os dados informados. Verifique os dados e/ou servidor está ativo.");
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

    private void verificarConexao() throws ExcecaoObjetoNuloCliente {
            cliente = new Cliente(Integer.parseInt(txtPorta.getText()), txtEndereco.getText());
            cliente.iniciarCliente();
            cliente.encerrarSocket();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btIniciar.setDisable(true);

        txtPorta.textProperty().addListener((observable, oldValue, newValue) -> {
            String porta = txtPorta.getText();
            String endereco = txtEndereco.getText();

            if(!porta.isEmpty() && !endereco.isEmpty()) btIniciar.setDisable(false);
            if(!porta.matches("^[0-9]+$")) {
                btIniciar.setDisable(true);
                txtEndereco.setDisable(true);
            } else txtEndereco.setDisable(false);
        });

        txtEndereco.textProperty().addListener((observable, oldValue, newValue) -> {
            String porta = txtPorta.getText();
            String endereco = txtEndereco.getText();
            if(!porta.isEmpty() && !endereco.isEmpty()) btIniciar.setDisable(false);
        });
    }
}
