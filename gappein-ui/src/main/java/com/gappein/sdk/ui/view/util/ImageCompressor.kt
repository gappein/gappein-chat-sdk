package com.gappein.sdk.ui.view.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ImageCompressor(context: Context) {

    companion object {

        private const val maxWidth = 612
        private const val maxHeight = 816
        private val compressFormat = CompressFormat.JPEG
        private const val quality = 50
        private const val FOLDER = "images"
    }

    private val destinationDirectoryPath: String =
        context.cacheDir.path + File.separator + FOLDER

    @Throws(IOException::class)
    fun compressToFile(imageFile: File): File? {
        return compressToFile(imageFile, imageFile.name)
    }

    @Throws(IOException::class)
    fun compressToFile(imageFile: File?, compressedFileName: String): File? {
        return imageFile?.let {
            compressImage(
                it, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName
            )
        }
    }

    @Throws(IOException::class)
    fun compressImage(
        imageFile: File,
        reqWidth: Int,
        reqHeight: Int,
        compressFormat: CompressFormat?,
        quality: Int,
        destinationPath: String?
    ): File? {
        var fileOutputStream: FileOutputStream? = null
        val file = File(destinationPath).parentFile
        if (!file.exists()) {
            file.mkdirs()
        }
        try {
            fileOutputStream = FileOutputStream(destinationPath)
            // write the compressed bitmap at the destination specified by destinationPath.
            decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight).compress(
                compressFormat,
                quality,
                fileOutputStream
            )
        } finally {
            fileOutputStream?.run {
                flush()
                close()
            }
        }
        return File(destinationPath)
    }

    @Throws(IOException::class)
    fun decodeSampledBitmapFromFile(imageFile: File, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFile.absolutePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        var scaledBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)

        //check the rotation of the image and display it properly
        val exif: ExifInterface = ExifInterface(imageFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
        val matrix = Matrix()
        if (orientation == 6) {
            matrix.postRotate(90f)
        } else if (orientation == 3) {
            matrix.postRotate(180f)
        } else if (orientation == 8) {
            matrix.postRotate(270f)
        }
        scaledBitmap = Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )
        return scaledBitmap
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}