package com.example.gem

object Constants {
    fun getQuestions(): ArrayList<Question> {
        val qList = ArrayList<Question>()
        qList.add(Question(1, "Which language is officially recommended by Google for Android development?", listOf("Java", "Kotlin", "C++", "Python"), 1, "Kotlin is the recommended language for modern Android development."))
        qList.add(Question(2, "What does APK stand for?", listOf("Android Package Kit", "Android Project Kit", "App Package Kit", "Application Project Key"), 0, "APK stands for Android Package Kit, the file format used to install Android apps."))
        qList.add(Question(3, "Which keyword is used to declare an immutable variable in Kotlin?", listOf("var", "let", "val", "const"), 2, "'val' is used for read-only (immutable) variables, while 'var' is for mutable variables."))
        qList.add(Question(4, "Which layout is best for complex, flat view hierarchies?", listOf("LinearLayout", "RelativeLayout", "FrameLayout", "ConstraintLayout"), 3, "ConstraintLayout allows you to create large and complex layouts with a flat view hierarchy."))
        qList.add(Question(5, "What component is used to display a scrolling list of items efficiently?", listOf("ListView", "ScrollView", "RecyclerView", "GridView"), 2, "RecyclerView is the modern and efficient way to display large lists by recycling views."))
        qList.add(Question(6, "Which method is called when an Activity becomes visible to the user?", listOf("onCreate", "onStart", "onResume", "onPause"), 1, "onStart is called when the activity becomes visible, and onResume when it starts interacting."))
        qList.add(Question(7, "How do you start a new Activity in Android?", listOf("startActivity()", "newActivity()", "openActivity()", "launch()"), 0, "startActivity(intent) is the standard way to launch a new activity."))
        qList.add(Question(8, "What is the purpose of the AndroidManifest.xml file?", listOf("To write UI layout", "To declare app components and permissions", "To store strings and colors", "To write database queries"), 1, "The Manifest describes essential information about your app to the Android build tools and OS."))
        qList.add(Question(9, "Which symbol is used for null-safety calls in Kotlin?", listOf("!!", "?.", "?:", "->"), 1, "The safe call operator '?.' returns null if the object is null, avoiding NullPointerExceptions."))
        qList.add(Question(10, "What does 'dp' stand for in Android layouts?", listOf("Display Pixels", "Density-independent Pixels", "Data Pixels", "Device Pixels"), 1, "Density-independent Pixels (dp) scale appropriately on devices with different screen densities."))
        qList.add(Question(11, "Which built-in Android class is used for scheduling countdowns?", listOf("Timer", "Handler", "CountDownTimer", "AlarmManager"), 2, "CountDownTimer is a convenient class provided by the Android SDK for countdowns."))
        qList.add(Question(12, "What is used to store key-value data persistently in Android?", listOf("Intents", "SharedPreferences", "ViewModels", "Bundles"), 1, "SharedPreferences provides a simple way to store small amounts of primitive data persistently."))
        qList.add(Question(13, "What is a 'data class' in Kotlin?", listOf("A class specifically for database access", "A class meant only to hold data", "A class that cannot be instantiated", "An abstract class"), 1, "Data classes automatically generate useful methods like equals(), hashCode(), and toString()."))
        qList.add(Question(14, "Which IDE is standard for Android development?", listOf("Eclipse", "IntelliJ IDEA", "Android Studio", "Visual Studio Code"), 2, "Android Studio is the official Integrated Development Environment (IDE) for Android."))
        qList.add(Question(15, "What is an Intent in Android?", listOf("A UI element", "A messaging object to request an action", "A database table", "A background thread"), 1, "An Intent is a messaging object you can use to request an action from another app component."))
        return qList
    }
}
