package com.dantesus.mlkitcam.helpers

import android.annotation.SuppressLint
import android.util.Size

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.dantesus.mlkitcam.cameraActivity.LandmarkView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import java.lang.Integer.min
import java.lang.Math.max

class ImageAnaliser(
    private  val view: LandmarkView
): ImageAnalysis.Analyzer {
    private  val options = AccuratePoseDetectorOptions
        .Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
        .build()
    private  val detector = PoseDetection.getClient(options)
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val frameImage = image.image
        if (frameImage != null){
            val imageForDetector =  InputImage.fromMediaImage(frameImage, image.imageInfo.rotationDegrees)
            val task =  detector.process(imageForDetector)
            task
                .addOnSuccessListener {
                    it.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
                    val size = Size(
                        min(image.width, image.height),
                        max(image.width, image.height)
                    )
                    view.setPose(it, size)
                    image.close()
                }
                .addOnFailureListener {
                    image.close()
                }
        }
    }
}