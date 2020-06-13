package com.swago.seenthemlive.ui.setlist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swago.seenthemlive.R
import com.swago.seenthemlive.api.setlistfm.Setlist

class SetlistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setlist_activity)
        if (savedInstanceState == null) {
            val setlist: Setlist = intent.getSerializableExtra(INTENT_SETLIST) as Setlist
            val showOthersAtShow = intent.getBooleanExtra(INTENT_SHOW_OTHERS_AT_SHOW, true)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SetlistFragment.newInstance(setlist, showOthersAtShow))
                .commitNow()
        }
    }

    companion object {

        private const val INTENT_SETLIST = "setlist"
        private const val INTENT_SHOW_OTHERS_AT_SHOW = "showOthersAtShow"

        fun newIntent(context: Context,
                      setlist: Setlist,
                      showOthersAtShow: Boolean? = true): Intent {
            val intent = Intent(context, SetlistActivity::class.java)
            intent.putExtra(INTENT_SETLIST, setlist)
            intent.putExtra(INTENT_SHOW_OTHERS_AT_SHOW, showOthersAtShow)
            return intent
        }
    }
}