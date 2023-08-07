module com.example.demo_teste_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;


    opens com.example.smaiccc_entrega_4 to javafx.fxml;
    exports com.example.smaiccc_entrega_4;
    exports com.example.smaiccc_entrega_4.controller;
    exports com.example.smaiccc_entrega_4.servidor;
    opens com.example.smaiccc_entrega_4.controller to javafx.fxml;
    opens com.example.smaiccc_entrega_4.servidor to javafx.fxml;
    opens com.example.smaiccc_entrega_4.model to javafx.base;
    exports com.example.smaiccc_entrega_4.cliente;
    opens com.example.smaiccc_entrega_4.cliente to javafx.fxml;
}