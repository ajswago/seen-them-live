package com.swago.seenthemlive

import android.app.Application
import com.testfairy.TestFairy


class SeenThemLiveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TestFairy.begin(this, BuildConfig.TESTFAIRY_KEY)
    }
}
