package com.example.gem

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SharedPrefsHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("GemCodeQuestPrefs", Context.MODE_PRIVATE)

    var totalXp: Int
        get() = prefs.getInt("TOTAL_XP", 0)
        set(value) = prefs.edit().putInt("TOTAL_XP", value).apply()

    var questionsAnswered: Int
        get() = prefs.getInt("TOTAL_QUESTIONS_ANSWERED", 0)
        set(value) = prefs.edit().putInt("TOTAL_QUESTIONS_ANSWERED", value).apply()

    var themeMode: String
        get() = prefs.getString("THEME_MODE", "system") ?: "system"
        set(value) = prefs.edit().putString("THEME_MODE", value).apply()

    fun addXp(xp: Int) {
        totalXp += xp
    }

    // --- Streak Logic ---
    var currentStreak: Int
        get() = prefs.getInt("CURRENT_STREAK", 0)
        private set(value) = prefs.edit().putInt("CURRENT_STREAK", value).apply()

    var bestStreak: Int
        get() = prefs.getInt("BEST_STREAK", 0)
        private set(value) = prefs.edit().putInt("BEST_STREAK", value).apply()

    private var activeDatesString: String
        get() = prefs.getString("ACTIVE_DATES", "[]") ?: "[]"
        set(value) = prefs.edit().putString("ACTIVE_DATES", value).apply()

    fun getActiveDates(): List<String> {
        val arr = JSONArray(activeDatesString)
        val list = mutableListOf<String>()
        for (i in 0 until arr.length()) {
            list.add(arr.getString(i))
        }
        return list
    }

    fun recordActivityDay() {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = formatter.format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = formatter.format(cal.time)

        val dates = getActiveDates().toMutableList()

        if (!dates.contains(todayStr)) {
            dates.add(todayStr)
            activeDatesString = JSONArray(dates).toString()

            if (dates.contains(yesterdayStr)) {
                currentStreak += 1
            } else {
                currentStreak = 1
            }

            if (currentStreak > bestStreak) {
                bestStreak = currentStreak
            }
        }
    }

    // --- Domain Stats ---
    fun updateDomainStats(domain: String, correct: Int, total: Int) {
        val statsStr = prefs.getString("DOMAIN_STATS", "{}") ?: "{}"
        val json = JSONObject(statsStr)
        
        val domainObj = if (json.has(domain)) json.getJSONObject(domain) else JSONObject()
        val prevAttempts = domainObj.optInt("attempts", 0)
        val prevCorrect = domainObj.optInt("correct", 0)
        val prevTotal = domainObj.optInt("total", 0)

        domainObj.put("attempts", prevAttempts + 1)
        domainObj.put("correct", prevCorrect + correct)
        domainObj.put("total", prevTotal + total)
        domainObj.put("lastPracticed", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))

        json.put(domain, domainObj)
        prefs.edit().putString("DOMAIN_STATS", json.toString()).apply()
    }

    fun getDomainAttempts(domain: String): Int {
        val statsStr = prefs.getString("DOMAIN_STATS", "{}") ?: "{}"
        val json = JSONObject(statsStr)
        if (!json.has(domain)) return 0
        return json.getJSONObject(domain).optInt("attempts", 0)
    }

    fun getDomainAccuracy(domain: String): Int {
        val statsStr = prefs.getString("DOMAIN_STATS", "{}") ?: "{}"
        val json = JSONObject(statsStr)
        if (!json.has(domain)) return 0
        val obj = json.getJSONObject(domain)
        val total = obj.optInt("total", 0)
        val correct = obj.optInt("correct", 0)
        if (total == 0) return 0
        return (correct * 100) / total
    }

    // --- Mistake Vault ---
    fun saveMistake(q: Question, userAnswer: Int) {
        val mistakesStr = prefs.getString("MISTAKE_VAULT", "{}") ?: "{}"
        val json = JSONObject(mistakesStr)
        
        val mistakeObj = if (json.has(q.id)) json.getJSONObject(q.id) else JSONObject()
        mistakeObj.put("domain", q.domain)
        mistakeObj.put("userAnswer", userAnswer)
        val timesWrong = mistakeObj.optInt("timesWrong", 0)
        mistakeObj.put("timesWrong", timesWrong + 1)
        mistakeObj.put("mastered", false)

        json.put(q.id, mistakeObj)
        prefs.edit().putString("MISTAKE_VAULT", json.toString()).apply()
    }

    fun markMistakeMastered(questionId: String) {
        val mistakesStr = prefs.getString("MISTAKE_VAULT", "{}") ?: "{}"
        val json = JSONObject(mistakesStr)
        if (json.has(questionId)) {
            val mistakeObj = json.getJSONObject(questionId)
            mistakeObj.put("mastered", true)
            prefs.edit().putString("MISTAKE_VAULT", json.toString()).apply()
        }
    }

    fun getMistakeIdsForDomain(domain: String): List<String> {
        val mistakesStr = prefs.getString("MISTAKE_VAULT", "{}") ?: "{}"
        val json = JSONObject(mistakesStr)
        val ids = mutableListOf<String>()
        val keys = json.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val obj = json.getJSONObject(key)
            if (obj.getString("domain") == domain && !obj.optBoolean("mastered", false)) {
                ids.add(key)
            }
        }
        return ids
    }

    fun getUnmasteredMistakesCount(): Int {
        val mistakesStr = prefs.getString("MISTAKE_VAULT", "{}") ?: "{}"
        val json = JSONObject(mistakesStr)
        var count = 0
        val keys = json.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val obj = json.getJSONObject(key)
            if (!obj.optBoolean("mastered", false)) {
                count++
            }
        }
        return count
    }
}
