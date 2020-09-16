package com.example.mushroompickercalendar

import androidx.fragment.app.Fragment

class MushroomListActivity:SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return MushroomListFragment()
    }
}