package com.example.eshopcommerce.Activity

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eshopcommerce.Entity.ProdutoAdd
import com.example.eshopcommerce.R
import com.example.eshopcommerce.Service.RetrofitService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UpdateProductActivity : AppCompatActivity() {

    private var editTextNome: EditText? = null
    private var editTextPreco: EditText? = null
    private var editTextDescricao: EditText? = null
    private var editTextQtd: EditText? = null
    private var spinnerSize: Spinner? = null
    private var spinnerCategory: Spinner? = null
    private var spinnerSpecies: Spinner? = null
    private var btnUpdateProduct: Button? = null
    private var btnSelectImage: Button? = null
    private lateinit var imgPreview: ImageView
    private var selectedImageUri: Uri? = null
    private var productId: String? = null
    private lateinit var backBtn: ImageView

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    imgPreview.setImageURI(uri)
                    selectedImageUri = uri
                    btnUpdateProduct?.isEnabled = true
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_product)
        productId = intent.getStringExtra("productId").toString()
        initView()

        editTextNome = findViewById(R.id.editTextNome)
        editTextPreco = findViewById(R.id.editTextPreco)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        editTextQtd = findViewById(R.id.editTextQtd)
        spinnerSize = findViewById(R.id.spinnerSize)
        spinnerSpecies = findViewById(R.id.spinnerSpecies)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        imgPreview = findViewById(R.id.imgPreview)

        btnSelectImage?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        val produtoCall = RetrofitService.produtoService.getProduto(productId!!)
        produtoCall.enqueue(object : retrofit2.Callback<ProdutoAdd> {
            override fun onResponse(call: Call<ProdutoAdd>, response: Response<ProdutoAdd>) {
                if (response.isSuccessful) {
                    val produto = response.body()
                    preencherCamposComDetalhesAtuais(produto)

                    produto?.imagem?.let { imageName ->
                        val imageUrl = "file:///android_asset/images/$imageName"

                        Log.d("UpdateProductActivity", "URL da imagem: $imageUrl")

                        Glide.with(this@UpdateProductActivity)
                            .load(Uri.parse(imageUrl))
                            .into(imgPreview)
                    }
                } else {
                    Toast.makeText(
                        this@UpdateProductActivity,
                        "Erro ao obter os detalhes do produto. Código: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProdutoAdd>, t: Throwable) {
                t.printStackTrace()
                Log.e("API_CALL", "Falha na chamada da API: ${t.message}")
                Toast.makeText(
                    this@UpdateProductActivity,
                    "Falha na chamada da API",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        btnUpdateProduct?.setOnClickListener {
            Log.d("UpdateProductActivity", "Iniciando atualização do produto com ID: $productId")

            val nome = editTextNome?.text.toString()
            val precoText = editTextPreco?.text.toString()
            val descricao = editTextDescricao?.text.toString()
            val quantidade = editTextQtd?.text.toString()
            val tamanho = spinnerSize?.selectedItem.toString()
            val categoria = spinnerCategory?.selectedItem.toString()
            val especie = spinnerSpecies?.selectedItem.toString()
            val imagem = selectedImageUri

            if (nome.isEmpty() || precoText.isEmpty() || descricao.isEmpty()) {
                Toast.makeText(
                    this@UpdateProductActivity,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val preco = try {
                precoText.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    this@UpdateProductActivity,
                    "Digite um valor válido para o preço",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            fun createPartFromString(value: String): RequestBody {
                return RequestBody.create(MultipartBody.FORM, value)
            }

            val nomePart = createPartFromString(nome)
            val precoPart = createPartFromString(precoText)
            val descricaoPart = createPartFromString(descricao)
            val tamanhoPart = createPartFromString(tamanho)
            val quantidadePart = createPartFromString(quantidade)
            val categoriaPart = createPartFromString(categoria)
            val especiePart = createPartFromString(especie)

            val imagemFile = createImageFileFromUri(selectedImageUri)
            val imagemPart = createPartFromImageFile("imagem", imagemFile)

            val call = RetrofitService.produtoService.updateProduto(
                productId!!,
                nomePart,
                precoPart,
                descricaoPart,
                tamanhoPart,
                quantidadePart,
                categoriaPart,
                especiePart,
                imagemPart
            )

            call.enqueue(object : retrofit2.Callback<ProdutoAdd> {
                override fun onResponse(call: Call<ProdutoAdd>, response: Response<ProdutoAdd>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UpdateProductActivity,
                            "Produto atualizado com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Limpe os campos ou faça qualquer outra ação necessária após o sucesso
                        // editTextNome?.setText("")
                        // editTextPreco?.setText("")
                        // editTextDescricao?.setText("")
                        // editTextQtd?.setText("")
                        // imgPreview.setImageURI(null)
                    } else {
                        Toast.makeText(
                            this@UpdateProductActivity,
                            "Erro ao atualizar o produto. Código: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProdutoAdd>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("API_CALL", "Falha na chamada da API: ${t.message}")
                    Toast.makeText(
                        this@UpdateProductActivity,
                        "Falha na chamada da API",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        backBtn.setOnClickListener {
            startActivity(Intent(this@UpdateProductActivity, ProductListActivity::class.java))
        }
    }

    private fun initView() {
        backBtn = findViewById(R.id.backBtn)
    }

    private fun preencherCamposComDetalhesAtuais(produto: ProdutoAdd?) {
        produto ?: return

        editTextNome?.setText(produto.nome)
        editTextPreco?.setText(produto.preco.toString())
        editTextDescricao?.setText(produto.descricao)
        editTextQtd?.setText(produto.quantidade.toString())


        val tamanhoArray = resources.getStringArray(R.array.size_array)
        val categoriaArray = resources.getStringArray(R.array.category_array)
        val especieArray = resources.getStringArray(R.array.species_array)

        val tamanhoIndex = tamanhoArray.indexOf(produto.tamanho)
        if (tamanhoIndex != -1) {
            spinnerSize?.setSelection(tamanhoIndex)
        } else {
            Log.e("UpdateProductActivity", "Tamanho do produto não encontrado na lista: ${produto.tamanho}")
        }

        val categoriaIndex = categoriaArray.indexOf(produto.categoria)
        if (categoriaIndex != -1) {
            spinnerCategory?.setSelection(categoriaIndex)
        } else {
            Log.e("UpdateProductActivity", "Categoria do produto não encontrada na lista: ${produto.categoria}")
        }

        val especieIndex = especieArray.indexOf(produto.especie)
        if (especieIndex != -1) {
            spinnerSpecies?.setSelection(especieIndex)
        } else {
            Log.e("UpdateProductActivity", "Espécie do produto não encontrada na lista: ${produto.especie}")
        }
    }


    private fun createImageFileFromUri(uri: Uri?): File? {
        uri ?: return null
        val contentResolver: ContentResolver = contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)

        val mimeType = contentResolver.getType(uri)
        val fileExtension = mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }

        val file = File(cacheDir, "temp_image.${fileExtension ?: "temp"}")
        FileOutputStream(file).use {
            it.write(inputStream?.readBytes() ?: return null)
        }

        return file
    }

    private fun createPartFromImageFile(name: String, file: File?): MultipartBody.Part? {
        file ?: return null
        val contentResolver: ContentResolver = contentResolver
        val mimeType = contentResolver.getType(Uri.fromFile(file))
        val requestBody: RequestBody = RequestBody.create(mimeType?.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(name, file.name, requestBody)
    }
}
