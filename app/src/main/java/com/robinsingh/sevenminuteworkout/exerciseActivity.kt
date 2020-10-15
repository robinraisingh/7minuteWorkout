package com.robinsingh.sevenminuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_cutom_back_information.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class exerciseActivity : AppCompatActivity(),TextToSpeech.OnInitListener{
    private var restTimer:CountDownTimer?=null  //10 second for rest
    private var onTimer:CountDownTimer?=null   // 30 second for workout
    private var restProgress=0
    private var onProgress=0
    private var onTimeDuration=30

    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePosition:Int=-1   //it will start from 0

    private var tts:TextToSpeech?=null

    private var player:MediaPlayer?=null

    private var exerciseAdapter:ExerciseAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(myToolBar)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        myToolBar.setNavigationOnClickListener {
            customDialogBackButton()
        }

        exerciseList=Constants.defaultExerciseList()
        setupRestView() //this will begin the process of setting the timer up

        tts=TextToSpeech(this,this) //if we donot impliment text to speech then it is not possible to make this activity as listner
        setupExerciseRV()
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown() //if we hit back during the speech it will shut it up
        }

        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0   //this is redundent as it will be set to 0 as we get in
        }
        if(onTimer!=null){
            onTimer!!.cancel()
            onProgress=0   //this is redundent as it will be set to 0 as we get in
        }
        if(player!=null){
            player!!.stop()
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

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

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
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }
                else{
                    finish()

                    var intent=Intent(this@exerciseActivity,finishActivity::class.java)
                    startActivity(intent)
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
        speakOut(exerciseList!![currentExercisePosition].getName())
        setOnProgressBar()
        //setting the image and name of exercise from the exercise list whose pointer is currently on currentExercisePosition
        evImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        exerciseName.text=exerciseList!![currentExercisePosition].getName()
    }

    // REST VIEW SETTING UP FUNCTION

    private fun setupRestView(){

        try{
            player=MediaPlayer.create(applicationContext,R.raw.press_start) //setting up media player
            player!!.start()
            player!!.isLooping=false  //will not repeat

        }catch(e:Exception){
            e.printStackTrace()
        }

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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)  //storing the operation result in val so that we can chack was it successfull or not later on

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }
    private fun speakOut(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
    private fun setupExerciseRV(){
        rvExerciseStatus.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter= ExerciseAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter=exerciseAdapter

    }
    private fun customDialogBackButton(){
        val customDialog= Dialog(this)

        customDialog.setContentView(R.layout.dialog_cutom_back_information)
        customDialog.tvYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

}
