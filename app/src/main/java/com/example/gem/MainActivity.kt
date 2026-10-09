package com.example.gem

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var prefsHelper: SharedPrefsHelper
    private var selectedDomain = ""
    private var selectedCount = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        prefsHelper = SharedPrefsHelper(this)
        applyTheme(prefsHelper.themeMode)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Record activity day if coming back
        prefsHelper.recordActivityDay()

        setupUI()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        setupUI()
    }

    private fun setupUI() {
        val tvCurrentStreak = findViewById<TextView>(R.id.tvCurrentStreak)
        val tvTotalXp = findViewById<TextView>(R.id.tvTotalXp)
        
        tvCurrentStreak.text = getString(R.string.streak_days_format, prefsHelper.currentStreak)
        tvTotalXp.text = prefsHelper.totalXp.toString()

        val btnThemeToggle = findViewById<ImageView>(R.id.btnThemeToggle)
        updateThemeIcon(btnThemeToggle)

        updateStartButton()
        updateDomainStats()
    }

    private fun setupListeners() {
        val btnThemeToggle = findViewById<ImageView>(R.id.btnThemeToggle)
        btnThemeToggle.setOnClickListener {
            toggleTheme()
        }

        val rgDomains = findViewById<RadioGroup>(R.id.rgDomains)
        rgDomains.setOnCheckedChangeListener { _, checkedId ->
            selectedDomain = when (checkedId) {
                R.id.rbDomainDSA -> "DSA"
                R.id.rbDomainJS -> "JavaScript"
                R.id.rbDomainKotlin -> "Kotlin"
                R.id.rbDomainJava -> "Java"
                R.id.rbDomainPython -> "Python"
                else -> ""
            }
            updateDomainStats()
            updateStartButton()
        }

        val rgCount = findViewById<RadioGroup>(R.id.rgCount)
        rgCount.setOnCheckedChangeListener { _, checkedId ->
            selectedCount = when (checkedId) {
                R.id.rbCount5 -> 5
                R.id.rbCount10 -> 10
                R.id.rbCount15 -> 15
                R.id.rbCount20 -> 20
                R.id.rbCount25 -> 25
                R.id.rbCount30 -> 30
                else -> 10
            }
            val tvLifelineNotice = findViewById<TextView>(R.id.tvLifelineNotice)
            tvLifelineNotice.visibility = if (selectedCount >= 10) View.VISIBLE else View.INVISIBLE
            updateStartButton()
        }
        
        // Defaults
        findViewById<RadioButton>(R.id.rbCount10).isChecked = true

        findViewById<Button>(R.id.btnStartQuiz).setOnClickListener {
            if (selectedDomain.isNotEmpty()) {
                SessionData.selectedDomain = selectedDomain
                SessionData.selectedCount = selectedCount
                SessionData.isMistakePractice = false
                startActivity(Intent(this, QuizActivity::class.java))
            }
        }

        findViewById<Button>(R.id.btnMistakeVault).setOnClickListener {
            startActivity(Intent(this, MistakeVaultActivity::class.java))
        }

        findViewById<Button>(R.id.btnQuizBattle).setOnClickListener {
            startActivity(Intent(this, BattleActivity::class.java))
        }
    }

    private fun updateDomainStats() {
        val tvDomainStats = findViewById<TextView>(R.id.tvDomainStats)
        if (selectedDomain.isEmpty()) {
            tvDomainStats.text = getString(R.string.select_domain_stats)
        } else {
            val attempts = prefsHelper.getDomainAttempts(selectedDomain)
            val accuracy = prefsHelper.getDomainAccuracy(selectedDomain)
            tvDomainStats.text = getString(R.string.domain_stats_format, attempts, accuracy)
        }
    }

    private fun updateStartButton() {
        val btnStartQuiz = findViewById<Button>(R.id.btnStartQuiz)
        btnStartQuiz.isEnabled = selectedDomain.isNotEmpty()
    }

    private fun applyTheme(themeMode: String) {
        val mode = when (themeMode) {
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
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
