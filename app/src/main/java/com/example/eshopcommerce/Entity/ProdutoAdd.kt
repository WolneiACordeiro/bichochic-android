package com.example.eshopcommerce.Entity

import okhttp3.MultipartBody

data class ProdutoAdd(
    val nome: String,
    val preco: Double,
    val descricao: String,
    val tamanho: String,
    val quantidade: Int,
    val especie: String,
    val categoria: String,
    val imagem: String
)
