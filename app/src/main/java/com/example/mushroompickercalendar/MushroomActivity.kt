package com.example.mushroompickercalendar

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import java.util.*

class MushroomActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        val mushroomId = intent.getSerializableExtra(EXTRA_MUSHROOM_ID) as UUID
        val fromRecyclerView = intent.getSerializableExtra(EXTRA_RECYCLER_VIEW) as Boolean
        return MushroomFragment.newInstance(mushroomId, fromRecyclerView)
    }

    companion object {

        const val EXTRA_RECYCLER_VIEW = "com.example.mushroompickercalendar.from_recycler_view"
        private const val EXTRA_MUSHROOM_ID = "com.example.mushroompickercalendar.mushroom_id"

        fun newIntent(context: Context, mushroomId: UUID, fromRecyclerView: Boolean): Intent {
            val intent = Intent(context, MushroomActivity::class.java)
            intent.putExtra(EXTRA_MUSHROOM_ID, mushroomId)
            intent.putExtra(EXTRA_RECYCLER_VIEW, fromRecyclerView)
            return intent
        }
    }

}
