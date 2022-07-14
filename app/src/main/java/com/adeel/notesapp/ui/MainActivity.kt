package com.adeel.notesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adeel.notesapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}