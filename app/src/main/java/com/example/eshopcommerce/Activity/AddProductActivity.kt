package com.example.eshopcommerce.Activity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.eshopcommerce.R

class AddProductActivity : AppCompatActivity() {
    private var editTextNome: EditText? = null
    private var editTextPreco: EditText? = null
    private var editTextDescricao: EditText? = null
    private var btnAddProduct: Button? = null
    private var btnSelectImage: Button? = null


    private val pickImageLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {

            Toast.makeText(this, "Imagem selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        editTextNome = findViewById(R.id.editTextNome)
        editTextPreco = findViewById(R.id.editTextPreco)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        btnAddProduct = findViewById(R.id.btnAddProduct)
        btnSelectImage = findViewById(R.id.btnSelectImage)

        btnSelectImage?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        btnAddProduct?.setOnClickListener {
            val nome = editTextNome?.text.toString()
            val precoText = editTextPreco?.text.toString()
            val descricao = editTextDescricao?.text.toString()

            if (nome.isEmpty() || precoText.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(
                    this@AddProductActivity,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val preco = try {
                precoText.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    this@AddProductActivity,
                    "Digite um valor válido para o preço",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this@AddProductActivity,
                "Produto adicionado com sucesso",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
