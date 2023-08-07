package com.example.smaiccc_entrega_4.controller;

import com.example.smaiccc_entrega_4.TransicaoTela;
import com.example.smaiccc_entrega_4.alerts.Dialogs;
import com.example.smaiccc_entrega_4.cliente.Cliente;
import com.example.smaiccc_entrega_4.enums.Incidente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoErroComunicacaoServidor;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloCliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoRespostaNulaServidor;
import com.example.smaiccc_entrega_4.validacao.ValidaDadosIncidente;
import com.example.smaiccc_entrega_4.validacao.ValidaRespostaServidor;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class IncidenteCadastroController <T>implements Initializable {
    @FXML
    private DatePicker txtData;
    @FXML
    private TextField txtHora;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtCidade;
    @FXML
    private TextField txtRua;
    @FXML
    private TextField txtBairro;
    @FXML
    private ComboBox <String> cbIncidente;
    @FXML
    private Button btCadastrar;
    @FXML
    private Pane root;
    @FXML
    private Text textErroServer;
    private Cliente cliente;
    private String dadosGerados;
    private TransicaoTela tt;
    private ObservableList<Incidente> incidentes;
    private int numIncidenteSelecionado;
    private JSONObject autenticacaoLoginArmazenada;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popularComboBox();

        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                    realizarCadastro();
            }
        });

        cbIncidente.valueProperty().addListener((observable, oldValue, newValue) -> {
            verificarNumIncidenteCorrespondente();
        });
    }

    private void realizarCadastro(){
        setDadosGerados("{\"data\":"+ txtData.getValue() + ",\"hora\":" + "\""+ txtHora.getText() + "\",\"estado\":" +"\"" + txtEstado.getText().toUpperCase() + "\",\"cidade\":" +"\"" + txtCidade.getText().toUpperCase() + "\",\"bairro\":" +"\"" +  txtBairro.getText().toUpperCase() +  "\",\"rua\":" +"\"" +
                txtRua.getText().toUpperCase() + "\",\"tipo_incidente\":" + numIncidenteSelecionado + ",\"token\":" +"\"" + autenticacaoLoginArmazenada.getString("token") + "\",\"id\":" + autenticacaoLoginArmazenada.getInt("id") + ",\"operacao\": 7}");
        ValidaDadosIncidente validaDadosIncidente = new ValidaDadosIncidente(new JSONObject(getDadosGerados()));
        if(validaDadosIncidente.validaDadosCadastroIncidente())
            verificarRespostaServidor(iniciarComunicacaoServidor());
        else
            textErroServer.setText(validaDadosIncidente.getErro());
    }

    private JSONObject iniciarComunicacaoServidor(){
        JSONObject resposta = null;
        try {
            cliente.iniciarCliente();
            JSONObject jsonLogin = new JSONObject(getDadosGerados());
            resposta = cliente.enviarDadosServidor(jsonLogin);
            System.out.println("ENVIADO: " + jsonLogin.toString());
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
        if(resposta !=null) {
            if (resposta.getString("status").equals("OK")) {
                Dialogs dialogs = new Dialogs(7);
                dialogs.exibirInformacao();
                obterTelaAtual().getOwner().hide();
            }
        }
        else textErroServer.setText(resposta.getString("status"));
    }

    private void verificarNumIncidenteCorrespondente(){
        String descricaoSelecionada = cbIncidente.getValue();
        numIncidenteSelecionado = -1;

        for (Incidente incidente : Incidente.values()) {
            if (incidente.getDescricao().equals(descricaoSelecionada)) {
                numIncidenteSelecionado = incidente.getNum();
                break;
            }
        }
    }

    private void popularComboBox(){
        cbIncidente.getItems().addAll(obterListaDescricaoIncidente());
    }

    public List<String> obterListaDescricaoIncidente(){
        List<String> incidenteDescricoes = new ArrayList<>();

        for (Incidente incidente : Incidente.values()) {
            incidenteDescricoes.add(incidente.getDescricao());
        }

        return incidenteDescricoes;
    }


    private Stage obterTelaAtual(){
        Stage st = (Stage) root.getScene().getWindow();
        return st;
    }

    @FXML
    protected void clickCadastrar(ActionEvent event) {
    }

    public IncidenteCadastroController(JSONObject autenticacaoLoginArmazenada, int porta, String endereco) {
        this.autenticacaoLoginArmazenada = autenticacaoLoginArmazenada;
        this.cliente = new Cliente(porta, endereco);

    }
    public String getDadosGerados() {
        return dadosGerados;
    }

    private void setDadosGerados(String dadosGerados) {
        this.dadosGerados = dadosGerados;
    }
}
