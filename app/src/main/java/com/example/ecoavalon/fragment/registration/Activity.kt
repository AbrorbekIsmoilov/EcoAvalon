package com.example.ecoavalon.fragment.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.ecoavalon.R
import com.example.ecoavalon.databinding.FragmentActivityBinding
import com.example.ecoavalon.fragment.Chat
import com.example.ecoavalon.fragment.Home
import com.example.ecoavalon.fragment.Product
import com.example.ecoavalon.fragment.Profile


class Activity : Fragment() {
 val binding by lazy { FragmentActivityBinding.inflate(layoutInflater) }
    private val homeFragment  = Home()
    private val productFragment = Product()
    private val chatFragment = Chat()
    private val profilFragment = Profile()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val navHostFragment = childFragmentManager.findFragmentById(R.id.patient_container) as NavHostFragment
//
//        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
//            val navController = navHostFragment.navController
//
//            when (menuItem.itemId) {
//                R.id.menu_home -> navController.navigate(R.id.home)
//                R.id.destination2 -> navController.navigate(R.id.destination2)
//                // Add more cases for each menu item destination
//
//                // Return true to indicate that the item has been selected
//                // and should be highlighted by the bottom navigation view
//                else -> true
//            }
//        }
        val number = arguments?.get("number")

        binding.bottomNavigation.background=null
        replaceFragment(homeFragment)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home_nav_graph -> replaceFragment(homeFragment)
                R.id.product_nav_graph -> replaceFragment(productFragment)
                R.id.chat_nav_graph -> replaceFragment(chatFragment)
                R.id.profile_nav_graph -> replaceFragment(profilFragment)
                else -> {
                    replaceFragment(homeFragment)
                }
            }
        }

        return binding.root
    }
    private fun replaceFragment(fragment : Fragment): Boolean {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.patient_container, fragment)
        transaction.commit()
        return true
    }
}