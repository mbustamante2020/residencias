package com.residencias.es.utils


import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object ImageUtils {
    private const val imageTheFile: String = "pac3Image.jpg"
    private const val folder: String = "PEC3App";
    private var currentImage: String = ""


    fun hasImageInStorage(baseDir: File?): Boolean {
        return getImage(baseDir)!= null
    }


    fun getImage(baseDir: File?): File {
        val file = File(baseDir, this.folder+File.separator+this.imageTheFile)
        Log.d("PEC3", file.absolutePath)
        return (file);
    }

    @Throws(IOException::class)
    public fun createImageFile(baseDir: File?, bitmap: Bitmap) {
        Log.d("PAC3", "!!!!!!!!!!!!!!CREATE IMAGE FILE; :::: ")

        // Set Folder Path
        val storageDir = baseDir.toString() + File.separator + this.folder;
        val storageDirFile = File(storageDir.toString())

        storageDirFile.mkdirs()

        val imageFile = File(baseDir, this.folder+File.separator+this.imageTheFile)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val fos = FileOutputStream(imageFile)
        fos.write(stream.toByteArray())
        fos.close()
    }

}