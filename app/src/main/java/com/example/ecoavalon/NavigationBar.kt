package com.example.ecoavalon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecoavalon.databinding.ActivityNavigationBarBinding
import com.example.ecoavalon.fragment.Chat
import com.example.ecoavalon.fragment.Home
import com.example.ecoavalon.fragment.Product
import com.example.ecoavalon.fragment.Profile

class NavigationBar : AppCompatActivity() {
    val binding by lazy { ActivityNavigationBarBinding.inflate(layoutInflater) }

    private val homeFragment  = Home()
    private val productFragment = Product()
    private val chatFragment = Chat()
    private val profileFragment = Profile()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.patient_container)
        binding.bottomNavigation.setupWithNavController(navController)

    }
}