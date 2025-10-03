package com.mrm.tirewise.viewModel

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel : ViewModel() {
    // A variable to store the image in Bitmap format
    private var _bitmap = MutableStateFlow<Bitmap>(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
    val bitmap = _bitmap.asStateFlow()

    // A variable to store the image URI
    private var _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    // Store the crop options
    val cropOptions = CropImageContractOptions(
        uri = imageUri.value,
        CropImageOptions(
            allowFlipping = false,
            autoZoomEnabled = true,
            // fixing the aspect ratio to be 1:1 --
            aspectRatioX = 1,
            aspectRatioY = 1,
            fixAspectRatio = true,
            // -------------------------------------
            imageSourceIncludeCamera = false,
            activityBackgroundColor = Color.BLACK,
        )
    )


    fun assignBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun assignUri(uri: Uri) {
        _imageUri.value = uri
    }





}