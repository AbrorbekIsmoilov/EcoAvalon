package com.example.ecoavalon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ecoavalon.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
        val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.sendOTPBtn.setOnClickListener {
            val numbers = binding.phoneEditTextNumber
            if (numbers.text.length == 13) {

                val intent = Intent(this, Verification::class.java)
                intent.putExtra("key", binding.phoneEditTextNumber.text.trim().toString())
//                intent.putExtra("uz", binding.code.registerCarrierNumberEditText(numbers))
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "raqamingiz xato", Toast.LENGTH_SHORT).show()
            }
        }
    }
}