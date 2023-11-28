package com.example.eshopcommerce.Service

import com.example.eshopcommerce.Entity.Produto
import retrofit2.Call
import retrofit2.http.*

interface ProdutoService {
    @GET("/api/produtos")
    fun getProdutos(): Call<List<Produto>>

    @POST("/api/produtos")
    fun criarProduto(@Body produto: Produto): Call<Produto>

    @PUT("/api/produtos/{id}")
    fun updateProduto(@Path("id") id: String, @Body produto: Produto): Call<Produto>

    @DELETE("/api/produtos/{id}")
    fun deleteProduto(@Path("id") id: String): Call<Void>
}
