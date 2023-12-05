package com.example.eshopcommerce.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eshopcommerce.Adapter.ProductListAdapter
import com.example.eshopcommerce.Entity.Produto
import com.example.eshopcommerce.R
import com.example.eshopcommerce.Service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductListActivity : AppCompatActivity() {
    private lateinit var productAdapter: ProductListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productAdapter = ProductListAdapter(ArrayList(), this)
        recyclerView.adapter = productAdapter

        initView()
        loadProducts()

        val addProductBtn: Button = findViewById(R.id.btnAddProduct)
        addProductBtn.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    private fun loadProducts() {
        val call = RetrofitService.produtoService.getProdutos()

        call.enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val produtos = response.body()
                    produtos?.let {
                        productAdapter.updateList(it)
                    }
                } else {
                    Log.e("API Response", "Erro na resposta da API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Log.e("API Failure", "Falha na chamada da API", t)
            }
        })

        productAdapter.setOnItemClickListener(object : ProductListAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                productAdapter.deleteProduct(position)
            }

            override fun onUpdateClick(position: Int) {
                val produto = productAdapter.getProductAt(position)
                val intent = Intent(this@ProductListActivity, UpdateProductActivity::class.java)
                intent.putExtra("productId", produto.id)
                startActivity(intent)
            }
        })

        backBtn.setOnClickListener {
            startActivity(Intent(this@ProductListActivity, MainActivity::class.java))
        }
    }

    private fun initView() {
        backBtn = findViewById(R.id.backBtn)
    }
}
