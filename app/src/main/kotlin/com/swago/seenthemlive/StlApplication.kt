package com.swago.seenthemlive

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StlApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
