package com.example.produtoestoque.Controller;

import com.example.produtoestoque.Model.Produto;
import com.example.produtoestoque.Repository.ProdutoRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

public class ProdutoController {

    @FXML
    private TableColumn<Produto, Integer> colId;
    @FXML
    private TableColumn<Produto, String> colNome;
    @FXML
    private TableColumn<Produto, Double> colPreco;
    @FXML
    private TableColumn<Produto, Integer> colQuantidade;
    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtQuantidade;

    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private Produto produtoSelecionado;

    @FXML
    private void initialize() {
        configurarTabela();
        atualizarTabela();

        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarProduto(newValue)
        );
    }

    @FXML
    void salvarProduto(ActionEvent event) {
        String nome = txtNome.getText().trim();
        String precoStr = txtPreco.getText().trim();
        String qtdStr = txtQuantidade.getText().trim();

        if (nome.isEmpty() || precoStr.isEmpty() || qtdStr.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha todos os campos.");
            return;
        }

        double preco;
        int quantidade;
        try {
            // Tenta converter os campos de texto para números
            preco = Double.parseDouble(precoStr.replace(",", ".")); // Substitui vírgula por ponto
            quantidade = Integer.parseInt(qtdStr);
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Erro de Formato", "Por favor, insira valores numéricos válidos para Preço e Quantidade.");
            return;
        }

        if (produtoSelecionado == null) { // Criar novo produto
            int novoId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
            Produto novoProduto = new Produto(novoId, nome, preco, quantidade);
            produtoRepository.adicionarProduto(novoProduto);
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Produto cadastrado com sucesso!");
        } else { // Atualizar produto existente
            produtoSelecionado.setNome(nome);
            produtoSelecionado.setPreco(preco);
            produtoSelecionado.setQuantidade(quantidade);
            produtoRepository.salvarCSV(); // Apenas salvar as alterações no arquivo
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Produto atualizado com sucesso!");
        }

        atualizarTabela();
        limparFormulario();
    }

    @FXML
    void novoProduto(ActionEvent event) {
        limparFormulario();
    }

    @FXML
    void excluirProduto(ActionEvent event) {
        if (produtoSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Nenhum Produto Selecionado", "Por favor, selecione um produto na lista para excluir.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Você tem certeza que deseja excluir o produto?");
        alert.setContentText(produtoSelecionado.getNome());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            produtoRepository.removerProduto(produtoSelecionado.getId());
            atualizarTabela();
            limparFormulario();
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Produto excluído com sucesso.");
        }
    }

    @FXML
    void fecharAplicacao(ActionEvent event) {
        System.exit(0);
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
    }

    private void atualizarTabela() {
        List<Produto> produtos = produtoRepository.listarTodos();
        tableViewProdutos.getItems().clear();
        tableViewProdutos.getItems().addAll(produtos);
    }

    private void selecionarProduto(Produto produto) {
        produtoSelecionado = produto;
        if (produto != null) {
            txtId.setText(String.valueOf(produto.getId()));
            txtNome.setText(produto.getNome());
            txtPreco.setText(String.format("%.2f", produto.getPreco())); // Formata o preço
            txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
        }
    }

    private void limparFormulario() {
        txtId.clear();
        txtNome.clear();
        txtPreco.clear();
        txtQuantidade.clear();
        produtoSelecionado = null;
        tableViewProdutos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}