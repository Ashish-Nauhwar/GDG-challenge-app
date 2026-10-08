package com.example.gem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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
            tvTitle.text = "🏆 QUIZ COMPLETE"
            val totalQuestions = SessionData.reviewQuestions.size
            val correctCount = SessionData.reviewQuestions.indices.count { i ->
                (i < SessionData.userAnswers.size) &&
                (SessionData.userAnswers[i] == SessionData.reviewQuestions[i].correctAnswerIndex)
            }
            val acc = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
            
            tvScore.text = getString(R.string.score_format, score)
            tvAccuracy.text = "Accuracy: $acc%"
            tvStreak.text = "Streak: $streak"
            
            xpEarned = (correctCount * 10) + 20
            
        } else {
            // Battle mode
            if (bossDefeated) {
                tvTitle.text = "👑 BOSS DEFEATED!"
                tvTitle.setTextColor(resources.getColor(R.color.xp_gold, theme))
                xpEarned = score + 50
            } else {
                tvTitle.text = "GAME OVER"
                tvTitle.setTextColor(resources.getColor(R.color.wrong_red, theme))
                xpEarned = score
            }
            tvScore.text = "Battle Score: $score"
            tvAccuracy.text = "Mode: QUIZ BATTLE"
            tvStreak.text = "Best Streak: $streak"
        }

        tvXp.text = "+$xpEarned XP"

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
