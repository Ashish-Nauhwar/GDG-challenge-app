package com.example.gem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var scoreText: TextView
    private lateinit var messageText: TextView
    private lateinit var playAgainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result)

        scoreText = findViewById(R.id.scoreText)
        messageText = findViewById(R.id.messageText)
        playAgainButton = findViewById(R.id.playAgainButton)

        val score = intent.getIntExtra(MainActivity.EXTRA_SCORE, 0)
        val total = intent.getIntExtra(MainActivity.EXTRA_TOTAL, 0)
        scoreText.text = getString(R.string.score_format, score, total)

        messageText.text = when {
            score == total -> getString(R.string.message_perfect)
            score >= (total / 2) -> getString(R.string.message_good_job)
            else -> getString(R.string.message_good_try)
        }

        playAgainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
