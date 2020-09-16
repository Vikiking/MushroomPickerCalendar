package com.example.mushroompickercalendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*
import kotlin.collections.ArrayList

class MushroomPagerActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private var mPagerAdapter: MushroomPagerAdapter? = null
    private var mMushrooms = ArrayList<Mushroom>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mushroom_pager)

        val mushroomId = intent.getSerializableExtra(EXTRA_MUSHROOM_ID) as UUID

        mViewPager = findViewById(R.id.mushroom_view_pager)
        mMushrooms = MushroomData.get(this).getMushrooms()

        val fragmentManager = supportFragmentManager
        mPagerAdapter = MushroomPagerAdapter(fragmentManager)
        mViewPager.adapter = mPagerAdapter

        for (i in 0 until mMushrooms.size) {
            if (mMushrooms.get(i).mId.equals(mushroomId)) {
                mViewPager.setCurrentItem(i)
                break
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        if (mPagerAdapter == null) {
            val fragmentManager = supportFragmentManager
            mPagerAdapter = MushroomPagerAdapter(fragmentManager)
            mViewPager.adapter = mPagerAdapter
        } else {
            mPagerAdapter?.notifyDataSetChanged()
            mViewPager.invalidate()
        }
    }

    inner private class MushroomPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            val mushroom = mMushrooms.get(position)
            return MushroomFragment.newInstance(mushroom.mId, false)
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

        override fun getCount(): Int {
            return mMushrooms.size
        }
    }

    companion object {
        private const val EXTRA_MUSHROOM_ID = "com.example.mushroompickercalendar.mushroom_id"

        fun newIntent(packageContext: Context, mushroomId: UUID): Intent {
            val intent = Intent(packageContext, MushroomPagerActivity::class.java)
            intent.putExtra(EXTRA_MUSHROOM_ID, mushroomId)
            return intent
        }
    }

}