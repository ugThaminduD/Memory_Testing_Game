package com.example.memory_testing_game

import android.content.SharedPreferences
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GamePlayMainActivity2 : AppCompatActivity() {

    private val sequence = mutableListOf<Int>()
    private var playerIndex = 0
    private var score = 0
    private val HIGH_SCORE_KEY = "high_score"
    //    private val TURN_TIME_LIMIT_MS = 3000L
    //    private lateinit var turnTimer: CountDownTimer
    private lateinit var sharedPreferences: SharedPreferences


    private lateinit var button: List<Button>  //btn list
    private lateinit var buttonStart: Button
    private lateinit var buttonEnd: Button

    private lateinit var textViewScore: TextView
    private lateinit var textViewGameOver: TextView
//    private lateinit var textViewRemainingTime: TextView

    var name: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play_main2)

        sharedPreferences = getSharedPreferences("game_data", Context.MODE_PRIVATE)
        name = intent.getStringExtra("name")

        buttonStart = findViewById(R.id.button_start)
        buttonStart.setOnClickListener {
            startGame()
        }
        buttonEnd = findViewById(R.id.button_end)
        buttonEnd.setOnClickListener {
            endGame()
        }

        button = listOf(
            findViewById(R.id.button_1),
            findViewById(R.id.button_2),
            findViewById(R.id.button_3),
            findViewById(R.id.button_4),
            findViewById(R.id.button_5),
            findViewById(R.id.button_6),
            findViewById(R.id.button_7),
            findViewById(R.id.button_8),
            findViewById(R.id.button_9)
        )
        button.forEachIndexed{ index, button ->
            button.setOnClickListener{
                checkSequence(index + 1)
            }
        }

        textViewScore = findViewById(R.id.textViewScore)
        updateScoreDisplay(loadHighScore(), loadHighScore())

        textViewGameOver = findViewById(R.id.textViewGameOver)


    }


    private fun startGame() {
        sequence.clear()
        score = 0
        val highScore = loadHighScore()
        updateScoreDisplay(score, highScore)
//        updateScoreDisplay(score, loadHighScore())
        showNextSequence()

        textViewGameOver.visibility = View.INVISIBLE
    }


    private fun showNextSequence() {
        playerIndex = 0
        sequence.add(Random.nextInt(9) + 1)
//        updateTurnTimer()
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)                         // Delay showing next sequence
            for (index in sequence.indices) {
                displayTile(sequence[index])
                delay(1000)           // Time to display each tile
            }
        }
    }


    private fun displayTile(tileIndex: Int) {            // Display

        val button = button[tileIndex - 1]

        val originalColor = button.backgroundTintList?.defaultColor    // Change color of tile
        val newColor = when {
            score >= 10 && score % 10 == 0 -> getRandomColor()
            else -> Color.YELLOW
        }
        button.setBackgroundColor(newColor)

        CoroutineScope(Dispatchers.Main).launch {
            delay(500)                                       // delay
            button.setBackgroundColor(originalColor ?: Color.WHITE)  // Restore original color
        }
    }


    private fun checkSequence(tileIndex: Int) {        //check function
        if (tileIndex == sequence[playerIndex]) {
            playerIndex++
            if (playerIndex == sequence.size) {   // Player got right sequence
                score++
                updateScoreDisplay(score, loadHighScore()) //update score every time
                showNextSequence()
            }
        } else {
            gameOver()
        }
    }


    private fun updateScoreDisplay(score: Int, highScore: Int) {
//        textViewScore.text = "Score: $score"
        textViewScore.text = "$name\nScore: $score\nHigh Score: $highScore"

    }


    private fun loadHighScore(): Int {
        return sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
    }


    private fun saveHighScore(score: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(HIGH_SCORE_KEY, score)
        editor.apply()
    }


    //    private fun checkAndUpdateHighScore(score: Int) {
//        val highScore = loadHighScore()
//        if (score > highScore) {
//            saveHighScore(score)
//
//        }
//    }
    private fun checkAndUpdateHighScore(score: Int) {
        val highScore = loadHighScore()
        if (score > highScore) {
            saveHighScore(score)
            updateScoreDisplay(score, score)// display the new high score.
        } else {
            updateScoreDisplay(score, highScore)
        }
    }


    private fun gameOver() {
        Toast.makeText(this, "Game Over! Score: $score", Toast.LENGTH_SHORT).show()
        checkAndUpdateHighScore(score)

        textViewGameOver.visibility = View.VISIBLE

        score = 0            // Reset score,start new game.
        sequence.clear()
        updateScoreDisplay(score, loadHighScore())
        //updateScoreDisplay(score)
        // Optionally, start a new game automatically.
        // startGame()
    }


    private fun endGame() {
        saveHighScore(score)

        val highScore = loadHighScore()
        if (score > highScore) {
            updateScoreDisplay(score, score)
        }
        val intent = Intent(this, GameMainActivity::class.java)
        intent.putExtra("name", name)
        startActivity(intent)
//        score = 0
//        updateScoreDisplay(score, loadHighScore())
    }


    private fun getRandomColor(): Int {
        val color = arrayOf(Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN)
        return color.random()
    }


}