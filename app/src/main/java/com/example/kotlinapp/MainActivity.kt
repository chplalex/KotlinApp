package com.example.kotlinapp

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnOnClick(view: View) {
        txtView.text = "!!! вернусь через 2 секунды !!!"
        btnView.visibility = GONE
        val activity: Activity = this
        val thread = Thread(Runnable {
            Thread.sleep(2000L)
            activity.runOnUiThread(Runnable {
                txtView.text = "Hello world!"
                btnView.visibility = VISIBLE
            })
        })
        thread.isDaemon = true
        thread.start()
    }

    fun imgOnClick(view: View) {
        finish()
    }

}