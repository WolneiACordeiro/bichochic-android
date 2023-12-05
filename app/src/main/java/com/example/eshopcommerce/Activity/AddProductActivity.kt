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

class AddProductActivity : AppCompatActivity() {

    private var editTextNome: EditText? = null
    private var editTextPreco: EditText? = null
    private var editTextDescricao: EditText? = null
    private var editTextQtd: EditText? = null
    private var spinnerSize: Spinner? = null
    private var spinnerCategory: Spinner? = null
    private var spinnerSpecies: Spinner? = null
    private var btnAddProduct: Button? = null
    private var btnSelectImage: Button? = null
    private lateinit var imgPreview: ImageView
    private var selectedImageUri: Uri? = null
    private lateinit var backBtn: ImageView

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    imgPreview.setImageURI(uri)
                    selectedImageUri = uri
                    btnAddProduct?.isEnabled = true
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        initView()

        editTextNome = findViewById(R.id.editTextNome)
        editTextPreco = findViewById(R.id.editTextPreco)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        editTextQtd = findViewById(R.id.editTextQtd)
        spinnerSize = findViewById(R.id.spinnerSize)
        spinnerSpecies = findViewById(R.id.spinnerSpecies)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnAddProduct = findViewById(R.id.btnAddProduct)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        imgPreview = findViewById(R.id.imgPreview)

        btnSelectImage?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        btnAddProduct?.setOnClickListener {
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
                    this@AddProductActivity,
                    "Preencha todos os campos",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (selectedImageUri == null) {
                Toast.makeText(
                    this@AddProductActivity,
                    "Selecione uma imagem antes de adicionar o produto",
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

            val call = RetrofitService.produtoService.criarProduto(
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
                        // Trate o sucesso da chamada
                        Toast.makeText(
                            this@AddProductActivity,
                            "Produto adicionado com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Limpe os campos ou faça qualquer outra ação necessária após o sucesso
                        // editTextNome?.setText("")
                        // editTextPreco?.setText("")
                        // editTextDescricao?.setText("")
                        // editTextQtd?.setText("")
                        // imgPreview.setImageURI(null)
                    } else {
                        // Trate os casos em que a chamada não foi bem-sucedida
                        Toast.makeText(
                            this@AddProductActivity,
                            "Erro ao adicionar o produto. Código: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProdutoAdd>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("API_CALL", "Falha na chamada da API: ${t.message}")
                    Toast.makeText(
                        this@AddProductActivity,
                        "Falha na chamada da API",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        backBtn.setOnClickListener {
            startActivity(Intent(this@AddProductActivity, ProductListActivity::class.java))
        }

    }

    private fun initView() {
        backBtn = findViewById(R.id.backBtn)
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
