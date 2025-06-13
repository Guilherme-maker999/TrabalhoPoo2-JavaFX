package com.example.despesapessoal.Repository;

import com.example.despesapessoal.Model.Despesa;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DespesaRepository {

    private final String caminhoCSV = "despesas.csv";
    private List<Despesa> despesas = new ArrayList<>();

    public DespesaRepository() {
        carregarCSV();
    }

    public void adicionarDespesa(Despesa despesa) {
        despesas.add(despesa);
        salvarCSV();
    }

    public void removerDespesa(int id) {
        despesas.removeIf(d -> d.getId() == id);
        salvarCSV();
    }

    public List<Despesa> listarTodas() {
        return Collections.unmodifiableList(despesas);
    }

    public void salvarCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCSV))) {
            for (Despesa d : despesas) {
                writer.write(toCSV(d));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar despesas.csv: " + e.getMessage());
        }
    }

    private void carregarCSV() {
        if (!Files.exists(Paths.get(caminhoCSV))) return;

        despesas.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoCSV))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");
                if (campos.length < 5) continue;

                Despesa despesa = new Despesa(
                        Integer.parseInt(campos[0]),      // id
                        campos[1],                          // descricao
                        Double.parseDouble(campos[2]),      // valor
                        LocalDate.parse(campos[3]),         // data
                        campos[4]                           // categoria
                );
                despesas.add(despesa);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar despesas.csv: " + e.getMessage());
        }
    }

    private String toCSV(Despesa despesa) {
        return String.join(";",
                String.valueOf(despesa.getId()),
                despesa.getDescricao(),
                String.valueOf(despesa.getValor()),
                despesa.getData().toString(), // Converte LocalDate para String
                despesa.getCategoria()
        );
    }
}