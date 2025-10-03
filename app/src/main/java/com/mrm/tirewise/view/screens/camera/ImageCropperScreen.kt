//package com.mrm.tirewise.view.screens.camera
//
//import android.app.Activity
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.os.Build
//import android.provider.MediaStore
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import com.canhub.cropper.CropImageContract
//import com.mrm.tirewise.viewModel.MainCameraViewModel
//
//@Composable
//fun ImageCropper(cameraViewModel: MainCameraViewModel, onSuccessCrop: () -> Unit) {
//
//    var bitmap: Bitmap? by remember { mutableStateOf(cameraViewModel.bitmap.value) }
////    var imageUri by remember { mutableStateOf(cameraViewModel.imageUri.value) }
//
//    val context = LocalContext.current as Activity
//
//    // assign the option to the crop launcher
//    val cropOptions = cameraViewModel.cropOptions
//
//
//    // The image crop launcher needs to be stored in a
//    // variable so it can be launched later.
//    val cropLauncher =
//        rememberLauncherForActivityResult(contract = CropImageContract())
//        { result ->
//            // Image crop was successful
//            if (result.isSuccessful) {
//                result.uriContent?.let {
//                    //getBitmap method is deprecated in Android SDK 29 or above so we need to do this check here
//                    // 1. Store the cropped image in a bitmap variable
//                    bitmap = if (Build.VERSION.SDK_INT < 28) {
//                        MediaStore.Images
//                            .Media.getBitmap(context.contentResolver, it)
//                    } else {
//                        val source = ImageDecoder
//                            .createSource(context.contentResolver, it)
//                        ImageDecoder.decodeBitmap(source)
//                    }
//                    // 2. Store the bitmap in the view model
//                    cameraViewModel.assignBitmap (bitmap!!)
//                    // 3. Navigate back to the camera screen
//
////                    onSuccessCrop()
//                }
//            } else {
//                // TODO handle the IMAGE CROP error here
//                println("ImageCropping error: ${result.error}")
//            }
//        }
////    val cor = rememberCoroutineScope()
////        cor.launch {
//
////    }
//    // The UI for the image cropping tool
//    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
//        // launch the image cropping tool
//        cropLauncher.launch(cropOptions)
//
//        // launch the image cropping tool
////        cropLauncher.launch(cropOptions)
//
//        // After cropping, display the image
//        Box(modifier = Modifier
//            .padding(paddingValues)
//            .fillMaxSize()) {
//            bitmap?.let {
//                Image(
//                    bitmap = it.asImageBitmap(),
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(0.8f)
//                )
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun MyPreview() {
////    ImageCropper( bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
//}