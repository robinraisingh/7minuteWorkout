package com.robinsingh.sevenminuteworkout

import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_finish.*
import java.text.SimpleDateFormat
import java.util.*

class finishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        btnFinish.setOnClickListener {
            finish()
        }
        addDateToDatabase()
    }
    /**
     * Function is used to insert the current system date in the sqlite database.
     */
    private fun addDateToDatabase() {

        val c = Calendar.getInstance() // Calender Current Instance
        val dateTime = c.time // Current Date and Time of the system.
        Log.e("Date : ", "" + dateTime) // Printed in the log.

        /**
         * Here we have taken an instance of Date Formatter as it will format our
         * selected date in the format which we pass it as an parameter and Locale.
         * Here I have passed the format as dd MMM yyyy HH:mm:ss.
         *
         * The Locale : Gets the current value of the default locale for this instance
         * of the Java Virtual Machine.
         */
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()) // Date Formatter
        val date = sdf.format(dateTime) // dateTime is formatted in the given format.
        Log.e("Formatted Date : ", "" + date) // Formatted date is printed in the log.

        // Instance of the Sqlite Open Helper class.
        val dbHandler = SqliteOpenHelper(this, null)
        dbHandler.addDate(date) // Add date function is called.
        Log.e("Date : ", "Added...") // Printed in log which is printed if the complete execution is done.
    }


}