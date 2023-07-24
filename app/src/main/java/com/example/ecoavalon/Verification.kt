package com.example.ecoavalon

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.ecoavalon.databinding.ActivityMainBinding
import com.example.ecoavalon.databinding.ActivityVerificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class Verification : AppCompatActivity() {
    private val binding by lazy { ActivityVerificationBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    private var countDownTimer: CountDownTimer? = null
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        countDown()
        val number = intent.getStringExtra("key")
//        FirebaseAuth.getInstance().firebaseAuthSettings
//            .setAppVerificationDisabledForTesting(true)
        binding.signUpThreeTitleTwo.text =
            "Biz sizning $number raqamingizga tastiqlash kodini yubordik."
        sendVerificationCode(number as String)
        binding.firstPinView.addTextChangedListener {
            if (it.toString().length==6){
                verifyCode()
            }
        }
//        if (auth.currentUser!=null){
//            startActivity(Intent(this,SucsessFul::class.java))
//        }
        binding.recoveryCodeReplay.setOnClickListener {
          resentCode(number)
            countDown()
        }

    binding.signUpThreeTitleTwo.setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE){
            verifyCode()
            val view = currentFocus
            if (view != null) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        true
    }}
    private fun countDown(){
        binding.signUpVerificationTimer.text = "00:59"
        countDownTimer = object : CountDownTimer(60000, 1000) { // 60 seconds countdown with 1 second interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.signUpVerificationTimer.text = "00:${seconds.toString()}"
            }

            override fun onFinish() {
                binding.recoveryCodeReplay.visibility = View.VISIBLE
            }
        }
        countDownTimer?.start()
    }
    private fun verifyCode() {
        val code = binding.firstPinView.text.toString()
        if (code.length == 6) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)
        }}
    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

            Log.w(ContentValues.TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {

            } else if (e is FirebaseTooManyRequestsException) {

            }


        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {

            Log.d(ContentValues.TAG, "onCodeSent:$verificationId")


            storedVerificationId = verificationId
            resendToken = token
        }
    }
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val numbere = auth.currentUser?.phoneNumber.toString()
                    intent.putExtra("keys", numbere)
                    val intent = Intent(this, SucsessFul::class.java)
                    startActivity(intent)
//                intent.putExtra("uz", binding.code.registerCarrierNumberEditText(numbers))
                startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this, "Code is wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    private fun resentCode(phoneNimber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNimber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}