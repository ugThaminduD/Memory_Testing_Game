package com.example.memory_testing_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LogoActivity : AppCompatActivity() {

    private lateinit var getStartedbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        getStartedbtn = findViewById(R.id.getStartedButton)
        getStartedbtn.setOnClickListener {
            open_GameMain()
        }

    }
    private fun open_GameMain(){
        val intent = Intent(this, GameMainActivity::class.java)
        startActivity(intent)
    }

}