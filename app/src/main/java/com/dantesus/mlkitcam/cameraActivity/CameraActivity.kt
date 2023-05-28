package com.dantesus.mlkitcam.cameraActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.dantesus.mlkitcam.R
import com.dantesus.mlkitcam.helpers.ImageAnaliser
import com.google.common.util.concurrent.ListenableFuture

class CameraActivity: AppCompatActivity() {
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraFuture: ListenableFuture<ProcessCameraProvider>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        cameraFuture = ProcessCameraProvider.getInstance(this)
        cameraFuture.addListener({
            cameraProvider = cameraFuture.get()
            bindPreview()
        },ContextCompat.getMainExecutor(this))
    }
    private fun bindPreview(){
        val preview = Preview.Builder().build()
        val surface = findViewById<PreviewView>(R.id.previewview)
        preview.setSurfaceProvider(surface.surfaceProvider)

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val analizer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val landmarkView = findViewById<LandmarkView>(R.id.landmark_view)
        analizer.setAnalyzer(
            mainExecutor,
            ImageAnaliser(landmarkView)
        )

        cameraProvider?.bindToLifecycle(this,cameraSelector,preview, analizer)
    }
}