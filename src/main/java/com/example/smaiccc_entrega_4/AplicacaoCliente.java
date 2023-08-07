package com.example.smaiccc_entrega_4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AplicacaoCliente extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AplicacaoCliente.class.getResource("cliente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Menu Inicial Cliente");
        stage.setScene(scene);
        stage.setX(250);
        stage.setMinWidth(376);
        stage.setMinHeight(300);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}