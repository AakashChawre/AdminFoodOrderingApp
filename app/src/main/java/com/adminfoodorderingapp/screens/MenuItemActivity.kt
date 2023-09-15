package com.adminfoodorderingapp.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adminfoodorderingapp.R
import com.adminfoodorderingapp.adapter.MenuAdapter
import com.adminfoodorderingapp.model.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuItemActivity : AppCompatActivity() {

    private val rv_menu_item :RecyclerView get() = findViewById(R.id.rv_menu_item)

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private val menuItems = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_item)
        // initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference

        // initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        getMenuItems()
    }

    private fun getMenuItems() {
        val foodRef : DatabaseReference = database.reference.child("menu")
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(Item::class.java)
                    menuItem.let {
                        menuItems.add(it!!)
                    }
                }

                showItems()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MenuItemActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showItems() {
        val adapter = MenuAdapter(this, menuItems,databaseReference)
        rv_menu_item.layoutManager = LinearLayoutManager(this)
        rv_menu_item.adapter = adapter
    }

}