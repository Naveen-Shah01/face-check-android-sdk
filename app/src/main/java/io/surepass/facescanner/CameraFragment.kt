package io.surepass.facescanner

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.surepass.facescanner.camera.CameraManager
import io.surepass.facescanner.databinding.FragmentCameraBinding
import io.surepass.facescanner.face_detection.FaceStatus


class CameraFragment : Fragment() {

    //TODO handle permission otherwise camera will not open
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!


    private lateinit var cameraManager: CameraManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        createCameraManager()
        cameraPermission()

        binding.btnSwitch.setOnClickListener {
            cameraManager.changeCameraSelector()
        }

        binding.btTakePhoto.setOnClickListener { view: View ->
            //TODO handle the camera permission first
            cameraManager.takePhoto(view)

        }
        return binding.root
    }

    private fun openCamera() {
        cameraManager.startCamera()
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            requireContext(),
            binding.previewViewFinder,
            this,
            binding.graphicOverlayFinder,
            ::processPicture
        )
    }

    private fun processPicture(faceStatus: FaceStatus) {
        Log.e(Constants.FACE_TAG, "This is it ${faceStatus.name}")

        if (faceStatus == FaceStatus.MULTIPLE_FACES) {
            Log.e(Constants.FACE_TAG, "This is it ${faceStatus.name}")
        }

        if (faceStatus == FaceStatus.MULTIPLE_FACES ||
            faceStatus == FaceStatus.TOO_FAR ||
            faceStatus == FaceStatus.NOT_CENTERED
        ) {
            binding.btWarningText.visibility = View.VISIBLE
            binding.btTakePhoto.visibility = View.GONE
        } else {
            binding.btWarningText.visibility = View.GONE
        }

        //TODO put the string in string resource file, don't hardcode
        when (faceStatus) {
            FaceStatus.MULTIPLE_FACES -> {
                binding.btWarningText.text = "Multiple faces detected"
            }

            FaceStatus.TOO_FAR -> {
                binding.btWarningText.text = "Face is too Far"
            }

            FaceStatus.NOT_CENTERED -> {
                binding.btWarningText.text = "Face not centered"
            }

            FaceStatus.VALID -> {
                binding.btTakePhoto.visibility = View.VISIBLE
            }

            else -> {
                Log.e(Constants.FACE_TAG, "Face valid")
            }
        }
    }

    private fun cameraPermission() {
        val cameraPermission = android.Manifest.permission.CAMERA

        if (ContextCompat.checkSelfPermission(
                requireContext(), cameraPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), cameraPermission
            )
        ) {
            val title = "Permission Required"
            val message = "App needs Camera Permission to set capture image"

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle(title).setMessage(message).setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->

                    requestCameraPermissionLauncher.launch(cameraPermission)

                    dialog.dismiss()
                }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            builder.create().show()

        } else {
            requestCameraPermissionLauncher.launch(
                cameraPermission
            )
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.CAMERA
            )
        ) {
            val title = "Permission required"
            val message =
                "Please allow camera permission to capture images from settings to set profile picture."
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle(title).setMessage(message).setCancelable(false)
                .setPositiveButton("Change Settings") { _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        } else {
            cameraPermission()
        }
    }

}