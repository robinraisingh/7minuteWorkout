package com.robinsingh.sevenminuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_exercise.*

class exerciseActivity : AppCompatActivity() {
    private var restTimer:CountDownTimer?=null
    private var restProgress=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(myToolBar)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        myToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
        setupRestView() //this will begin the process of setting the timer up
    }

    override fun onDestroy() {
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0   //this is redundent as it will be set to 0 as we get in
        }
        super.onDestroy()
        //actually this whole methode is redundent as when activity be called again evert thing will be setup again
    }

    //function that will set the timer ans progress bar
    //this function will make the text view changes
    private fun setRestProgressBar(){
        progressBar.progress=restProgress
        restTimer=object : CountDownTimer(10000,1000){
            override fun onTick(millisToFinish: Long) {
                restProgress++
                progressBar.progress=10-restProgress   //start from 0 towards 10
                tvTimer.text=(10-restProgress).toString()

            }

            override fun onFinish() {
                Toast.makeText(this@exerciseActivity,"finish",Toast.LENGTH_SHORT).show()
            }

        }.start()

    }
    private fun setupRestView(){
        //if the restTimer is not nul then cance it and reset the values
        //this method will acutually be called first and check if timer is running or not
        //and then start everything from beggining
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }
        //and then restart again
        setRestProgressBar()
    }
}