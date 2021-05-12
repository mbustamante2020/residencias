package com.residencias.es.ui.photo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.databinding.FragmentPhotoBinding
import com.residencias.es.utils.ImageUtils
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.PhotoViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File

private const val REQUEST_IMAGE_CAPTURE = 1
private const val cameraRequestCode = 1888

class PhotoFragment : Fragment() {


    private val photoViewModel: PhotoViewModel by viewModel()

    private var canOpenCamera: Boolean = false
    private var photo: Uri? = null

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        context?.let { context ->
            val baseDir: File? = context.getExternalFilesDir(null);


            if(ImageUtils.hasImageInStorage(baseDir)) {
                // image exist in external storage
                // print last image stored
                binding.textNoImage.visibility = View.INVISIBLE

                val filePath = ImageUtils.getImage(baseDir)
                val bit = BitmapFactory.decodeFile(filePath.toString())
                binding.imageThumbnail.setImageBitmap(bit)
            }
            else {
                Log.d("PEC3", "hasn't image in storage!!")
            }

            binding.takePictureButton.setOnClickListener { // open camera when click button
                // camera will open only if permission are accepted
                if (canOpenCamera) {
                    Log.d("PEC3", "OPEN CAMERA!!!!!!!!!!!!")
                    openCamera()
                }
            }






            // check if mobile has camera
            if(checkCameraHardware(context)) {
                // check permission
                /**
                 * Ask for permission when application is running
                 */
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

                    Log.d("PEC3", "REQUEST PERMISSIONS!!!!")
                    ActivityCompat.requestPermissions(
                        Activity(),
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        cameraRequestCode
                    )

                    Log.d("PEC4", "END REQUEST PERMISSIONS")

                } else {
                    // has permission
                    canOpenCamera = true
                }
            }



            observePhoto()

        }


    }


    private fun updatePhoto(file: File, headerValue: String) {
        // binding.progressBar.visibility = View.VISIBLE
        try {
            photoViewModel.updatePhoto(file, headerValue)
        } catch (t: UnauthorizedException) {
            //onUnauthorized()
        }

    }

    private fun observePhoto() {
        photoViewModel.photos.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText( context, "subida", Toast.LENGTH_SHORT ).show()
                    Log.i("observePhoto ----> ", "observePhoto subida")
                }
                Status.LOADING -> {
                    //Toast.makeText( this, "cargando", Toast.LENGTH_SHORT ).show()
                    //Log.i("getProvinces ----> ", "observerSearch ERROR")
                }
                Status.ERROR -> {
                    Toast.makeText( context, "error", Toast.LENGTH_SHORT ).show()
                    Log.i("observePhoto ----> ", "observePhoto ERROR")
                    /* if (adapter?.currentList.isNullOrEmpty()) {
                        Toast.makeText( this@ResidencesSearchActivity, getString(R.string.error_residences), Toast.LENGTH_SHORT ).show()
                    }*/
                }
            }
        })
    }

    private fun onUnauthorized() {
        // Clear local access token
        photoViewModel.onUnauthorized()
        // User was logged out, close screen and all parent screens and open login
        //finishAffinity()
        //startActivity(Intent(this, LoginActivity::class.java))
    }










    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
    }

    fun openCamera() {
        Log.d("PEC3", "OPEN CAMERA!!!!")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            context?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if(hasPermission(Manifest.permission.CAMERA) && hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted
                canOpenCamera = true
            }
        }

    }

    private fun hasPermission(perm: String):Boolean {
        return(PackageManager.PERMISSION_GRANTED== context?.let {
            ActivityCompat.checkSelfPermission(
                it, perm)
        });
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("PEC3", "onActivityResult")

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d("PEC3", "decode the image!!!!!")
            binding.textNoImage.visibility = View.INVISIBLE
            try {
                val imageBitmap = data!!.extras?.get("data") as Bitmap

                val baseDir: File? = context?.getExternalFilesDir(null);
                val file = ImageUtils.createImageFile(baseDir, imageBitmap);
                binding.imageThumbnail?.setImageBitmap(imageBitmap)





                //val bitmap = (image.getDrawable() as BitmapDrawable).getBitmap()
                val stream = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val imageByteArray = stream.toByteArray()


                //val imageByteArray = data!!.extras?.get("data") as ByteArray
                val headerValue = "residencia.jpg"
                updatePhoto(file, headerValue)





                Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show()
            } catch(ex:Error) {
                Toast.makeText(context, "The imaged couldn't be saved", Toast.LENGTH_SHORT).show()
            }

        }
    }

}