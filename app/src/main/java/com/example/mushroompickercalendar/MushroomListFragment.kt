package com.example.mushroompickercalendar

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MushroomListFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: MushroomAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_mushroom_list, container, false)
        mRecyclerView = view.findViewById(R.id.mushroom_recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_mushroom_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_mushroom -> {
                val mushroom = Mushroom()
                MushroomData.get(context!!).addMushroom(mushroom)
//                val intent = MushroomEditActivity.newIntent(context!!, mushroom.mId)
                val intent = MushroomActivity.newIntent(context!!, mushroom.mId, true)
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val mushroomData = MushroomData.get(context!!)
        val mushrooms = mushroomData.getMushrooms()

        if (mAdapter == null) {
            mAdapter = MushroomAdapter(mushrooms)
            mRecyclerView.adapter = mAdapter
        } else {
            mAdapter?.setMushrooms(mushrooms)
            mAdapter?.notifyDataSetChanged()
        }
    }

    inner private class MushroomHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_mushroom, parent, false)),
        View.OnClickListener {

        private lateinit var mMushroom: Mushroom
        private lateinit var mNameTextView: TextView

        init {
            mNameTextView = itemView.findViewById(R.id.mushroom_name)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = MushroomPagerActivity.newIntent(context!!, mMushroom.mId)
            startActivity(intent)
        }

        fun bind(mushroom: Mushroom) {
            mMushroom = mushroom
            mNameTextView.text = mushroom.mName
        }
    }

    inner private class MushroomAdapter(private var mMushrooms: List<Mushroom>) :
        RecyclerView.Adapter<MushroomHolder?>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MushroomHolder {
            val layoutInflater = LayoutInflater.from(context)
            return MushroomHolder(layoutInflater, parent)
        }

        override fun getItemCount(): Int {
            return mMushrooms.size
        }

        override fun onBindViewHolder(holder: MushroomHolder, position: Int) {
            val mushroom = mMushrooms.get(position)
            holder.bind(mushroom)
        }

        fun setMushrooms(mushrooms: ArrayList<Mushroom>) {
            mMushrooms = mushrooms
        }
    }

}