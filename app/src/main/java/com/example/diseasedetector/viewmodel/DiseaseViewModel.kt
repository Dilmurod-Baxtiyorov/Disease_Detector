package com.example.diseasedetector.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.diseasedetector.R
import com.example.diseasedetector.data.model.OrganInfo
import com.example.diseasedetector.ml.Model
import kotlinx.coroutines.flow.asStateFlow
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import androidx.core.graphics.scale
import com.example.diseasedetector.ml.ModelTb

class DiseaseViewModel(application: Application) : AndroidViewModel(application) {
    private val _organs = listOf(
        OrganInfo(
            id = 1,
            name = "Lung",
            imageResId = R.drawable.or_lung,
            diseases = listOf("Tuberculosis", "Pneumonia", "Asthma")
        ),
        OrganInfo(
            id = 2,
            name = "Kidney",
            imageResId = R.drawable.or_kidney,
            diseases = listOf("Kidney Stones", "Nephritis")
        ),
        OrganInfo(
            id = 3,
            name = "Liver",
            imageResId = R.drawable.or_liver,
            diseases = listOf("Hepatitis", "Fatty Liver")
        ),
        OrganInfo(
            id = 4,
            name = "Heart",
            imageResId = R.drawable.or_heart,
            diseases = listOf("Heart Attack", "Arrhythmia", "Hypertension")
        ),
        OrganInfo(
            id = 5,
            name = "Brain",
            imageResId = R.drawable.or_brain,
            diseases = listOf("Stroke", "Alzheimer's Disease", "Brain Tumor")
        ),
        OrganInfo(
            id = 6,
            name = "Hand",
            imageResId = R.drawable.or_hand,
            diseases = listOf("Arthritis", "Carpal Tunnel Syndrome", "Tendinitis")
        )
    )

    val organList: List<OrganInfo> get() = _organs

    private val _isChatSelected = MutableStateFlow<Boolean?>(null)
    val isChatSelected: StateFlow<Boolean?> = _isChatSelected

    fun setChatSelected(selected: Boolean) {
        _isChatSelected.value = selected
    }

    fun initializeIfNeeded(isChatStart: Boolean) {
        if (_isChatSelected.value == null) {
            _isChatSelected.value = isChatStart
        }
    }
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    fun setImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun clearImageUri() {
        _selectedImageUri.value = null
    }

    fun pneumoniaClassify(context: Context, image: Bitmap): String {
        val model = Model.newInstance(context)

        val imageSize = 224
        val resizedImage = image.scale(imageSize, imageSize)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(imageSize * imageSize)
        resizedImage.getPixels(intValues, 0, imageSize, 0, 0, imageSize, imageSize)

        var pixelIndex = 0
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val pixel = intValues[pixelIndex++]
                val r = (pixel shr 16 and 0xFF).toFloat() / 255.0f
                val g = (pixel shr 8 and 0xFF).toFloat() / 255.0f
                val b = (pixel and 0xFF).toFloat() / 255.0f
                byteBuffer.putFloat(r)
                byteBuffer.putFloat(g)
                byteBuffer.putFloat(b)
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        model.close()

        val outputArray = outputFeature0.floatArray
        val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
        val labels = listOf("Normal", "Pneumonia")
        val confidence = outputArray[maxIndex] * 100

        return "${labels[maxIndex]} (%.2f%%)".format(confidence)
    }

    private val _pneumoniaResult = MutableStateFlow<String?>(null)
    val pneumoniaResult: StateFlow<String?> = _pneumoniaResult

    fun classifyPneumonia(context: Context, bitmap: Bitmap) {
        val result = pneumoniaClassify(context, bitmap)
        _pneumoniaResult.value = result
    }


    fun tuberculosisClassify(context: Context, image: Bitmap): String {
        val model = ModelTb.newInstance(context)

        val imageSize = 224
        val resizedImage = image.scale(imageSize, imageSize)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(imageSize * imageSize)
        resizedImage.getPixels(intValues, 0, imageSize, 0, 0, imageSize, imageSize)

        var pixelIndex = 0
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val pixel = intValues[pixelIndex++]
                val r = (pixel shr 16 and 0xFF).toFloat() / 255.0f
                val g = (pixel shr 8 and 0xFF).toFloat() / 255.0f
                val b = (pixel and 0xFF).toFloat() / 255.0f
                byteBuffer.putFloat(r)
                byteBuffer.putFloat(g)
                byteBuffer.putFloat(b)
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        model.close()

        val outputArray = outputFeature0.floatArray
        val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
        val labels = listOf("Normal", "Tuberculosis")
        val confidence = outputArray[maxIndex] * 100

        return "${labels[maxIndex]} (%.2f%%)".format(confidence)
    }


    private val _tbResult = MutableStateFlow<String?>(null)
    val tbResult: StateFlow<String?> = _tbResult

    fun classifyTuberculosis(context: Context, bitmap: Bitmap) {
        val result = pneumoniaClassify(context, bitmap)
        _tbResult.value = result
    }
}