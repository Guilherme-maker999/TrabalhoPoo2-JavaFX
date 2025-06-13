package com.example.despesapessoal.Controller;

import com.example.despesapessoal.Model.Despesa;
import com.example.despesapessoal.Repository.DespesaRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DespesaController {

    //<editor-fold desc="Atributos FXML">
    @FXML private TableView<Despesa> tableViewDespesas;
    @FXML private TableColumn<Despesa, Integer> colId;
    @FXML private TableColumn<Despesa, String> colDescricao;
    @FXML private TableColumn<Despesa, Double> colValor;
    @FXML private TableColumn<Despesa, LocalDate> colData;
    @FXML private TableColumn<Despesa, String> colCategoria;
    @FXML private TextField txtId;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtValor;
    @FXML private DatePicker datePickerData;
    @FXML private ComboBox<String> comboBoxCategoria;
    //</editor-fold>

    private final DespesaRepository despesaRepository = new DespesaRepository();
    private Despesa despesaSelecionada;

    @FXML
    private void initialize() {
        configurarTabela();
        configurarComponentes();
        atualizarTabela();

        tableViewDespesas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarDespesa(newValue)
        );
    }

    @FXML
    void salvarDespesa(ActionEvent event) {
        // Coleta de dados da UI
        String descricao = txtDescricao.getText().trim();
        String valorStr = txtValor.getText().trim();
        LocalDate data = datePickerData.getValue();
        String categoria = comboBoxCategoria.getValue();

        // Validação
        if (descricao.isEmpty() || valorStr.isEmpty() || data == null || categoria == null || categoria.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha todos os campos.");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr.replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "Por favor, insira um valor numérico válido para o Valor.");
            return;
        }

        if (despesaSelecionada == null) { // Criar nova despesa
            int novoId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
            Despesa novaDespesa = new Despesa(novoId, descricao, valor, data, categoria);
            despesaRepository.adicionarDespesa(novaDespesa);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Despesa cadastrada com sucesso!");
        } else { // Atualizar despesa existente
            despesaSelecionada.setDescricao(descricao);
            despesaSelecionada.setValor(valor);
            despesaSelecionada.setData(data);
            despesaSelecionada.setCategoria(categoria);
            despesaRepository.salvarCSV(); // Salva o estado atual da lista
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Despesa atualizada com sucesso!");
        }

        atualizarTabela();
        limparFormulario();
    }

    @FXML
    void novoDespesa(ActionEvent event) {
        limparFormulario();
    }

    @FXML
    void excluirDespesa(ActionEvent event) {
        if (despesaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Despesa Selecionada", "Por favor, selecione uma despesa na lista para excluir.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Tem certeza que deseja excluir a despesa?");
        alert.setContentText(despesaSelecionada.getDescricao());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            despesaRepository.removerDespesa(despesaSelecionada.getId());
            atualizarTabela();
            limparFormulario();
        }
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
    }

    private void configurarComponentes() {
        // Preenche o ComboBox com categorias pré-definidas
        comboBoxCategoria.setItems(FXCollections.observableArrayList(
                "Alimentação", "Transporte", "Lazer", "Moradia", "Saúde", "Educação", "Outros"
        ));
        // Define a data de hoje como padrão no DatePicker
        datePickerData.setValue(LocalDate.now());
    }

    private void atualizarTabela() {
        List<Despesa> despesas = despesaRepository.listarTodas();
        tableViewDespesas.getItems().setAll(despesas);
    }

    private void selecionarDespesa(Despesa despesa) {
        despesaSelecionada = despesa;
        if (despesa != null) {
            txtId.setText(String.valueOf(despesa.getId()));
            txtDescricao.setText(despesa.getDescricao());
            txtValor.setText(String.format("%.2f", despesa.getValor()));
            datePickerData.setValue(despesa.getData());
            comboBoxCategoria.setValue(despesa.getCategoria());
        }
    }

    private void limparFormulario() {
        txtId.clear();
        txtDescricao.clear();
        txtValor.clear();
        datePickerData.setValue(LocalDate.now());
        comboBoxCategoria.setValue(null);
        despesaSelecionada = null;
        tableViewDespesas.getSelectionModel().clearSelection();
    }


    @FXML
    void fecharAplicacao(ActionEvent event) { System.exit(0); }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}