package com.mrm.tirewise.utils

import android.graphics.Bitmap
import android.util.Log
import com.mrm.tirewise.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder


const val INPUT_SIZE = 224 // Size of the input image for the model

enum class ModelLabels(val label: String) {
    BAD("Bad"),
    MODERATE("Moderate"),
    GOOD("Good")
}

fun classifyImage(bitmap: Bitmap, context: android.content.Context) : String {
    // Calling model
    Log.d ("__TFLITE", "Call model " )
    val model = Model.newInstance(context)

    // 1. Prepare image for the model
    // Change the image resolution to 224 x 224
    val image = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)

    // 2. Convert the image to a TensorBuffer
    val inputFeature0 = convertBitmapToTensorBuffer(image)

    // Runs model inference and gets result.
    val outputs = model.process(inputFeature0)
    val outputFeature0 = outputs.outputFeature0AsTensorBuffer

    val result = getClass(outputFeature0)
    Log.d( "__TFLITE", "classifyImage: $result")
    // Releases model resources if no longer used.
    model.close()

    return result
}


private fun convertBitmapToTensorBuffer(image: Bitmap): TensorBuffer {

    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

    val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * 3)
    byteBuffer.order(ByteOrder.nativeOrder())

    val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
    // Convert the hardware bitmap to software bitmap
    val image = image.copy(Bitmap.Config.ARGB_8888, false)

    image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
    var pixel = 0
    //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
    //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
    for (i in 0 until INPUT_SIZE) {
        for (j in 0 until INPUT_SIZE) {
            val `val` = intValues[pixel++] // RGB
            byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255))
            byteBuffer.putFloat((`val` shr 8 and 0xFF)  * (1f / 255))
            byteBuffer.putFloat((`val` and 0xFF)        * (1f / 255))
        }
    }
    inputFeature0.loadBuffer(byteBuffer);

    return inputFeature0
}

fun getClass(outputFeature0: TensorBuffer) : String {
    val confidences: FloatArray = outputFeature0.floatArray
    // find the index of the class with the biggest confidence.
    // find the index of the class with the biggest confidence.
    var maxPos = 0
    var maxConfidence = 0f
    for (i in confidences.indices) {
        if (confidences[i] > maxConfidence) {
            maxConfidence = confidences[i]
            maxPos = i
        }
    }
    val classes = arrayOf(ModelLabels.BAD.label, ModelLabels.MODERATE.label, ModelLabels.GOOD.label)
    return classes[maxPos]

}