package br.com.polenflorestal.qrcodecmpc.utils

import android.app.Application
import android.content.Context

class MyApp : Application() {

    companion object {
        lateinit var context: Context
        fun getAppContext():Context { return context
        }
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }

}