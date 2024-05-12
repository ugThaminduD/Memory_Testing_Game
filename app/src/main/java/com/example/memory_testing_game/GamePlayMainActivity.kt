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


class GamePlayMainActivity : AppCompatActivity() {

    private val sequence = mutableListOf<Int>()
    private var playerIndex = 0
    private var score = 0
    private val HIGH_SCORE_KEY = "high_score"
//    private val TURN_TIME_LIMIT_MS = 3000L
//    private lateinit var turnTimer: CountDownTimer
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button

    private lateinit var buttonStart: Button
    private lateinit var buttonEnd: Button

    private lateinit var textViewScore: TextView
    private lateinit var textViewGameOver: TextView
//    private lateinit var textViewRemainingTime: TextView

    var name: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play_main)

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


        button1 = findViewById(R.id.button_1)       //  buttons
        button2 = findViewById(R.id.button_2)
        button3 = findViewById(R.id.button_3)
        button4 = findViewById(R.id.button_4)

        button1.setOnClickListener {
            checkSequence(1)
        }
        button2.setOnClickListener {
            checkSequence(2)
        }
        button3.setOnClickListener {
            checkSequence(3)
        }
        button4.setOnClickListener {
            checkSequence(4)
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
        showNextSequence()

        textViewGameOver.visibility = View.INVISIBLE
    }


    private fun showNextSequence() {
        playerIndex = 0
        sequence.add(Random.nextInt(4) + 1)
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

        val button = when (tileIndex) {
            1 -> button1
            2 -> button2
            3 -> button3
            4 -> button4
            else -> null
        }
        button?.let {

            val originalColor = it.backgroundTintList?.defaultColor    // Change color of tile
            val newColor = when {
                score >= 10 && score % 10 == 0 -> getRandomColor()
                else -> Color.YELLOW
            }
            it.setBackgroundColor(newColor)

            CoroutineScope(Dispatchers.Main).launch {
                delay(500)                                  // delay
                it.setBackgroundColor(originalColor ?: Color.WHITE) // Restore original color
            }
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
        textViewScore.text = "$name\nScore: $score\nHigh Score: $highScore"  // score display

    }


    private fun loadHighScore(): Int {
        return sharedPreferences.getInt(HIGH_SCORE_KEY, 0)
    }


    private fun saveHighScore(score: Int) {
        val editor = sharedPreferences.edit()  // store in sharePreferences
        editor.putInt(HIGH_SCORE_KEY, score)   // current score => HIGH_SCORE_KEY
        editor.apply()
    }


    private fun checkAndUpdateHighScore(score: Int) {
        val highScore = loadHighScore()
        if (score > highScore) {
            saveHighScore(score)            // save new score => high score
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


//    private fun updateTurnTimer() {
//        turnTimer?.cancel() // Cancel any ongoing timer
//        turnTimer = object : CountDownTimer(TURN_TIME_LIMIT_MS, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                val secondsRemaining = millisUntilFinished / 1000
//                textViewRemainingTime.text = "Time remaining: $secondsRemaining seconds"
//            }
//            override fun onFinish() {
//                gameOver()// Time's up
//            }
//        }.start()
//    }


//    private fun onButtonClicked(tileIndex: Int) {
//        turnTimer?.cancel() // Cancel the turn timer
//        checkSequence(tileIndex)
//    }


}

