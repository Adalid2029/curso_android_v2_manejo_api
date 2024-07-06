package com.curso.android.crudvideotutorialesretrofit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddVideoActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etPassword: EditText
    private lateinit var etSize: EditText
    private lateinit var ivImage: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnAddVideo: Button
    private var selectedImageUri: Uri? = null

    companion object {
        private const val TAG = "AddVideoActivity"
        private const val PERMISSION_REQUEST_CODE = 100
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video)
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        etPassword = findViewById(R.id.etPassword)
        etSize = findViewById(R.id.etSize)
        ivImage = findViewById(R.id.ivImage)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnAddVideo = findViewById(R.id.btnAddVideo)

        btnSelectImage.setOnClickListener {
            checkPermissionAndPickImage()
        }

        btnAddVideo.setOnClickListener(){
            uploadVideo()
        }
    }

    private fun uploadVideo() {

        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val password = etPassword.text.toString()
        val size = etSize.text.toString()

        if(title.isEmpty() || description.isEmpty() || password.isEmpty() || size.isEmpty() || selectedImageUri == null){
            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        val inputStream = contentResolver.openInputStream(selectedImageUri!!)
        val file = File(cacheDir, "temp_image")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestedFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestedFile)

        val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordPart = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val sizePart = size.toRequestBody("text/plain".toMediaTypeOrNull())

        val videoService = RetrofitClient.getClient("https://api.stacknews.xyz/").create(VideoService::class.java)

        Log.d(TAG, "Iniciando llamada a addVideo con título: $title")

        videoService.addVideo(titlePart, descriptionPart, passwordPart, sizePart, imagePart).enqueue(object:
            Callback<AddVideoResponse> {
            override fun onResponse(
                call: Call<AddVideoResponse>,
                response: Response<AddVideoResponse>
            ) {
                if(response.isSuccessful){
                    val message = response.body()?.message ?: "Video agregado exitosamente"
                    Log.d(TAG, "Respuesta exitosa: $message")
                    Toast.makeText(this@AddVideoActivity, message, Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    val error = response.errorBody()?.string()
                    Log.d(TAG, "Error en la respuesta. Código: ${response.code()}, Cuerpo: $error")
                    Toast.makeText(this@AddVideoActivity, "Error al agregar el video: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddVideoResponse>, t: Throwable) {
                Toast.makeText(this@AddVideoActivity, "Error al agregar el video tutorial", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun checkPermissionAndPickImage() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            } else {
                pickImageFromGallery()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                pickImageFromGallery()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            data?.data.let { uri ->
                selectedImageUri = uri
                ivImage.setImageURI(uri)
            } ?: run {
                Toast.makeText(
                    this,
                    "No se pudo obtener la imagen seleccionada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
