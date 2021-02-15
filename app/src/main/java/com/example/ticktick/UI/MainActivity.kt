package com.example.ticktick.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ticktick.R
import dagger.hilt.android.AndroidEntryPoint

/**
   Author : Swaraj Deshmukh
   Date :  15 December 2020
 **/

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}