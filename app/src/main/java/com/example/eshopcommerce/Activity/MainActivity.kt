package com.example.eshopcommerce.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.example.eshopcommerce.R
import androidx.recyclerview.widget.RecyclerView
import com.example.eshopcommerce.Adapter.PopularListAdapter
import com.example.eshopcommerce.Domain.PopularDomain
import com.example.eshopcommerce.Entity.Produto
import com.example.eshopcommerce.Service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MainActivity : AppCompatActivity() {
    private lateinit var adapterPopular: RecyclerView.Adapter<*>
    private lateinit var recyclerViewPopular: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerview();
        bottom_navigation();
    }

    private fun bottom_navigation() {
        val homeBtn: LinearLayout = findViewById(R.id.homeBtn)
        val cartBtn: LinearLayout = findViewById(R.id.cartBtn)
        val productsBtn: LinearLayout = findViewById(R.id.productsBtn)

        homeBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity::class.java))
        }

        cartBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, CartActivity::class.java))
        }

        productsBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProductListActivity::class.java))
        }
    }

    private fun initRecyclerview() {
        val items = ArrayList<PopularDomain>()
        val call = RetrofitService.produtoService.getProdutos()

        recyclerViewPopular = findViewById(R.id.recyclerViewPopular)

        call.enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val produtos = response.body()
                    produtos?.let {
                        for (produto in it) {
                            val popularItem = PopularDomain(
                                produto.nome,
                                produto.descricao,
                                produto.imagem,
                                20,
                                20.00,
                                produto.preco
                            )
                            items.add(popularItem)
                        }
                        adapterPopular = PopularListAdapter(items)

                        recyclerViewPopular.adapter = adapterPopular
                    }
                } else {
                    Log.e("API Response", "Erro na resposta da API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                // Log de falha, se necessário
                Log.e("API Failure", "Falha na chamada da API", t)
            }
        })
    }


}