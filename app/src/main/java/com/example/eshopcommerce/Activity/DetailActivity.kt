package com.example.eshopcommerce.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.eshopcommerce.Domain.PopularDomain
import com.example.eshopcommerce.Helper.ManagmentCart
import com.example.eshopcommerce.R

class DetailActivity : AppCompatActivity() {
    private lateinit var addToCartBtn: Button
    private lateinit var titleTxt: TextView
    private lateinit var feeTxt: TextView
    private lateinit var descriptionTxt: TextView
    private lateinit var reviewTxt: TextView
    private lateinit var scoreTxt: TextView
    private lateinit var picItem: ImageView
    private lateinit var backBtn: ImageView
    private var numberOrder: Int = 1
    private lateinit var managmentCart: ManagmentCart
    private lateinit var obj: PopularDomain
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        managmentCart = ManagmentCart(this)
        initView();
        getBundle();
    }

    private fun getBundle() {
        obj = (intent.getSerializableExtra("object") as? PopularDomain)!!
        val drawableResourceId = resources.getIdentifier(obj?.picUrl, "drawable", packageName)
        Glide.with(this).load(drawableResourceId).into(picItem)
        titleTxt.text = obj?.title
        reviewTxt.text = obj?.review.toString()
        scoreTxt.text = obj?.score.toString()
        feeTxt.text = obj?.price.toString()
        addToCartBtn.setOnClickListener {
            obj?.numberinCart = numberOrder
            managmentCart.insertFood(obj)
        }
        backBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
        }
    }

    private fun initView() {
        addToCartBtn = findViewById(R.id.addToCartBtn)
        feeTxt=findViewById(R.id.priceTxt)
        titleTxt=findViewById(R.id.titleTxt)
        descriptionTxt=findViewById(R.id.descriptionTxt)
        picItem=findViewById(R.id.itemPic)
        reviewTxt=findViewById(R.id.reviewTxt)
        scoreTxt=findViewById(R.id.scoreTxt)
        backBtn=findViewById(R.id.backBtn)
    }
}