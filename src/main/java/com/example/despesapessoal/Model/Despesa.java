package com.example.despesapessoal.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Despesa {

    private final IntegerProperty id;
    private final StringProperty descricao;
    private final DoubleProperty valor;
    private final ObjectProperty<LocalDate> data;
    private final StringProperty categoria;

    public Despesa(int id, String descricao, double valor, LocalDate data, String categoria) {
        this.id = new SimpleIntegerProperty(id);
        this.descricao = new SimpleStringProperty(descricao);
        this.valor = new SimpleDoubleProperty(valor);
        this.data = new SimpleObjectProperty<>(data);
        this.categoria = new SimpleStringProperty(categoria);
    }

    // --- Getters, Setters e Properties ---

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getDescricao() { return descricao.get(); }
    public void setDescricao(String descricao) { this.descricao.set(descricao); }
    public StringProperty descricaoProperty() { return descricao; }

    public double getValor() { return valor.get(); }
    public void setValor(double valor) { this.valor.set(valor); }
    public DoubleProperty valorProperty() { return valor; }

    public LocalDate getData() { return data.get(); }
    public void setData(LocalDate data) { this.data.set(data); }
    public ObjectProperty<LocalDate> dataProperty() { return data; }

    public String getCategoria() { return categoria.get(); }
    public void setCategoria(String categoria) { this.categoria.set(categoria); }
    public StringProperty categoriaProperty() { return categoria; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Despesa despesa = (Despesa) o;
        return getId() == despesa.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}