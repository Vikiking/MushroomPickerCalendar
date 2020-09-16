package com.example.mushroompickercalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.mushroom_image_activity.*
import java.io.File
import java.util.*

class MushroomImageActivity : AppCompatActivity() {

    private var mMushroom: Mushroom? = null
    private lateinit var mPhotoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mushroom_image_activity)

        val mushroomId = intent.getSerializableExtra(EXTRA_MUSHROOM_ID) as UUID
        mMushroom = MushroomData.get(this).getMushroom(mushroomId)
        mPhotoFile = MushroomData.get(this).getPhotoFile(mMushroom!!)
        val bitmap = PictureUtils.getScaledBitmap(mPhotoFile.path, this)
        mushroom_full_image.setImageBitmap(bitmap)
    }

    companion object {
        const val EXTRA_MUSHROOM_ID = "com.example.mushroompickercalendar.mushroom_id"
    }

}
