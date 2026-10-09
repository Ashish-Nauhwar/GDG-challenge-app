package com.example.gem

object SessionData {
    var reviewQuestions = ArrayList<Question>()
    var userAnswers = ArrayList<Int>() // -1 represents timeout or skipped
    var selectedDomain: String = ""
    var selectedCount: Int = 10
    var isMistakePractice: Boolean = false
}
