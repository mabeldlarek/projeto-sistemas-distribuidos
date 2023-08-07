package com.example.smaiccc_entrega_4.controller;

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

public class IncidenteListagemController <T> implements Initializable {

    @FXML
    private Pane root;
    @FXML
    private DatePicker txtData;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtCidade;
    @FXML
    private Text textErroServer;
    @FXML
    private Button btListar;
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
    //private TableColumn<Incidente, Integer> colTipo;
    @FXML
    private TableColumn<Incidente, Integer> colId;
    @FXML
    private TableView<Incidente> table;
    private ObservableList<Incidente> listaIncidentes;
    private List<Incidente> incidentes;
    private String dadosGerados;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        incidentes = new ArrayList<>();

        btListar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                listarIncidentes();
            }
        });
    }

    private void listarIncidentes(){
        if(incidentes.size()!=0){
            table.getItems().clear();
        }
        setDadosGerados("{\"data\":\""+ txtData.getValue() + "\",\"estado\":" +"\"" + txtEstado.getText().toUpperCase() + "\",\"cidade\":" +"\"" + txtCidade.getText().toUpperCase() + "\",\"operacao\": 4}");
        ValidaDadosIncidente validaDadosIncidente = new ValidaDadosIncidente(new JSONObject(getDadosGerados()));
        if(validaDadosIncidente.validaDadosListagemIncidente())
            verificarRespostaServidor(iniciarComunicacaoServidor());
        else
            textErroServer.setText(validaDadosIncidente.getErro());

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
        if(resposta == null)
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
                if (resposta.getInt("operacao") == (4)) {
                    carregarJson(resposta);
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
        }else {
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

    public String getDadosGerados() {
        return dadosGerados;
    }

    private void setDadosGerados(String dadosGerados) {
        this.dadosGerados = dadosGerados;
    }

    @FXML
    protected void listarIncidentes(ActionEvent event) {
        listarIncidentes();
    }

    public IncidenteListagemController(int porta, String endereco) {
        this.cliente = new Cliente(porta, endereco);
    }

    private ObservableList<Incidente> listaDeIncidentes() {
        return FXCollections.observableArrayList(incidentes);
    }
}



