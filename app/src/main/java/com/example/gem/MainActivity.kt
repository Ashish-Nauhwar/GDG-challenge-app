package com.example.gem

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var prefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        prefsHelper = SharedPrefsHelper(this)
        
        // Apply theme before super.onCreate
        applyTheme(prefsHelper.themeMode)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvBestScore = findViewById<TextView>(R.id.tvBestScore)
        val tvBestStreak = findViewById<TextView>(R.id.tvBestStreak)
        val tvTotalXp = findViewById<TextView>(R.id.tvTotalXp)
        val btnStartQuiz = findViewById<Button>(R.id.btnStartQuiz)
        val btnQuizBattle = findViewById<Button>(R.id.btnQuizBattle)
        val btnThemeToggle = findViewById<ImageView>(R.id.btnThemeToggle)

        tvBestScore.text = getString(R.string.best_score_format, prefsHelper.bestScore)
        tvBestStreak.text = getString(R.string.best_streak_format, prefsHelper.bestStreak)
        tvTotalXp.text = getString(R.string.total_xp_format, prefsHelper.totalXp)

        btnStartQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        btnQuizBattle.setOnClickListener {
            startActivity(Intent(this, BattleActivity::class.java))
        }

        updateThemeIcon(btnThemeToggle)
        btnThemeToggle.setOnClickListener {
            toggleTheme()
        }
    }

    override fun onResume() {
        super.onResume()
        val tvBestScore = findViewById<TextView>(R.id.tvBestScore)
        val tvBestStreak = findViewById<TextView>(R.id.tvBestStreak)
        val tvTotalXp = findViewById<TextView>(R.id.tvTotalXp)

        tvBestScore.text = getString(R.string.best_score_format, prefsHelper.bestScore)
        tvBestStreak.text = getString(R.string.best_streak_format, prefsHelper.bestStreak)
        tvTotalXp.text = getString(R.string.total_xp_format, prefsHelper.totalXp)
    }

    private fun applyTheme(themeMode: String) {
        val mode = when (themeMode) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
        delegate.localNightMode = mode
    }

    private fun updateThemeIcon(button: ImageView) {
        val currentIsDark = when (prefsHelper.themeMode) {
            "dark" -> true
            "light" -> false
            else -> {
                val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                currentNightMode == Configuration.UI_MODE_NIGHT_YES
            }
        }
        if (currentIsDark) {
            button.setImageResource(R.drawable.ic_sun)
        } else {
            button.setImageResource(R.drawable.ic_moon)
        }
    }

    private fun toggleTheme() {
        val currentIsDark = when (prefsHelper.themeMode) {
            "dark" -> true
            "light" -> false
            else -> {
                val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                currentNightMode == Configuration.UI_MODE_NIGHT_YES
            }
        }
        
        val newMode = if (currentIsDark) "light" else "dark"
        prefsHelper.themeMode = newMode
        applyTheme(newMode)
    }
}
