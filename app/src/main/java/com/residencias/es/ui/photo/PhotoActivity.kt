package com.residencias.es.ui.photo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.residencias.es.R
import com.residencias.es.data.network.Endpoints
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.photo.model.Photo
import com.residencias.es.databinding.ActivityPhotoBinding
import com.residencias.es.utils.ImageUtils
import com.residencias.es.viewmodel.PhotoViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

private const val REQUEST_IMAGE_CAPTURE = 1
private const val cameraRequestCode = 1888

class PhotoActivity : AppCompatActivity() {

    private val photoViewModel: PhotoViewModel by viewModel()

    private lateinit var binding: ActivityPhotoBinding

    private var canOpenCamera: Boolean = false

    private var photo: Photo? = null
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = intent
        photo = intent.getParcelableExtra("photo")

        val actionBar = supportActionBar
        actionBar?.title = "Fotografía"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary, null)))



        binding.btnDelete.setOnClickListener{
            deleteImage()
            onBackPressed()
            finish()
        }

        binding.btnSave.setOnClickListener { // open camera when click button
            uploadPhoto()
            onBackPressed()
            finish()
        }

        if( photo == null ) {
            openCamera()
        } else {

            binding.apply {

                title.setText("${photo!!.title}")
                description.setText("${photo!!.description}")

                principalImage.isChecked = photo!!.principal == 1

                photo!!.path.let {

                    Glide.with(this@PhotoActivity)
                        .load( "${Endpoints.urlImagen}/${it}-240x160.webp")
                        //.fitCenter()
                        .centerCrop()
                        .error(R.drawable.ic_no_available)
                        .into(imageResidence)
                }
            }
        }











        // check if mobile has camera
        if(checkCameraHardware(this)) {
            // check permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    cameraRequestCode
                )


            } else {
                // has permission
                canOpenCamera = true
            }
        }

    }


    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
    }

    private fun deleteImage() {
        try {
            photo?.let {
                photoViewModel.deletePhoto(it)
            }
            Toast.makeText(this, "Imagen eliminada", Toast.LENGTH_SHORT).show()
        } catch (t: UnauthorizedException) {
            //onUnauthorized()
        }
    }

    private fun uploadPhoto() {
        val principal = if(binding.principalImage.isChecked) 1 else 0
        val photo = Photo(photo?.id ?: 0, "", binding.title.text.toString(), binding.description.text.toString(), principal)

        if (imageFile?.exists() == true) {
            try {
                photoViewModel.uploadPhoto(imageFile, photo)
                Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show()
            } catch (t: UnauthorizedException) {
                //onUnauthorized()
            }
        } else {
            photoViewModel.updatePhoto(photo)
            Toast.makeText(this, "Imagen actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(hasPermission(Manifest.permission.CAMERA) && hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted
                canOpenCamera = true
            }
        }
    }

    private fun hasPermission(perm: String):Boolean {
        return(PackageManager.PERMISSION_GRANTED== this.let {
            ActivityCompat.checkSelfPermission(it, perm)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                val imageBitmap = data!!.extras?.get("data") as Bitmap

                val baseDir: File? = getExternalFilesDir(null)
                imageFile = ImageUtils.createImageFile(baseDir, imageBitmap)
                //binding.imageResidence.setImageBitmap(imageBitmap)

                Glide.with(this@PhotoActivity)
                    .load(imageFile)
                    .centerCrop()
                    .error(R.drawable.ic_no_available)
                    .into(binding.imageResidence)

                //Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show()
            } catch(ex:Error) {
                Toast.makeText(this, "The imaged couldn't be saved", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}