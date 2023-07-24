package com.example.ecoavalon.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.ecoavalon.R
import com.example.ecoavalon.databinding.FragmentChatBinding
import com.example.ecoavalon.models.MyMessage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chat : Fragment() {
    private val binding by lazy { FragmentChatBinding.inflate(layoutInflater) }

    private lateinit var firebaseDataView:FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseDataView = FirebaseDatabase.getInstance()
        reference = firebaseDataView.getReference("message")
        binding.apply {
            btnSend.setOnClickListener {
                val id = reference.push().key
                val myMessage = MyMessage(id, edtMessage.text.toString())
                reference.child(id!!).setValue(myMessage)
                edtMessage.text.clear()
            }
        }
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return binding.root
    }

}