package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast

import com.example.messenger.databinding.ActivitySignUpBinding
import com.example.messenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity(), TextWatcher {



    private lateinit var binding: ActivitySignUpBinding


    private val mAuth : FirebaseAuth by lazy {

        FirebaseAuth.getInstance()
    }

   private val fireStoreInstance : FirebaseFirestore by lazy {

         FirebaseFirestore.getInstance()
    }

    private val currentUserDocRef : DocumentReference
        get() = fireStoreInstance.document("Users/${mAuth.currentUser?.uid.toString()}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)




        binding.editTextNameSignUp.addTextChangedListener(this@SignUpActivity)
        binding.editTextEmailSignUp.addTextChangedListener(this@SignUpActivity)
        binding.editTextPasswordSignUp.addTextChangedListener(this@SignUpActivity)

        binding.btnSignUp.setOnClickListener{

            val name = binding.editTextNameSignUp.text.toString().trim()
            val email = binding.editTextEmailSignUp.text.toString().trim()
            val password = binding.editTextPasswordSignUp.text.toString().trim()

            if (name.isEmpty()){

                binding.editTextNameSignUp.error = "Name Required"
                binding.editTextNameSignUp.requestFocus()
                return@setOnClickListener

            }

            if (email.isEmpty()){

                binding.editTextEmailSignUp.error = "Email Required"
                binding.editTextEmailSignUp.requestFocus()
                return@setOnClickListener

            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                binding.editTextEmailSignUp.error = "Please enter a valid email"
                binding.editTextEmailSignUp.requestFocus()
                return@setOnClickListener

            }


            if (password.length < 6){

                binding.editTextPasswordSignUp.error = "6 char required"
                binding.editTextPasswordSignUp.requestFocus()
                return@setOnClickListener

            }

            createNewAccount(name,email,password)

        }

    }

    private fun createNewAccount(name: String, email: String, password: String) {

        binding.progressSignUp.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task ->

            val newUser = User(name , "")

            currentUserDocRef.set(newUser)

            if (task.isSuccessful){

                binding.progressSignUp.visibility = View.INVISIBLE

                val intentMainActivity = Intent(this@SignUpActivity,MainActivity::class.java)
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentMainActivity)

            }   else{

                binding.progressSignUp.visibility = View.INVISIBLE

                Toast.makeText(this@SignUpActivity,task.exception?.message,Toast.LENGTH_LONG).show()

            }



        }


    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        binding.btnSignUp.isEnabled = binding.editTextNameSignUp.text.trim().isNotBlank()
                && binding.editTextEmailSignUp.text.trim().isNotEmpty()
                && binding.editTextPasswordSignUp.text.trim().isNotEmpty()






    }

    override fun afterTextChanged(s: Editable?) {

    }
}