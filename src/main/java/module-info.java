module com.example.produtoestoque {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.despesapessoal to javafx.fxml; // Permite que o FXML acesse as classes neste pacote
    opens com.example.despesapessoal.Controller to javafx.fxml; // Permite que o FXML acesse o controlador
    exports com.example.despesapessoal; // Exporta o pacote principal
    opens com.example.despesapessoal.Model to javafx.base;

}