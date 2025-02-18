package com.example.composeanimations.utils

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager

object FlashLightManager {

    private var cameraManager: CameraManager? = null
    private var cameraID: String? = null

    fun init(context: Context?) {
        if (cameraManager == null) cameraManager =
            context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        // Exception is handled to check whether the camera resource is being used by another service or not
        try {
            // 0 means back camera unit, 1 means front camera unit
            cameraID = cameraManager?.cameraIdList?.get(0)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    fun toggleFlashLight(turnOnFlash: Boolean) {
        try {
            cameraID?.let { id ->
                cameraManager?.setTorchMode(id, turnOnFlash)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}