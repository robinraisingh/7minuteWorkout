package com.robinsingh.sevenminuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_exercise.*

class exerciseActivity : AppCompatActivity() {
    private var restTimer:CountDownTimer?=null  //10 second for rest
    private var onTimer:CountDownTimer?=null   // 30 second for workout
    private var restProgress=0
    private var onProgress=0
    private var onTimeDuration=30

    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePosition:Int=-1   //it will start from 0

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

        exerciseList=Constants.defaultExerciseList()
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

    //function that will set the timer ans progress bar for rest
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

                currentExercisePosition++  //exercise and rest finished
                setupOnView() //here it is calling ON timer which will go on for 30 second
                //Toast.makeText(this@exerciseActivity,"finish",Toast.LENGTH_SHORT).show()
            }

        }.start()

    }

    // WORKOUT PROGRESS BAR ON SETTING FUNCTION

    private fun setOnProgressBar(){
        onProgressBar.progress=onProgress
        onTimer=object : CountDownTimer(30000,1000){
            override fun onTick(millisToFinish: Long) {
                onProgress++
                onProgressBar.progress=30-onProgress   //start from 0 towards 30
                tvOnTimer.text=(30-onProgress).toString()

            }

            override fun onFinish() {
                if(currentExercisePosition<11){
                    setupRestView()
                }
                else{
                    Toast.makeText(this@exerciseActivity,"well done!",Toast.LENGTH_SHORT).show()
                }
            }

        }.start()

    }

    //ON VIEW FUNCTION

    private fun setupOnView(){

        llRestLayout.visibility= View.INVISIBLE
        llOnLayout.visibility=View.VISIBLE
        if(onTimer!=null){
            onTimer!!.cancel()
            onProgress=0
        }
        setOnProgressBar()
        //setting the image and name of exercise from the exercise list whose pointer is currently on currentExercisePosition
        evImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        exerciseName.text=exerciseList!![currentExercisePosition].getName()
    }

    // REST VIEW SETTING UP FUNCTION

    private fun setupRestView(){
        llRestLayout.visibility= View.VISIBLE
        llOnLayout.visibility=View.INVISIBLE
        //if the restTimer is not nul then cance it and reset the values
        //this method will acutually be called first and check if timer is running or not
        //and then start everything from beggining
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }
        //and then restart again
        upcomingExercise.text=exerciseList!![currentExercisePosition+1].getName()
        setRestProgressBar()
    }

}