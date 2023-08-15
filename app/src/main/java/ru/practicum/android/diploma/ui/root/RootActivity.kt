package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        findViewById<TextView>(R.id.buildConfigReadExampleTextView).text =
            "${BuildConfig.HH_CLIENT_ID} -- ${BuildConfig.HH_CLIENT_SECRET}"
    }
}