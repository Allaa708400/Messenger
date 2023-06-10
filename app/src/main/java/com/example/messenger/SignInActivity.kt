package com.example.messenger

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.messenger.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity(), TextWatcher {

      private lateinit var binding: ActivitySignInBinding

    private val mAuth : FirebaseAuth by lazy {

        FirebaseAuth.getInstance()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding  = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.editTextEmailSignIn.addTextChangedListener(this@SignInActivity)
        binding.editTextPasswordSignIn.addTextChangedListener(this@SignInActivity)


        binding.btnSignIn.setOnClickListener {

            val email = binding.editTextEmailSignIn.text.toString()
            val password = binding.editTextPasswordSignIn.text.toString()


            if (email.isEmpty()){

                binding.editTextEmailSignIn.error = "Email Required"
                binding.editTextEmailSignIn.requestFocus()
                return@setOnClickListener

            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                binding.editTextEmailSignIn.error = "Please enter a valid email"
                binding.editTextEmailSignIn.requestFocus()
                return@setOnClickListener

            }

            if (password.length < 6){

                binding.editTextPasswordSignIn.error = "6 char required"
                binding.editTextPasswordSignIn.requestFocus()
                return@setOnClickListener

            }

            signIn(email , password)
        }


        binding.btnCreateAccount.setOnClickListener {

            val createNewAccountIntent = Intent(this,SignUpActivity::class.java)
           startActivity(createNewAccountIntent)

        }

    }



    private fun signIn(email : String , password : String) {

        binding.progressSignIn.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener {task ->

            if (task.isSuccessful){

                binding.progressSignIn.visibility = View.INVISIBLE

                val intentMainActivity = Intent(this@SignInActivity,MainActivity::class.java)
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentMainActivity)


            }else {

                binding.progressSignIn.visibility = View.INVISIBLE

                Toast.makeText(this@SignInActivity,task.exception?.message, Toast.LENGTH_LONG).show()

            }

        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser?.uid != null){

            val intentMainActivity = Intent(this@SignInActivity,MainActivity::class.java)
            startActivity(intentMainActivity)

        }
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        binding.btnSignIn.isEnabled = binding.editTextEmailSignIn.text.toString().trim().isNotEmpty()
                && binding.editTextPasswordSignIn.text.toString().trim().isNotEmpty()

    }

    override fun afterTextChanged(s: Editable?) {

    }
}