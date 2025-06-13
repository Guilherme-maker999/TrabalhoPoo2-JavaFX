module com.example.produtoestoque {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.produtoestoque to javafx.fxml; // Permite que o FXML acesse as classes neste pacote
    opens com.example.produtoestoque.Controller to javafx.fxml; // Permite que o FXML acesse o controlador
    exports com.example.produtoestoque; // Exporta o pacote principal
    opens com.example.produtoestoque.Model to javafx.base;

}