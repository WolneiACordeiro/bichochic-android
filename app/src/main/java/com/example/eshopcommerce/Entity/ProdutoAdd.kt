package com.example.eshopcommerce.Entity

data class ProdutoAdd(
    val id: String,
    val nome: String,
    val preco: Double,
    val descricao: String,
    val tamanho: String,
    val quantidade: Int,
    val especie: String,
    val categoria: String,
    val imagem: String
)
