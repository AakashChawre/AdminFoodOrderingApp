package com.adminfoodorderingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.adminfoodorderingapp.model.Item
import com.adminfoodorderingapp.screens.AddMenuActivity
import com.adminfoodorderingapp.screens.MenuItemActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private val cv_add_menu: CardView get() = findViewById(R.id.cv_add_menu)
    private val cv_show_menu_item: CardView get() = findViewById(R.id.cv_show_menu_item)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cv_add_menu.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddMenuActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        cv_show_menu_item.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MenuItemActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

    }
}