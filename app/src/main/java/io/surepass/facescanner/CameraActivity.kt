package io.surepass.facescanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.surepass.facescanner.databinding.ActivityCameraBinding
import io.surepass.facescanner.databinding.ActivityPreviewBinding

class CameraActivity : AppCompatActivity() {


    //TODO delete file provider if cameraX is not using it
    //TODO warning if eyes are open or closed
    //TODO stop the cameraX properly when image clicked
    //TODO handle the camera permission properly
    //TODO if saving the image then ask the permission
    //TODO change buttons to Floating action buttons

    private lateinit var binding: ActivityCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}