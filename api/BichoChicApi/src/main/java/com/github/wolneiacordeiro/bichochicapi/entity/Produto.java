package com.github.wolneiacordeiro.bichochicapi.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collection = "produtos")
public class Produto {
    @Id
    private String id;
    private String nome;
    private double preco;
    private String descricao;
    private String tamanho;
    private int quantidade;
    private String especie;
    private String categoria;
    private String imagem;

    public Produto(String id, String nome, double preco, String descricao, String tamanho, int quantidade, String especie, String categoria, String imagem) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.tamanho = tamanho;
        this.quantidade = quantidade;
        this.especie = especie;
        this.categoria = categoria;
        this.imagem = imagem;
    }

    public Produto() {
        this.id = UUID.randomUUID().toString();
    }
}
