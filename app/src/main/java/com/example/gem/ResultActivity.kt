package com.example.gem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val prefs = SharedPrefsHelper(this)

        val tvTitle = findViewById<TextView>(R.id.tvResultTitle)
        val tvScore = findViewById<TextView>(R.id.tvScoreText)
        val tvAccuracy = findViewById<TextView>(R.id.tvAccuracyText)
        val tvStreak = findViewById<TextView>(R.id.tvBestStreakText)
        val tvXp = findViewById<TextView>(R.id.tvXpEarnedText)
        
        val btnReview = findViewById<Button>(R.id.btnReview)
        val btnPlayAgain = findViewById<Button>(R.id.btnPlayAgain)
        val btnBattle = findViewById<Button>(R.id.btnBattle)
        val btnHome = findViewById<Button>(R.id.btnHome)

        val score = intent.getIntExtra("SCORE", 0)
        val streak = intent.getIntExtra("STREAK", 0)
        val mode = intent.getStringExtra("MODE") ?: "NORMAL"
        val bossDefeated = intent.getBooleanExtra("BOSS_DEFEATED", false)

        var xpEarned: Int

        if (mode == "NORMAL") {
            tvTitle.text = getString(R.string.quiz_complete_title)
            val totalQuestions = SessionData.reviewQuestions.size
            val correctCount = SessionData.reviewQuestions.indices.count { i ->
                i < SessionData.userAnswers.size && 
                SessionData.userAnswers[i] == SessionData.reviewQuestions[i].correctAnswerIndex 
            }
            val acc = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
            
            tvScore.text = getString(R.string.score_format, score)
            tvAccuracy.text = getString(R.string.accuracy_format, acc)
            tvStreak.text = getString(R.string.streak_format, streak)
            
            xpEarned = (correctCount * 10) + 20
            
        } else {
            // Battle mode
            if (bossDefeated) {
                tvTitle.text = getString(R.string.boss_defeated)
                tvTitle.setTextColor(ContextCompat.getColor(this, R.color.color_xp))
                xpEarned = score + 50
            } else {
                tvTitle.text = getString(R.string.game_over)
                tvTitle.setTextColor(ContextCompat.getColor(this, R.color.color_error))
                xpEarned = score
            }
            tvScore.text = getString(R.string.battle_score_format, score)
            tvAccuracy.text = getString(R.string.mode_battle)
            tvStreak.text = getString(R.string.best_streak_format, streak)
        }

        tvXp.text = getString(R.string.xp_earned_format, xpEarned)

        // Update SharedPreferences
        prefs.addXp(xpEarned)
        prefs.bestScore = score
        prefs.bestStreak = streak

        btnReview.setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }

        btnPlayAgain.setOnClickListener {
            val intent = if (mode == "NORMAL") Intent(this, QuizActivity::class.java) else Intent(this, BattleActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnBattle.setOnClickListener {
            startActivity(Intent(this, BattleActivity::class.java))
            finish()
        }

        btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
