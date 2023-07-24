package com.example.ecoavalon.fragment.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.ecoavalon.R
import com.example.ecoavalon.Verification
import com.example.ecoavalon.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class SignUp : Fragment() {
    val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isGallery()
        auth = FirebaseAuth.getInstance()
        val bundle = Bundle()
        binding.sendOTPBtn.setOnClickListener {
            val numbers = binding.phoneEditTextNumber
            if (numbers.text.length == 13) {
                bundle.putString("numberKey",binding.phoneEditTextNumber.text.trim().toString())

                findNavController().navigate(R.id.action_signUp_to_verificationCode,bundle)
            }
            else{
                Toast.makeText(context, "raqamingiz xato", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root

    }
    fun isGallery() {


        binding.imageView.setOnClickListener {
            getImageContent.launch("image/*")
        }
    }
    @SuppressLint("SimpleDateFormat")
    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()){
        it ?: return@registerForActivityResult
        binding.imageView.setImageURI(it)

        val inputStream = requireActivity().contentResolver.openInputStream(it)
        val vaqt = SimpleDateFormat("yyyyMMdd hh:mm:ss").format(Date())
        val file = File(requireActivity().filesDir, "$vaqt.jpg")
        val fileOutStream = FileOutputStream(file)
        inputStream?.copyTo(fileOutStream)
        fileOutStream.close()
        inputStream?.close()

    }

}