package com.example.mushroompickercalendar

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import java.util.*

class MushroomEditActivity:SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        val mushroomId = intent.getSerializableExtra(EXTRA_MUSHROOM_ID) as UUID
        return MushroomEditFragment.newInstance(mushroomId)
    }

    companion object {

        private const val EXTRA_MUSHROOM_ID = "com.example.mushroompickercalendar.mushroom_id"

        fun newIntent(context: Context, mushroomId: UUID): Intent {
            val intent = Intent(context, MushroomEditActivity::class.java)
            intent.putExtra(EXTRA_MUSHROOM_ID, mushroomId)
            return intent
        }
    }
}