package io.surepass.facescanner

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import io.surepass.facescanner.databinding.FragmentCameraBinding
import io.surepass.facescanner.databinding.FragmentPreviewBinding


class PreviewFragment : Fragment() {


    //TODO handle the retake button
    //TODO add animation when moving to matching fragment

    val args: PreviewFragmentArgs by navArgs()

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)

        val imageUriString = args.imageUri
        val imageUri = Uri.parse(imageUriString)

        if (imageUri != null) {
            binding.previewImage.visibility = View.VISIBLE
            binding.previewImage.setImageURI(imageUri)
        } else {
            binding.previewImage.visibility = View.VISIBLE
            binding.nextbtn.visibility = View.GONE
        }

        binding.nextbtn.setOnClickListener { view: View ->

            val action =
                PreviewFragmentDirections.actionPreviewFragmentToMatchingFragment(imageUriString)
            Navigation.findNavController(view).navigate(action)

        }
        return binding.root
    }

}