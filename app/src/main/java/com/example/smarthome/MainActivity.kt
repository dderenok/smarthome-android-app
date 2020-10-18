package com.example.smarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        d(MAIN_LOG_TAG, "Main page started")
    }

    companion object {
        private var MAIN_LOG_TAG = MainActivity::class.java.simpleName
    }
}