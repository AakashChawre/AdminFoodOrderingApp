package com.adminfoodorderingapp.screens

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.adminfoodorderingapp.R
import com.adminfoodorderingapp.databinding.ActivityAddMenuBinding
import com.adminfoodorderingapp.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMenuBinding

    private lateinit var item_name: String
    private lateinit var item_price: String
    private lateinit var item_description: String
    private lateinit var item_ingredients: String
    private var item_image_uri: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // initialize Firebase Auth
        auth = Firebase.auth

        // initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        binding.btnAddItem.setOnClickListener {
            item_name = binding.edtName.text.toString().trim()
            item_price = binding.edtPrice.text.toString().trim()
            item_description = binding.edtDescription.text.toString().trim()
            item_ingredients = binding.edtIngredient.text.toString().trim()

            if ( item_name.isBlank() || item_price.isBlank() || item_description.isBlank() || item_ingredients.isBlank() ) {
                Toast.makeText(this, "Please fill details.", Toast.LENGTH_SHORT).show()
            }else{
                  addItem(item_name, item_price, item_description, item_ingredients)
            }
        }

        binding.ivAddImage.setOnClickListener {
            itemImage.launch("image/*")
        }

    }

    private val itemImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.ivItemImage.setImageURI(uri)
            item_image_uri = uri
        }
    }

    private fun addItem(
        itemName: String,
        itemPrice: String,
        itemDescription: String,
        itemIngredients: String
    ) {
        val menuRef = database.getReference("menu")
        val itemKey = menuRef.push().key

        if (item_image_uri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_image/${itemKey}.jpg")
            val uploadTask = imageRef.putFile(item_image_uri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    // Create menu item
                    val item = Item(
                        itemName,
                        itemPrice,
                        itemDescription,
                        itemIngredients,
                        downloadUrl.toString(),
                    )

                    itemKey.let { key ->
                        menuRef.child(key!!).setValue(item)
                            .addOnSuccessListener {Toast.makeText(this, "Data upload success full.", Toast.LENGTH_SHORT).show() }
                            .addOnFailureListener {Toast.makeText(this, "menuRef failure.", Toast.LENGTH_SHORT).show() }
                    }
                }
                    .addOnFailureListener { Toast.makeText(this, "Download Url failure.", Toast.LENGTH_SHORT).show() }
            }
                .addOnFailureListener {  Toast.makeText(this, "Upload task failure.", Toast.LENGTH_SHORT).show()   }
        }else{ Toast.makeText(this, "Please select item image.", Toast.LENGTH_SHORT).show() }

    }
}