package com.example.ecoavalon.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.ecoavalon.databinding.FragmentProfileBinding

class Profile : Fragment() {
    val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val sharedPreferences = requireActivity().getSharedPreferences("key", Context.MODE_PRIVATE)

        val numbers =sharedPreferences.getString("userNumber","").toString()
        binding.number.text = numbers

        val imagecatch = requireActivity().getSharedPreferences("image", Context.MODE_PRIVATE)

        val images =imagecatch.getString("userPhoto","").toString()
        binding.imageView.setImageURI(images.toUri())
        return binding.root
    }


}