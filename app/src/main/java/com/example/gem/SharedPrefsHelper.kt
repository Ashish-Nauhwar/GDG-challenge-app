package com.example.gem

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("GemQuizPrefs", Context.MODE_PRIVATE)

    var bestScore: Int
        get() = prefs.getInt("BEST_SCORE", 0)
        set(value) {
            if (value > bestScore) prefs.edit().putInt("BEST_SCORE", value).apply()
        }

    var bestStreak: Int
        get() = prefs.getInt("BEST_STREAK", 0)
        set(value) {
            if (value > bestStreak) prefs.edit().putInt("BEST_STREAK", value).apply()
        }

    var totalXp: Int
        get() = prefs.getInt("TOTAL_XP", 0)
        set(value) = prefs.edit().putInt("TOTAL_XP", value).apply()

    fun addXp(xp: Int) {
        totalXp += xp
    }
}
