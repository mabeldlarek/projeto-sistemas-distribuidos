package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.TransicaoTela;
import com.example.smaiccc_entrega_4.cliente.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MenuSistemaPadraoController <T>{
    @FXML
    private Button btCadastrar;
    @FXML
    private Button btLogar;
    @FXML
    private Button btListar;
    private int txtPorta;
    private String txtEndereco;

    @FXML
    private Pane root;
    @FXML
    private Pane paneConteudo;
    private TransicaoTela tt;
    private Cliente cliente;

    public MenuSistemaPadraoController(int txtPorta, String txtEndereco) {
        this.txtPorta = txtPorta;
        this.txtEndereco = txtEndereco;
    }

    public MenuSistemaPadraoController() {

    }

    @FXML
    protected void listarIncidentes(ActionEvent event) {
        IncidenteListagemController ic = new IncidenteListagemController(txtPorta, txtEndereco);
        chamarTela("/com/example/smaiccc_entrega_4/incidentes-listagem.fxml", (T) ic, "Lista de Incidentes");
    }

    @FXML
    protected void realizarLogin(ActionEvent event) {
        LoginController loginController = new LoginController (txtPorta, txtEndereco);
        chamarTela("/com/example/smaiccc_entrega_4/login.fxml", (T) loginController, "Login");
    }

    @FXML
    protected void realizarCadastro(ActionEvent event) {
        UsuarioCadastroController usuarioCadastroController = new UsuarioCadastroController(txtPorta, txtEndereco);
        chamarTela("/com/example/smaiccc_entrega_4/cadastro.fxml", (T) usuarioCadastroController, "Cadastro de Usuário");
    }

    private void chamarTela(String caminho, T controller, String nome){
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(caminho));
        fxmlloader.setController(controller);
        tt = new TransicaoTela(nome);
        tt.iniciarStage(fxmlloader, obterTelaAtual());
    }

    private Stage obterTelaAtual(){
        Stage st = (Stage) root.getScene().getWindow();
        return st;
    }

    public Pane getRoot() {
        return root;
    }
}
