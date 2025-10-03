package com.mrm.tirewise.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val DATE_FORMAT = "d-MMM-yyyy • hh:mm aa"


fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg",    /* suffix */
        externalCacheDir  /* directory */
    )
    return image
}

private fun generateFileName(imagePrefix: String): String {
    val timeStamp: String =
        SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
    return "${imagePrefix}_${timeStamp}_.jpg"
}


fun saveBitmapToFile(context: Context, bitmap: Bitmap) : Uri {
    var uri = Uri.EMPTY
    try {
        val contentResolver = context.contentResolver

        val fileName = generateFileName("bitmap")
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/")
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
        } else {
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val file = File(directory, fileName)
            contentValues.put(MediaStore.MediaColumns.DATA, file.absolutePath)
        }
        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
        contentResolver.openOutputStream(uri).use { output -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output!!) }
    } catch (e: java.lang.Exception) {
        Log.d("Error Converting Bitmap to URI", e.toString()) // java.io.IOException: Operation not permitted
    }
    return  uri
}

//fun convertBitmapToUri(context: Context, bitmap: Bitmap): Uri {
//    val imageUri = createMediaUri(context, "bitmap_image")
//    storeBitmapInFile(bitmap,  imageUri.toString())
//    return imageUri
//}