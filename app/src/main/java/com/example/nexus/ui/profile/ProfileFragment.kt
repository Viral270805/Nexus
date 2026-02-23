package com.example.nexus.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.nexus.databinding.FragmentProfileBinding
import com.example.nexus.utils.SessionManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private var profileImageUri: Uri? = null

    // Activity Result Launcher for picking an image
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let {
                profileImageUri = it
                binding.ivProfileImage.setImageURI(profileImageUri)
                // In a real app, you'd upload this URI to Firebase Storage
                Toast.makeText(context, "Profile image updated (demo)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load user data from session
        binding.tvProfileName.text = sessionManager.getUserName()
        binding.tvProfileEmail.text = sessionManager.getUserEmail()
        binding.tvProfileLocation.text = "Loading location..." // This will be updated by DashboardActivity

        binding.btnEditProfile.setOnClickListener {
            // In a real app, this would open an edit dialog or activity
            Toast.makeText(context, "Edit Profile Clicked (Demo)", Toast.LENGTH_SHORT).show()
        }

        binding.ivProfileImage.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(galleryIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}