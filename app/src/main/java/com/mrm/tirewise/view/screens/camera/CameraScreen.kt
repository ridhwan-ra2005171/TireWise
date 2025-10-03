package com.mrm.tirewise.view.screens.camera

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.canhub.cropper.CropImageContract
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mrm.tirewise.R
import com.mrm.tirewise.view.reusablecomponents.CameraPermissionDialog
import com.mrm.tirewise.viewModel.CameraViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel,
    onConfirmPhoto: () -> Unit,
//    callCropTool: (Boolean) -> Unit, // A boolean callback to let the app know when to navigate to image crop screen
//    onOpenGallery: () -> Unit
) {

    val camerPermission = rememberPermissionState(Manifest.permission.CAMERA,
        // Permission required for photo album access depending on Android version
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//           Manifest.permission.READ_MEDIA_IMAGES
//        } else {
//            Manifest.permission.READ_EXTERNAL_STORAGE },
    )

    val context = LocalContext.current

    val showRationalDialog = remember { mutableStateOf(false) }
    if (showRationalDialog.value) {
        CameraPermissionDialog(showRationalDialog = showRationalDialog, permission = camerPermission, context = context)
    }

    val allPermissionsGranted = camerPermission.status.isGranted

    LaunchedEffect(key1 = Unit ) {
        if (!camerPermission.status.isGranted) {
            camerPermission.launchPermissionRequest()
        } else {
//            Toast.makeText(
//                context,
//                "We have camera and photo album permission",
//                Toast.LENGTH_SHORT
//            ).show()
            showRationalDialog.value = false
        }
    }

    // We will check the camera and gallery permissions here
    CameraScreenContent(
        cameraViewModel = cameraViewModel,
        onConfirmPhoto = onConfirmPhoto,
        showPermissionDialog = { showRationalDialog.value = it },
        allPermissionsGranted = allPermissionsGranted
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onDismiss: () -> Unit, onRetakePhoto: () -> Unit, onConfirmPhoto: () -> Unit, imageBitmap: Bitmap, coroutineScope: CoroutineScope) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
//        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        PhotoBottomSheetContent(
            bitmap = imageBitmap,
            modifier = Modifier
                .padding(vertical = 50.dp)
                .fillMaxSize(),
            onRetakePhoto = { coroutineScope.launch{modalBottomSheetState.hide() }; onRetakePhoto() },
            onConfirmPhoto = onConfirmPhoto
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable
fun CameraScreenContent(
    cameraViewModel: CameraViewModel,
    onConfirmPhoto: () -> Unit = {},
    showPermissionDialog: (Boolean) -> Unit,
    allPermissionsGranted: Boolean,
) {
    val context: Context = LocalContext.current
    // Coroutine scope used to open the bottom sheet
    val coroutineScope = rememberCoroutineScope()

    var showSheet by remember { mutableStateOf(false) }

    // Open the bottom sheet
    AnimatedVisibility (showSheet) {
        BottomSheet(
            onDismiss = { showSheet = false },
            onRetakePhoto = { showSheet = false},
            onConfirmPhoto = onConfirmPhoto,
            imageBitmap = cameraViewModel.bitmap.value,
            coroutineScope = coroutineScope)
    }

    // Bitmap from View Model
    val bitmap by cameraViewModel.bitmap.collectAsState()

    // Image URI from View Model
    var imageUri by remember {
        mutableStateOf(cameraViewModel.imageUri.value)
    }

    /// CAMERA RELATED LOGIC ============================ ///

    // camera controller must be called in an activity
    val cameraController: LifecycleCameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                // We set the camera X use-cases here i.e. Video capture , Image capture, or Image Analysis use-case
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    /// =============================================== ///

    /// GALLERY RELATED LOGIC ========================= ///

    // The image crop launcher needs to be stored in a
    // variable so it can be launched later.
    val cropLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
            // Image crop was successful
            if (result.isSuccessful) {
                result.uriContent?.let {
                    //getBitmap method is deprecated in Android SDK 29 or above so we need to do this check here
                    // 1. Store the cropped image in a bitmap variable
                    val tempBitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images
                            .Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }
                    // 2. Store the bitmap in the view model
                    cameraViewModel.assignBitmap(tempBitmap!!)
                    cameraViewModel.assignUri(it)
                    // 3. Open the bottom sheet
                    showSheet = true
                }
            } else {
                // TODO handle the IMAGE CROP error here
                println("ImageCropping error: ${result.error}")
            }
        }


    /// ----------------------------------------------------///
    // When an image is chosen from the gallery the if statement will get executed:
    if (imageUri != null) {

        Log.d("__URI", "CameraScreen: imageUri not null : $imageUri")
        val bitmapTemp = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images
                .Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, imageUri!!)
            ImageDecoder.decodeBitmap(source)
        }

        cameraViewModel.assignBitmap(bitmapTemp)
        cameraViewModel.assignUri( imageUri!! )
        Log.d("__BITMAP", "CameraScreen: after assigning bitmap : $bitmap")

        Log.d("__CROP", "CameraScreen: after calling Crop image: $bitmap")
        imageUri = null
    }
    /// ==================================== ///

    //// Flashlight related logic ////
    //Flashlight state(Boolean): TRUE or FALSE
    val flashState = remember{
        //set initial state to false
        mutableStateOf(false)
    }

    if (!allPermissionsGranted) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background).padding(30.dp) ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "The app needs to access your camera. Please grant the permission.",
                fontWeight = FontWeight.Medium, textAlign = TextAlign.Center
            )

            ElevatedButton(
                modifier = Modifier.padding(10.dp),
                onClick = { showPermissionDialog(true) }) {
                Text(text = "Grant Permission")
            }
        }
        return
    }

    Scaffold(
        floatingActionButton = {
            if (flashState.value) {
                try {
                    cameraController.cameraControl?.enableTorch(true).also {
                            Log.d( "__FLASH", "flash on.")
                        }
                } catch (e: Exception) { e.printStackTrace() }
            } else {
                try {
                    cameraController.cameraControl?.enableTorch(false).also {
                            Log.d( "__FLASH", "flash off")
                        }
                } catch (e: Exception) { Log.d("__FLASH", "Error: ${e.message}") }
            }

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                //// Flash mode button ////
                IconButton(
                    // Set Flash Mode
                    onClick = { flashState.value = !flashState.value }
                ) {
                    Icon(tint = if (flashState.value) MaterialTheme.colorScheme.primary else Color.White,
                        painter = painterResource(
                            id = if (flashState.value) R.drawable.ic_flash_on else R.drawable.ic_flash_off
                        ),
                        modifier = Modifier.padding(top = 5.dp).size(32.dp),
                        contentDescription = null
                    )
                }

                //// Take Picture button ////
                ElevatedButton(
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.Black
                    ),
                    // Method for taking a picture
                    onClick = {
                        takePicture(context, cameraController, cameraViewModel::assignBitmap)
                        // Open bottom sheet after taking a picture
                        coroutineScope.launch {
                            delay(500)
                            showSheet = true
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .size(74.dp)
                        .border(
                            width = 4.5.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
//                        .background(
//                            color = Color.Black,
//                            shape = CircleShape
//                        ),
                ) {}
                //// Open Gallery button ////
                IconButton(
                    onClick = {
                        // launch the image cropping tool
                        cropLauncher.launch(cameraViewModel.cropOptions)
                        // Expand the bottom sheet
//                        showSheet = true
                    }) {
                    Icon(tint = Color.White,
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = R.drawable.icon_iamge),
                        contentDescription = null
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // This Android view is used to display the camera preview (i.e. opens the camera)
                CameraPreview(
                    controller = cameraController,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }
        }
}

//fun convertUriToBitmap(appContext: Context, imageUri: Uri): Bitmap {
//    val contentResolver: ContentResolver = appContext.contentResolver
//    return try {
//        if (Build.VERSION.SDK_INT < 28) {
//            return MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//        } else {
//            val source = ImageDecoder.createSource(contentResolver, imageUri)
//            return ImageDecoder.decodeBitmap(source)
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
//    }
//}

fun takePicture(appContext: Context, controller: LifecycleCameraController, onPictureTaken: (Bitmap) -> Unit) {

    // most CameraX use-cases/methods need executors
    // Executor contains information about which thread the function will be executed on
    //
    controller.takePicture(
        ContextCompat.getMainExecutor(appContext),
        object : OnImageCapturedCallback() {
            //^^ Image Proxy contains additional information for the image like rotation
            // When the take picture was successful:
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                // Matrix to handle the photo rotation depending on the screen rotation of the device
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                // Our function to handle the picture taken
                onPictureTaken(rotatedBitmap)
            }

            // When the take picture was NOT successful:
            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take picture: $exception")
            }
        }
    )
}


@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    // This Android view is used to display the camera preview (i.e. the open the camera)
    AndroidView(
        factory = {
            // Create a PreviewView and set it up to take photos
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
//                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        modifier = modifier,
    )
}

// Overlay
//@Composable
//fun TransparentClipLayout2(
//    modifier: Modifier,
//    width: Dp,
//    height: Dp,
//    offsetY: Dp,
//) {
//
//    val offsetInPx: Float
//    val widthInPx: Float
//    val heightInPx: Float
//
//    with(LocalDensity.current) {
//        offsetInPx = offsetY.toPx()
//        widthInPx = width.toPx()
//        heightInPx = height.toPx()
//    }
//
//    Canvas(modifier = modifier) {
//
//        val canvasWidth = size.width
//
//        with(drawContext.canvas.nativeCanvas) {
//            val checkPoint = saveLayer(null, null)
//
//            // Destination
//            drawRect(Color(0x77000000))
//
//            // Source
//            drawRoundRect(
//                // center the offset of the square
//                topLeft =  Offset(
//                    x = (canvasWidth - widthInPx) / 2,
//                    y = ((size.height)/4)
//                ),
////                topLeft = Offset(
////                    x = (canvasWidth - widthInPx) / 2,
////                    y = offsetInPx
////                    y = 0f
////                ),
//                size = Size(widthInPx, heightInPx),
//                cornerRadius = CornerRadius(50f,50f),
//                color = Color.Transparent,
//                blendMode = BlendMode.Clear
//            )
//            restoreToCount(checkPoint)
//        }
//
//    }
//}
