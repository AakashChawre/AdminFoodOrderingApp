package com.adminfoodorderingapp.screens

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import com.adminfoodorderingapp.MainActivity
import com.adminfoodorderingapp.R
import com.adminfoodorderingapp.databinding.ActivitySignUpBinding
import com.adminfoodorderingapp.model.UserDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    val act_list_of_location: AutoCompleteTextView get() = findViewById(R.id.act_location)

    private lateinit var location: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = Firebase.auth

        // initialize Firebase Database
        database = Firebase.database.reference


        val locations = arrayOf("Indore", "Dewas", "Ujjen", "Kannod", "Bahndariya", "Bhopal")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, locations)
        act_list_of_location.setAdapter(adapter)

        binding.tvHaveAccount.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentLogin)
            finish()
        }


        binding.acbSignUp.setOnClickListener {
            name = binding.edtName.text.toString()
            email = binding.edtEmail.text.toString()
            password = binding.edtPassword.text.toString()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Enter valid input...", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                saveUserDetail()
                updateUI()

            } else {
                Toast.makeText(this, "Account creation failed.", Toast.LENGTH_SHORT).show()
                Log.d("account", "Failed : ${task.exception!!.message}")
            }
        }
    }

    private fun saveUserDetail() {
        name = binding.edtName.text.toString()
        email = binding.edtEmail.text.toString()
        password = binding.edtPassword.text.toString()

        // save user in database
        val user = UserDetails(name, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }

    private fun updateUI() {
        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }
}