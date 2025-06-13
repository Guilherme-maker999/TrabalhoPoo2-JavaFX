package com.example.produtoestoque.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Produto {

    private final IntegerProperty id;
    private final StringProperty nome;
    private final DoubleProperty preco;
    private final IntegerProperty quantidade;

    public Produto(int id, String nome, double preco, int quantidade) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.preco = new SimpleDoubleProperty(preco);
        this.quantidade = new SimpleIntegerProperty(quantidade);
    }


    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public IntegerProperty idProperty() {
        return id;
    }

    public String getNome() {
        return nome.get();
    }
    public void setNome(String nome) {
        this.nome.set(nome);
    }
    public StringProperty nomeProperty() {
        return nome;
    }

    public double getPreco() {
        return preco.get();
    }
    public void setPreco(double preco) {
        this.preco.set(preco);
    }
    public DoubleProperty precoProperty() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade.get();
    }
    public void setQuantidade(int quantidade) {
        this.quantidade.set(quantidade);
    }
    public IntegerProperty quantidadeProperty() {
        return quantidade;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id.get() +
                ", nome='" + nome.get() + '\'' +
                ", preco=" + preco.get() +
                ", quantidade=" + quantidade.get() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return getId() == produto.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}