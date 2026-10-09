package com.example.gem

import java.io.Serializable

data class Question(
    val id: String,
    val domain: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val hint: String = "",
    val difficulty: String = "Medium"
) : Serializable
