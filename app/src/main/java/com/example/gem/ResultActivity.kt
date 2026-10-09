package com.example.gem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val prefs = SharedPrefsHelper(this)
        prefs.recordActivityDay()

        val tvDomainLabel = findViewById<TextView>(R.id.tvDomainLabel)
        val tvTitle = findViewById<TextView>(R.id.tvResultTitle)
        val tvScore = findViewById<TextView>(R.id.tvScoreText)
        val tvTotal = findViewById<TextView>(R.id.tvTotalQuestions)
        val pbScoreRing = findViewById<ProgressBar>(R.id.pbScoreRing)
        val tvAccuracy = findViewById<TextView>(R.id.tvAccuracyText)
        val tvStreak = findViewById<TextView>(R.id.tvBestStreakText)
        val tvXp = findViewById<TextView>(R.id.tvXpEarnedText)
        
        val btnPlayAgain = findViewById<Button>(R.id.btnPlayAgain)
        val btnReview = findViewById<Button>(R.id.btnReview)
        val btnHome = findViewById<Button>(R.id.btnHome)
        val btnMistakes = findViewById<Button>(R.id.btnMistakes)

        val score = intent.getIntExtra("SCORE", 0) // Passed as XP
        val total = intent.getIntExtra("TOTAL", 10)
        val correctCount = intent.getIntExtra("CORRECT_COUNT", 0)
        val streak = intent.getIntExtra("STREAK", 0)
        val mode = intent.getStringExtra("MODE") ?: "NORMAL"
        val bossDefeated = intent.getBooleanExtra("BOSS_DEFEATED", false)

        var xpEarned = score

        if (mode == "NORMAL") {
            tvDomainLabel.text = getString(R.string.result_domain_complete, SessionData.selectedDomain.uppercase())
            
            val acc = if (total > 0) (correctCount * 100) / total else 0
            
            tvTitle.text = when {
                acc == 100 -> getString(R.string.result_title_perfect)
                acc >= 70 -> getString(R.string.result_title_good)
                else -> getString(R.string.result_title_try)
            }
            
            tvScore.text = correctCount.toString()
            tvTotal.text = getString(R.string.result_total_format, total)
            pbScoreRing.progress = acc
            tvAccuracy.text = getString(R.string.accuracy_value_format, acc)
            tvStreak.text = streak.toString()
            
            // Stats check
            prefs.questionsAnswered += total
        } else {
            // Battle mode
            tvDomainLabel.text = "QUIZ BATTLE COMPLETE"
            if (bossDefeated) {
                tvTitle.text = getString(R.string.result_title_battle_win)
                tvTitle.setTextColor(ContextCompat.getColor(this, R.color.color_xp))
                xpEarned += 50
            } else {
                tvTitle.text = getString(R.string.result_title_battle_lose)
                tvTitle.setTextColor(ContextCompat.getColor(this, R.color.color_error))
            }
            tvScore.text = score.toString()
            tvTotal.text = " XP"
            pbScoreRing.progress = if (bossDefeated) 100 else (score * 100 / 120)
            tvAccuracy.text = "BATTLE"
            tvStreak.text = streak.toString()
        }

        tvXp.text = getString(R.string.xp_earned_format, xpEarned)

        prefs.addXp(xpEarned)

        btnReview.setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }

        btnPlayAgain.setOnClickListener {
            val intent = if (mode == "NORMAL") Intent(this, QuizActivity::class.java) else Intent(this, BattleActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnMistakes.setOnClickListener {
            startActivity(Intent(this, MistakeVaultActivity::class.java))
            finish()
        }

        btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
