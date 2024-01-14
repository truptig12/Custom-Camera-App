package com.hurix.cameraexample

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {
    val mGetUserByIdViewModel: GetUserByIdViewModel by viewModel()
    private var choosePhoto: ChoosePhoto? = null
    private lateinit var img: ImageView
    private lateinit var button: Button
    private lateinit var upload: Button
    private lateinit var repeat: Button
    private lateinit var textview: TextView
    private val textRecognizer: TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById<Button>(R.id.button)
        upload = findViewById<Button>(R.id.upload)
        repeat = findViewById<Button>(R.id.repeat)
        textview = findViewById<TextView>(R.id.textview)
        img = findViewById(R.id.img)
        button.setOnClickListener {
            choosePhoto = ChoosePhoto(this);
        }
        observeInsertUpdateResponse()
        upload.setOnClickListener {

            val original: Bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, choosePhoto!!.cropImageUrl)
            val resized = getResizedBitmap(original, 1200)
            val uri = getImageUri(this, resized!!)
            val finalFile = File(getRealPathFromURI(uri))

            val image = InputImage.fromBitmap(resized, 0)

            val result = textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Task completed successfully
                    textview.setText(visionText.text.toString())
                    repeat.visibility = View.VISIBLE
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    textview.setText(e.toString())
                    repeat.visibility = View.VISIBLE
                }

//            upload(finalFile)

        }

        repeat.setOnClickListener {
            val i = Intent(this@MainActivity, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }

    private fun upload(finalFile: File) {
        var fileReqBody: RequestBody? = null
        fileReqBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), finalFile!!)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", finalFile!!.name, fileReqBody)
        var paramObject = JSONObject()
        paramObject.put("image_text", finalFile!!.name)
        var requestBody = UpdateProfileImageRequest()
        requestBody.image_text = paramObject.toString()
        requestBody.authHeader =
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTU2LCJmaXJzdF9uYW1lIjoiU1dBUE5JTCBNT1JFIiwibGFzdF9uYW1lIjoiIiwiZG9iIjoiMDAwMC0wMC0wMCAwMDowMDowMCIsInNjaG9vbF9pZCI6MTczNiwic2Nob29sX25hbWUiOiJuZXcgZW5nbGlzaCBzY2hvb2wiLCJncmFkZV9uYW1lIjoiMTEiLCJncmFkZV9pZCI6IjAtNS04Iiwicm9tYW5fZ3JhZGUiOiJYSSIsImJvYXJkX25hbWUiOiJDQlNFIiwiYm9hcmRfaWQiOiIwLTMtMSIsImdlbmRlciI6IiIsInVzZXJfdHlwZSI6IiIsImVtYWlsIjoiIiwibW9iaWxlIjoiOTg3MDg3ODE4OCIsInJlZ2lzdHJhdGlvbmNvdW50cnkiOiIiLCJyZWdpc3RyYXRpb25yZWdpb24iOiIiLCJyZWdpc3RyYXRpb25wbGF0Zm9ybSI6IiIsImdvb2dsZXVzZXJpZCI6IiIsImZhY2Vib29rdXNlcmlkIjoiIiwiaXBhZGRyZXNzIjoiIiwic3RhdGUiOiIiLCJjaXR5IjoiIiwibGFzdGxvZ2luIjoiMjAyMS0wNi0wM1QxMDoyMDo0MS4wMDBaIiwib2xkX3VzZXJfaWQiOjM0NDQ4MzIsImNyZWF0ZWRfYXQiOiIyMDIxLTA2LTAzVDEwOjIwOjQxLjAwMFoiLCJ1cGRhdGVkX2F0IjoiMjAyMS0wNi0wM1QxMDoyMjowNC4wMDBaIiwidXNlcl9zZXR0aW5ncyI6W3sia2V5IjoiY291bnRyeSIsInZhbHVlIjoiSW5kaWEifSx7ImtleSI6ImNpdHkiLCJ2YWx1ZSI6IlRoYW5lIn1dLCJzdWJzY3JpcHRpb24iOnsicGxhbl9pZCI6IjEyMyIsInBsYW5fbmFtZSI6Im1zdmdvIGJhc2ljIiwib3JkZXJfaWQiOiJNU1YtMiIsInN0YXJ0X2RhdGUiOiIyMDIxLTA1LTE4IDAwOjAwOjAwIiwiZW5kX2RhdGUiOiIyMDIxLTA2LTAxIDIzOjU5OjU5IiwiaXNfZnJlZSI6MSwiZGlzcGxheV90ZXh0IjoiWW91IGFyZSBjdXJyZW50bHkgb24gdGhlIGZyZWUgcGxhbiB3aXRoIGxpbWl0ZWQgYWNjZXNzIG9uIHlvdXIgZmVhdHVyZXMiLCJwcmVtaXVtX2J1dHRvbiI6IlVQR1JBREUgVE8gbXN2Z28gQUNFIn0sImlhdCI6MTYyMzczOTgwMCwiZXhwIjoxNjIzOTEyNjAwfQ.XodQ6EjK4trDndDEOyNTCjJDScogEBG9RBmndfYFo_A"
        requestBody.image = body
        mGetUserByIdViewModel.updateProfileImage(requestBody)
    }

    fun getRealPathFromURI(uri: Uri?): String? {
        val cursor: Cursor = contentResolver.query(uri!!, null, null, null, null)!!
        cursor.moveToFirst()
        val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 50, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "TUV" + System.currentTimeMillis(),
                null
            )
        return Uri.parse(path)
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == ChoosePhoto.CHOOSE_PHOTO_INTENT) {
                if (data != null && data.data != null) {
                    choosePhoto!!.handleGalleryResult(data)
                } else {
                    choosePhoto!!.handleCameraResult(choosePhoto!!.cameraUri)
                }
            } else if (requestCode == ChoosePhoto.SELECTED_IMG_CROP) {
                img?.setImageURI(choosePhoto!!.cropImageUrl)
                button.visibility = View.GONE
                upload.visibility = View.VISIBLE

            } else {
                Toast.makeText(this, "Please take a picture again", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please take a picture again", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ChoosePhoto.SELECT_PICTURE_CAMERA) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) choosePhoto!!.showAlertDialog()
        }
    }

    private fun observeInsertUpdateResponse() {
        mGetUserByIdViewModel.updateProfileImageResponse()
            .observe(this, androidx.lifecycle.Observer { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this, "Image updated successfully", Toast.LENGTH_LONG).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, response.error?.message.toString(), Toast.LENGTH_LONG)
                            .show()
                        Log.e("error", response.error.toString())
                    }
                }
            })
    }

}