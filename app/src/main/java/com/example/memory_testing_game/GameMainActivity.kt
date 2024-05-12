package com.example.memory_testing_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class GameMainActivity : AppCompatActivity() {

    private lateinit var imgbutton1: ImageButton
    private lateinit var imgbutton2: ImageButton
    private lateinit var entName: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_main)

        imgbutton1 = findViewById(R.id.imageButton)
        imgbutton1.setOnClickListener {
            open_GamePlayMainActivity()
        }
        imgbutton2 = findViewById(R.id.imageButton2)
        imgbutton2.setOnClickListener {
            open_GamePlayMainActivity2()
        }

        entName = findViewById(R.id.entName)



    }

    private fun open_GamePlayMainActivity(){
        val name = entName.text.toString().trim()

        if (name.isNotEmpty()){
            Toast.makeText(this, "Welcome, $name", Toast.LENGTH_LONG).show()

            val intent = Intent(this, GamePlayMainActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "Please enter your Name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun open_GamePlayMainActivity2(){
        val name = entName.text.toString().trim()

        if (name.isNotEmpty()) {
            Toast.makeText(this, "Welcome, $name", Toast.LENGTH_LONG).show()

            val intent = Intent(this, GamePlayMainActivity2::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "Please enter your Name", Toast.LENGTH_SHORT).show()
        }
    }

}