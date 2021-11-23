package com.example.paging3app

import android.app.Application

class App : Application() {
    companion object {
        private var appInstance : App? = null

        fun getAppInstance() = appInstance
    }

    override fun onCreate() {
        super.onCreate()
        if (appInstance == null) {
            appInstance = this
        }
    }
}