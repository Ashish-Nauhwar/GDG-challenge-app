package com.example.gem

import java.io.Serializable

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
) : Serializable
