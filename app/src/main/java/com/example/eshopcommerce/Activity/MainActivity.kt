package com.example.eshopcommerce.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eshopcommerce.R
import androidx.recyclerview.widget.RecyclerView
import com.example.eshopcommerce.Adapter.PopularListAdapter
import com.example.eshopcommerce.Domain.PopularDomain


class MainActivity : AppCompatActivity() {
    private lateinit var adapterPopular: RecyclerView.Adapter<*>
    private lateinit var recyclerViewPopular: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerview();
    }
    private fun initRecyclerview() {
        val items = ArrayList<PopularDomain>()
        items.add(PopularDomain("MacBook Pro 13 M2 Chip", "", "pic1", 15, 20.0, 4.0))
        items.add(PopularDomain("MacBook Pro 13 M2 Chip", "", "pic1", 15, 20.0, 4.0))
        items.add(PopularDomain("MacBook Pro 13 M2 Chip", "", "pic1", 15, 20.0, 4.0))
        recyclerViewPopular = findViewById(R.id.view1);
        recyclerViewPopular.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterPopular = PopularListAdapter(items)
        recyclerViewPopular.adapter = adapterPopular
    }
}