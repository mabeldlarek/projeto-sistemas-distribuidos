package com.example.smaiccc_entrega_4.alerts;

import javafx.scene.control.Alert;

public class Dialogs {
    private Alert alerta;
    private int operacao;

    public Dialogs(int operacao) {
        this.operacao = operacao;
        this.alerta = new Alert(Alert.AlertType.NONE);
    }

    public void exibirErro(String mensagem){
        alerta.setAlertType(Alert.AlertType.ERROR);
        alerta.setHeaderText(mensagem);
        alerta.show();
    }

    public void exibirInformacao(){
        alerta.setAlertType(Alert.AlertType.INFORMATION);
        if (getOperacao() == 1){
            alerta.setHeaderText("Cadastro de usuário realizado com sucesso");
        }
        if (getOperacao() == 3){
            alerta.setHeaderText("Edição de cadastro realizada com sucesso. Você foi desconectado.");
        }
        if (getOperacao() == 6){
            alerta.setHeaderText("Incidente excluído com sucesso.");
        }
        if (getOperacao() == 7){
            alerta.setHeaderText("Incidente reportado com sucesso.");
        }
        if (getOperacao() == 8){
            alerta.setHeaderText("Exclusão realizada com sucesso. Você foi desconectado.");
        }
        if (getOperacao() == 9){
            alerta.setHeaderText("Logout realizado com sucesso. Você foi desconectado.");
        }

        alerta.show();
    }

    public boolean exibirConfirmacao(){
        alerta.setAlertType(Alert.AlertType.CONFIRMATION);

        if (getOperacao() == 8 || getOperacao() == 6 ){
            alerta.setHeaderText("Deseja realmente excluir o item selecionado? Essa ação não poderá ser desfeita");
        }

        String result = alerta.showAndWait().get().getText();
        if (result.equalsIgnoreCase("cancelar")) {
            return false;
        }

        return true;
    }

    public void exibirAviso(String mensagem){
        alerta.setAlertType(Alert.AlertType.WARNING);
        alerta.setHeaderText(mensagem);
        alerta.show();
    }

    public int getOperacao() {
        return operacao;
    }

    private void setOperacao(int operacao) {
        this.operacao = operacao;
    }

}
