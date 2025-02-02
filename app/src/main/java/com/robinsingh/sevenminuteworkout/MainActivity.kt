package com.robinsingh.sevenminuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llStart.setOnClickListener {
            val intent= Intent(this,exerciseActivity::class.java)
            startActivity(intent)
        }
        llBMI.setOnClickListener {
            val intent= Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }
        llHistory.setOnClickListener {
            val intent= Intent(this,historyActivity::class.java)
            startActivity(intent)
        }

    }

}