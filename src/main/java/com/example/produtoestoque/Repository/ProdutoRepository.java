package com.example.produtoestoque.Repository;

import com.example.produtoestoque.Model.Produto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProdutoRepository {
    private final String caminhoCSV = "produtos.csv";

    private List<Produto> produtos = new ArrayList<>();

    public ProdutoRepository() {
        carregarCSV();
    }

    public Produto buscarPorId(int id) {
        return produtos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        salvarCSV();
    }

    public void removerProduto(int id) {
        produtos.removeIf(p -> p.getId() == id);
        salvarCSV();
    }

    public List<Produto> listarTodos() {
        return Collections.unmodifiableList(produtos);
    }

    private void carregarCSV() {
        if (!Files.exists(Paths.get(caminhoCSV))) return;

        produtos.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoCSV))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");
                if (campos.length < 4) continue;

                Produto produto = new Produto(
                        Integer.parseInt(campos[0]), // id
                        campos[1],                   // nome
                        Double.parseDouble(campos[2]), // preco
                        Integer.parseInt(campos[3])  // quantidade
                );
                produtos.add(produto);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao carregar CSV de produtos: " + e.getMessage());
        }
    }

    public void salvarCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCSV))) {
            for (Produto p : produtos) {
                writer.write(toCSV(p));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar CSV de produtos: " + e.getMessage());
        }
    }

    private String toCSV(Produto produto) {
        return String.join(";",
                String.valueOf(produto.getId()),
                produto.getNome(),
                String.valueOf(produto.getPreco()),
                String.valueOf(produto.getQuantidade())
        );
    }
}