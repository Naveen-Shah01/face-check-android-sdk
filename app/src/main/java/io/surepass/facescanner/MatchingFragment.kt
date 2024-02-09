package io.surepass.facescanner

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import io.surepass.facescanner.databinding.FragmentMatchingBinding
import io.surepass.facescanner.databinding.FragmentPreviewBinding


class MatchingFragment : Fragment() {

    val args: MatchingFragmentArgs by navArgs()

    private var _binding: FragmentMatchingBinding? = null
    private val binding get() = _binding!!

    //TODO handle the errors properly
    //TODO handle the retake button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchingBinding.inflate(inflater, container, false)


        val imageUriString = args.imageUri
        val imageUri = Uri.parse(imageUriString)

        if (imageUri != null) {
            binding.capturedImage.setImageURI(imageUri)
            binding.availableImage.setImageURI(imageUri)
        } else {

            binding.nextbtn.visibility = View.GONE
            binding.btAdd.visibility=View.GONE
            binding.btRetake.visibility=View.VISIBLE
        }
        return binding.root
    }
}