package com.example.ecoavalon.fragment.registration

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.ecoavalon.R
import com.example.ecoavalon.SucsessFul
import com.example.ecoavalon.databinding.FragmentVerificationCodeBinding
import com.example.ecoavalon.fragment.Profile
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import java.util.concurrent.TimeUnit

class VerificationCode : Fragment() {
  val binding by lazy { FragmentVerificationCodeBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    private var countDownTimer: CountDownTimer? = null
    private lateinit var profile : Profile
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


//        FirebaseAuth.getInstance().getFirebaseAuthSettings()
//            .setAppVerificationDisabledForTesting(true)
        profile = Profile()
        auth = FirebaseAuth.getInstance()
        countDown()
       binding.signUpNextBtn.setOnClickListener {
           findNavController().navigate(R.id.action_verificationCode_to_activity)
       }
        val number = arguments?.getString("numberKey").toString()
        binding.signUpThreeTitleTwo.text =
            "Biz sizning $number raqamingizga tastiqlash kodini yubordik."
        sendVerificationCode(number as String)
        binding.firstPinView.addTextChangedListener {
            if (it.toString().length==6){
                verifyCode()
            }
        }

        binding.recoveryCodeReplay.setOnClickListener {
            resentCode(number)
            countDown()
        }
        binding.signUpThreeTitleTwo.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                verifyCode()
                val view = requireActivity().currentFocus
                if (view != null) {
                    val imm: InputMethodManager =
                        requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            true
        }
        return binding.root
    }
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
        }
    }
    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
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
    @SuppressLint("CommitPrefEdits")
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
//                    val numbere = intent.getStringExtra("key")
//                    intent.putExtra("keys", numbere)
//                    val intent = Intent(this, SucsessFul::class.java)
//                    startActivity(intent)
////                intent.putExtra("uz", binding.code.registerCarrierNumberEditText(numbers))
//                    startActivity(intent)
//                    val bundle = Bundle()
//                    bundle.putString("number",auth.currentUser?.phoneNumber.toString())
                    val user = auth.currentUser?.phoneNumber.toString()
                    val sharedPreferences = requireActivity().getSharedPreferences("key", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userNumber",user)
                        .apply()
                    findNavController().navigate(R.id.action_verificationCode_to_activity)
                } else {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                    if (it.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(requireActivity(), "Code is wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    private fun resentCode(phoneNimber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNimber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}