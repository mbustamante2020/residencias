package com.residencias.es.utils


import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object ImageUtils {
    private const val imageTheFile: String = "image.jpg"
    private const val folder: String = "temp"

    @Throws(IOException::class)
    fun createImageFile(baseDir: File?, bitmap: Bitmap) : File {
        // Set Folder Path
        val storageDir = baseDir.toString() + File.separator + this.folder
        val storageDirFile = File(storageDir)

        storageDirFile.mkdirs()

        val imageFile = File(baseDir, this.folder+File.separator+this.imageTheFile)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val fos = FileOutputStream(imageFile)
        fos.write(stream.toByteArray())
        fos.close()

        return imageFile
    }
}