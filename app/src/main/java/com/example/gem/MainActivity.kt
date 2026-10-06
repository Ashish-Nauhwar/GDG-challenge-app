package com.example.gem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var questionNumberText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var radioButtons: Array<RadioButton>

    private val questions = arrayOf(
        "Which language is used for Android development?",
        "Which IDE is commonly used for Android development?",
        "What does APK stand for?",
        "Which data structure follows FIFO?",
    )

    private val options = arrayOf(
        arrayOf("Go", "Kotlin", "Python", "C++"),
        arrayOf("Android Studio", "IntelliJ", "Visual Studio", "Eclipse"),
        arrayOf("Android Package Kit", "Android Project Kit", "Android Package File", "Android Project File"),
        arrayOf("Stack", "Queue", "Tree", "Linked List"),
    )

    private val correctAnswers = intArrayOf(
        1, 0, 0, 1,
    )
    private var currentQuestion = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        questionText = findViewById(R.id.questionText)
        questionNumberText = findViewById(R.id.questionNumberText)
        optionsGroup = findViewById(R.id.optionsGroup)
        nextButton = findViewById(R.id.nextButton)

        radioButtons = arrayOf(
            findViewById(R.id.option1),
            findViewById(R.id.option2),
            findViewById(R.id.option3),
            findViewById(R.id.option4),
        )

        if (savedInstanceState != null) {
            currentQuestion = savedInstanceState.getInt(KEY_CURRENT_QUESTION, 0)
            score = savedInstanceState.getInt(KEY_SCORE, 0)
        }

        showQuestion()

        nextButton.setOnClickListener {
            val answered = checkAnswer()
            if (!answered) {
                return@setOnClickListener
            }

            if (currentQuestion < (questions.size - 1)) {
                currentQuestion++
                showQuestion()
            } else {
                openResultScreen()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_QUESTION, currentQuestion)
        outState.putInt(KEY_SCORE, score)
    }

    private fun showQuestion() {
        questionText.text = questions[currentQuestion]
        questionNumberText.text = getString(R.string.question_progress, currentQuestion + 1, questions.size)

        for (i in radioButtons.indices) {
            radioButtons[i].text = options[currentQuestion][i]
        }
        optionsGroup.clearCheck()
    }

    private fun checkAnswer(): Boolean {
        val selectedId = optionsGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, getString(R.string.select_an_answer), Toast.LENGTH_SHORT).show()
            return false
        }

        val selectedIndex = radioButtons.indexOfFirst { it.id == selectedId }
        if (selectedIndex != -1 && selectedIndex == correctAnswers[currentQuestion]) {
            score++
            Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show()
        } else {
            val correctAnswer = options[currentQuestion][correctAnswers[currentQuestion]]
            Toast.makeText(this, getString(R.string.wrong_answer, correctAnswer), Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun openResultScreen() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(EXTRA_SCORE, score)
            putExtra(EXTRA_TOTAL, questions.size)
        }
        startActivity(intent)
        finish()
    }

    companion object {
        const val EXTRA_SCORE = "SCORE"
        const val EXTRA_TOTAL = "TOTAL"
        private const val KEY_CURRENT_QUESTION = "KEY_CURRENT_QUESTION"
        private const val KEY_SCORE = "KEY_SCORE"
    }
}
