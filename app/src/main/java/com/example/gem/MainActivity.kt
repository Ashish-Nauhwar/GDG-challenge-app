package com.example.gem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var prefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsHelper = SharedPrefsHelper(this)

        val tvBestScore = findViewById<TextView>(R.id.tvBestScore)
        val tvBestStreak = findViewById<TextView>(R.id.tvBestStreak)
        val tvTotalXp = findViewById<TextView>(R.id.tvTotalXp)
        val btnStartQuiz = findViewById<Button>(R.id.btnStartQuiz)
        val btnQuizBattle = findViewById<Button>(R.id.btnQuizBattle)

        tvBestScore.text = "🏆 Best Score: ${prefsHelper.bestScore}"
        tvBestStreak.text = "🔥 Best Streak: ${prefsHelper.bestStreak}"
        tvTotalXp.text = "⭐ XP: ${prefsHelper.totalXp}"

        btnStartQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        btnQuizBattle.setOnClickListener {
            startActivity(Intent(this, BattleActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh values when returning to Home
        val tvBestScore = findViewById<TextView>(R.id.tvBestScore)
        val tvBestStreak = findViewById<TextView>(R.id.tvBestStreak)
        val tvTotalXp = findViewById<TextView>(R.id.tvTotalXp)

        tvBestScore.text = "🏆 Best Score: ${prefsHelper.bestScore}"
        tvBestStreak.text = "🔥 Best Streak: ${prefsHelper.bestStreak}"
        tvTotalXp.text = "⭐ XP: ${prefsHelper.totalXp}"
    }
}
