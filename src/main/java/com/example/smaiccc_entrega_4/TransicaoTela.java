/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.smaiccc_entrega_4;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author Meu Computador
 */
public class TransicaoTela<T> {

    private Scene scene;
    private String name = "";

    public TransicaoTela(String name) {
        this.name = name;
    }

    public TransicaoTela() {

    }

    public void iniciarStage(FXMLLoader fxmlloader, Stage stgPai) {
        try {
            Parent my_root;
            my_root = (Parent) fxmlloader.load();
            Stage stage = new Stage();
            stage.initOwner(stgPai);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(my_root));
            setScene(stage.getScene());
            stage.setTitle(this.name);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
           ex.getMessage();//
        }
    }

    private void setScene(Scene scene){
        this.scene = scene;
    }
}
