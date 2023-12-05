package com.example.eshopcommerce.Service

import com.example.eshopcommerce.Entity.Produto
import com.example.eshopcommerce.Entity.ProdutoAdd
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProdutoService {
    @GET("/api/produtos")
    fun getProdutos(): Call<List<Produto>>

    @GET("/api/produtos/{id}")
    fun getProduto(@Path("id") id: String): Call<ProdutoAdd>

    @Multipart
    @POST("/api/produtos")
    fun criarProduto(
        @Part("nome") nome: RequestBody,
        @Part("preco") preco: RequestBody,
        @Part("descricao") descricao: RequestBody,
        @Part("tamanho") tamanho: RequestBody,
        @Part("quantidade") quantidade: RequestBody,
        @Part("especie") especie: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part imagem: MultipartBody.Part?
    ): Call<ProdutoAdd>

    @Multipart
    @PUT("/api/produtos/{id}")
    fun updateProduto(
        @Path("id") id: String,
        @Part("nome") nome: RequestBody,
        @Part("preco") preco: RequestBody,
        @Part("descricao") descricao: RequestBody,
        @Part("tamanho") tamanho: RequestBody,
        @Part("quantidade") quantidade: RequestBody,
        @Part("especie") especie: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part imagem: MultipartBody.Part?
    ): Call<ProdutoAdd>

    @DELETE("/api/produtos/{id}")
    fun deleteProduto(@Path("id") id: String): Call<Void>
}
