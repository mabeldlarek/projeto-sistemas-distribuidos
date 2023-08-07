package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.ServerManager;
import com.example.smaiccc_entrega_4.autenticacao.TokenServidor;
import com.example.smaiccc_entrega_4.dao.UsuarioDAO;
import com.example.smaiccc_entrega_4.model.Usuario;
import com.example.smaiccc_entrega_4.servidor.Servidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ServidorController implements Initializable {
    @FXML
    private TextField txtPorta;
    @FXML
    private Button btIniciar;
    @FXML
    private Button btEncerrar;
    @FXML
    private Text txtIsAtivo;

    @FXML
    private TableColumn<Usuario, String> colId;
    @FXML
    private TableColumn<Usuario, String> colIp;
    @FXML
    private TableView<Usuario> tabelaSessao;
    private Servidor servidor;
    private Task<Void> serverTask;
    private Thread serverThread;
    private TextArea txtConsole;
    ServerManager sm;

    private ObservableList<Usuario> listaClientes;
    private List<Usuario> clientes;

    @FXML
    protected void iniciarServidor(ActionEvent event) {
        serverTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                servidor = new Servidor(Integer.parseInt(txtPorta.getText()));
                ServerManager sm = new ServerManager();
                Thread threadServidor = new Thread(servidor);
                threadServidor.start();
                updateProgress(50, 100);
                return null;
            }
        };

        serverTask.setOnRunning(e -> {
            btEncerrar.setDisable(false);
        });

        serverTask.setOnSucceeded(e -> {
            sm = new ServerManager();
            txtIsAtivo.setText("Ativo");
        });

        serverThread = new Thread(serverTask);
        serverThread.start();
    }

    @FXML
    protected void encerrarServidor(ActionEvent event) {
        ServerManager sm = new ServerManager();
        if(!txtPorta.getText().isEmpty()) {
            sm.stopServer(Integer.parseInt(txtPorta.getText()));
            btEncerrar.setDisable(true);
            txtIsAtivo.setText("Não ativo");
            tabelaSessao.getItems().clear();
        }
    }

    private void inicializaTabela(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIp.setCellValueFactory(new PropertyValueFactory<>("nome"));
        preencherTabela();
    }

    @FXML
    public void atualizarTabela() {
        tabelaSessao.getItems().clear();
        preencherTabela();
    }

    private void preencherTabela(){
        TokenServidor tk = new TokenServidor();
        UsuarioDAO usuarioDao = new UsuarioDAO();
        tk.getTokens().forEach((key, value) -> {
            clientes.add(usuarioDao.getUsuarioPorId(String.valueOf(key)));
        });
        listaClientes = FXCollections.observableList(clientes);
        tabelaSessao.setItems(listaClientes);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btIniciar.setDisable(true);
        btEncerrar.setDisable(true);
        clientes = new ArrayList<>();
        inicializaTabela();
        txtPorta.textProperty().addListener((observable, oldValue, newValue) -> {
            String porta = txtPorta.getText();
            if(!porta.isEmpty())
                btIniciar.setDisable(false);
            if(!porta.matches("^[0-9]+$")) btIniciar.setDisable(true);
        });
    }

}
