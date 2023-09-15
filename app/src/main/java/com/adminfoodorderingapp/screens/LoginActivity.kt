package com.adminfoodorderingapp.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.adminfoodorderingapp.MainActivity
import com.adminfoodorderingapp.R
import com.adminfoodorderingapp.databinding.ActivityLoginBinding
import com.adminfoodorderingapp.model.UserDetails
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var name: String? = null
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = Firebase.auth

        // initialize Firebase Database
        database = Firebase.database.reference

        // initialize Google SignIn Option
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // initialize Google SignIn Client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.acbLogin.setOnClickListener {
//            location = binding.actLocation.toString()
            email = binding.edtEmail.text.toString()
            password = binding.edtPassword.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill details.", Toast.LENGTH_SHORT).show()
            } else {
                login(email, password)
            }
        }


        binding.tvDonTAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }


    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login successfully.", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                // saveUserDetail()
                updateUI(user)
            } else {
                Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show()
                Log.d("account", "Failed : ${task.exception!!.message}")
            }
        }
    }

    private fun saveUserDetail() {
        email = binding.edtEmail.text.toString()
        password = binding.edtPassword.text.toString()

        // save user in database
        val user = UserDetails(name, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        userId.let {
            database.child("user").child(userId).setValue(user)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(
            Intent(
                this,   MainActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }
}