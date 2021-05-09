package com.residencias.es.ui.photo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.residencias.es.databinding.FragmentPhotoBinding

private const val REQUEST_IMAGE_CAPTURE = 1
private const val cameraRequestCode = 1888

class PhotoFragment : Fragment() {


    private var canOpenCamera: Boolean = false
    private var photo: Uri? = null

   // private val profileViewModel: ProfileViewModel by viewModel()

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

/*

        val baseDir: File? = this.getExternalFilesDir(null);


        if(ImageUtils.hasImageInStorage(baseDir)) {
            // image exist in external storage
            // print last image stored
            binding.textNoImage?.visibility = View.INVISIBLE

            val filePath = ImageUtils.getImage(baseDir)
            val bit = BitmapFactory.decodeFile(filePath.toString())
            binding.imageThumbnail?.setImageBitmap(bit)
        }
        else {
            Log.d("PEC3", "hasn't image in storage!!")
        }

        binding.takePictureButton?.setOnClickListener { // open camera when click button
            // camera will open only if permission are accepted
            if (canOpenCamera) {
                Log.d("PEC3", "OPEN CAMERA!!!!!!!!!!!!")
                openCamera()
            }
        }

        // check if mobile has camera
        if(checkCameraHardware(this)) {
            // check permission
            /**
             * Ask for permission when application is running
             */
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

                Log.d("PEC3", "REQUEST PERMISSIONS!!!!")
                ActivityCompat.requestPermissions(
                        this,
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

        */













    }


/*

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY));
    }

    fun openCamera() {
        Log.d("PEC3", "OPEN CAMERA!!!!")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
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
        return(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(this, perm));
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("PEC3", "onActivityResult")

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d("PEC3", "decode the image!!!!!")
            binding.textNoImage?.visibility = View.INVISIBLE
            try {
                val imageBitmap = data!!.extras?.get("data") as Bitmap

                val baseDir: File? = this.getExternalFilesDir(null);
                ImageUtils.createImageFile(baseDir, imageBitmap);
                binding.imageThumbnail?.setImageBitmap(imageBitmap)

                Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show()
            } catch(ex:Error) {
                Toast.makeText(this, "The imaged couldn't be saved", Toast.LENGTH_SHORT).show()
            }

        }
    }

*/










}