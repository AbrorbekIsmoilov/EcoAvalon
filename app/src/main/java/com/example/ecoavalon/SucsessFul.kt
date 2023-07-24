package com.example.ecoavalon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ecoavalon.databinding.ActivitySucsessFulBinding
import com.google.firebase.auth.FirebaseAuth

class SucsessFul : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val binding by lazy { ActivitySucsessFulBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucsess_ful)
        auth = FirebaseAuth.getInstance()

       binding.number.text = auth.currentUser?.phoneNumber.toString()
        binding.number.setOnLongClickListener {
            Toast.makeText(this, "Xuy ketasiz", Toast.LENGTH_SHORT).show()
            true
        }
    }
}