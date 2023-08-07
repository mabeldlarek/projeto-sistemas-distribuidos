package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.alerts.Dialogs;
import com.example.smaiccc_entrega_4.cliente.Cliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoErroComunicacaoServidor;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloCliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoRespostaNulaServidor;
import com.example.smaiccc_entrega_4.model.Incidente;
import com.example.smaiccc_entrega_4.validacao.ValidaDadosIncidente;
import com.example.smaiccc_entrega_4.validacao.ValidaRespostaServidor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class IncidenteUsuarioListagemController implements Initializable {
    @FXML
    private Pane root;
    @FXML
    private Text textErroServer;
    @FXML
    private Button btListar;
    @FXML
    private Button btExcluir;
    @FXML
    private TableColumn<Incidente, String> colData;
    @FXML
    private TableColumn<Incidente, String> colHora;
    @FXML
    private TableColumn<Incidente, String> colEstado;
    @FXML
    private TableColumn<Incidente, String> colCidade;
    @FXML
    private TableColumn<Incidente, String> colRua;
    @FXML
    private TableColumn<Incidente, String> colBairro;
    @FXML
    private TableColumn<Incidente, String> colTipo;
    @FXML
    private TableColumn<Incidente, Integer> colId;
    @FXML
    private TableView<Incidente> table;
    private ObservableList<Incidente> listaIncidentes;
    private List<Incidente> incidentes;
    private String dadosGerados;
    private Cliente cliente;
    private JSONObject autenticacaoLoginArmazenada;
    private int idIncidenteSelecionado;

    public IncidenteUsuarioListagemController(int porta, String endereco) {
        this.cliente = new Cliente(porta, endereco);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        incidentes = new ArrayList<>();
        btExcluir.setDisable(true);

        btListar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                table.getItems().clear();
                listarIncidentes();
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idIncidenteSelecionado = newSelection.getId_incidente();
                btExcluir.setDisable(false);
            }
        });

        btExcluir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialogs dialogs = new Dialogs(6);
                if(dialogs.exibirConfirmacao())
                    excluirIncidente(event);
            }
        });

    }
    private void listarIncidentes(){
        if(incidentes.size()!=0){
            table.getItems().clear();
        }
        setDadosGerados("{\"operacao\": 5" + ",\"token\":" +"\"" + autenticacaoLoginArmazenada.getString("token") + "\",\"id\":" + autenticacaoLoginArmazenada.getInt("id") + "}");
        ValidaDadosIncidente validaDadosIncidente = new ValidaDadosIncidente(new JSONObject(getDadosGerados()));
        verificarRespostaServidor(iniciarComunicacaoServidor());
    }

    private void inicializaTabela(){
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colBairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        colRua.setCellValueFactory(new PropertyValueFactory<>("rua"));
        colTipo.setCellValueFactory(cellData -> {
            String tipoString = com.example.smaiccc_entrega_4.enums.Incidente.INCIDENTE_1.getDescricaoByNum(cellData.getValue().getId_incidente());
            return new SimpleStringProperty(tipoString);
        });
        colId.setCellValueFactory(new PropertyValueFactory<>("id_incidente"));
        preencherTabela();
    }

    private void preencherTabela(){
        listaIncidentes = FXCollections.observableList(incidentes);
        table.setItems(listaIncidentes);
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
        if(resposta!=null) {
            if (resposta.getString("status").equals("OK")) {
                if (resposta.getInt("operacao") == (5)) {
                    carregarJson(resposta);
                    inicializaTabela();
                } else if (resposta.getInt("operacao") == (6)) {
                    removerIncidentePorId();
                    inicializaTabela();
                }
            }
        }
    }

    private void carregarJson(JSONObject resposta) {
        JSONObject jsonIncidentes = resposta;
        JSONArray incidentesArray = jsonIncidentes.getJSONArray("incidentes");

        if(incidentesArray == null || incidentesArray.isEmpty()){
            textErroServer.setText("Sem resultados");
        } else {
            for (int i = 0; i < incidentesArray.length(); i++) {
                JSONObject jsonIncidente = incidentesArray.getJSONObject(i);
                String data = jsonIncidente.getString("data");
                String hora = jsonIncidente.getString("hora");
                String estado = jsonIncidente.getString("estado");
                String cidade = jsonIncidente.getString("cidade");
                String rua = jsonIncidente.getString("rua");
                String bairro = jsonIncidente.getString("bairro");
                int tipo = jsonIncidente.getInt("tipo_incidente");
                int id_incidente = jsonIncidente.getInt("id_incidente");
                incidentes.add(new Incidente(data, estado, cidade, bairro, hora, rua, tipo, id_incidente));
            }
        }
    }

    public void removerIncidentePorId() {
        for (Incidente incidente : listaIncidentes) {
            if (incidente.getId_incidente() == idIncidenteSelecionado) {
                listaIncidentes.remove(incidente);
                break;
            }
        }
        if(listaIncidentes.isEmpty()){
            btExcluir.setDisable(true);
        }
    }

    public String getDadosGerados() {
        return dadosGerados;
    }

    private void setDadosGerados(String dadosGerados) {
        this.dadosGerados = dadosGerados;
    }

    @FXML
    protected void listarIncidentes(ActionEvent event) {

    }

    @FXML
    protected void excluirIncidente(ActionEvent event) {
        setDadosGerados("{\"operacao\": 6" + ",\"token\":" +"\"" + autenticacaoLoginArmazenada.getString("token") + "\",\"id\":" + autenticacaoLoginArmazenada.getInt("id")  + ",\"id_incidente\":" + idIncidenteSelecionado + "}");
        verificarRespostaServidor(iniciarComunicacaoServidor());
    }

    public IncidenteUsuarioListagemController(int porta, String endereco, JSONObject autenticacaoLoginArmazenada) {
        this.cliente = new Cliente(porta, endereco);
        this.autenticacaoLoginArmazenada = autenticacaoLoginArmazenada;
    }

}
