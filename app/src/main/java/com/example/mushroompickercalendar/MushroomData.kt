package com.example.mushroompickercalendar

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.mushroompickercalendar.database.MushroomBaseHelper
import com.example.mushroompickercalendar.database.MushroomCursorWrapper
import com.example.mushroompickercalendar.database.MushroomDBSchema
import com.example.mushroompickercalendar.database.MushroomDBSchema.MushroomTable
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MushroomData private constructor(context: Context) {

    var mMushrooms = ArrayList<Mushroom>()
    private lateinit var mContext: Context
    private lateinit var mDatabase: SQLiteDatabase

    init {
        mContext = context.applicationContext
        mDatabase = MushroomBaseHelper(mContext).writableDatabase
    }

    fun getMushroom(id: UUID): Mushroom? {
        val cursor = queryMushrooms(
            MushroomTable.Cols.UUID + " = ?",
            arrayOf(id.toString())
        )
        try {
            if (cursor.count == 0) return null
            cursor.moveToFirst()
            return cursor.getMushroom()
        } finally {
            cursor.close()
        }
    }

    fun addMushroom(mushroom: Mushroom) {
        val values = getContentValues(mushroom)
        mDatabase.insert(MushroomTable.NAME, null, values)
    }

    fun deleteMushroom(mushroom: Mushroom) {
        val whereClause = MushroomTable.Cols.UUID + " = ?"
        mDatabase.delete(MushroomTable.NAME, whereClause, arrayOf(mushroom.mId.toString()))
    }

    fun updateMushroom(mushroom: Mushroom) {
        val uuidString = mushroom.mId.toString()
        val values = getContentValues(mushroom)

        mDatabase.update(
            MushroomTable.NAME, values,
            MushroomTable.Cols.UUID + " = ?",
            arrayOf(uuidString)
        )
    }

    fun getMushrooms(): ArrayList<Mushroom> {
        mMushrooms = ArrayList<Mushroom>()
        val cursor = queryMushrooms(null, null)

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                mMushrooms.add(cursor.getMushroom())
                cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }

        return mMushrooms
    }

    fun getContentValues(mushroom: Mushroom): ContentValues {
        val values = ContentValues()

        values.put(MushroomTable.Cols.UUID, mushroom.mId.toString())
        values.put(MushroomTable.Cols.NAME, mushroom.mName)
        values.put(MushroomTable.Cols.MONTH, mushroom.mMonth)
        values.put(MushroomTable.Cols.FOREST, mushroom.mForestType)
        values.put(MushroomTable.Cols.COOKING, mushroom.mCookingMethod)

        return values
    }

    fun getPhotoFile(mushroom: Mushroom): File {
        val filesDir = mContext.filesDir
        return File(filesDir, mushroom.getPhotoFilename())
    }

    private fun queryMushrooms(
        whereClause: String?,
        whereArgs: Array<String>?
    ): MushroomCursorWrapper {
        val cursor = mDatabase.query(
            MushroomTable.NAME,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            null
        )
        return MushroomCursorWrapper(cursor)
    }

    companion object {

        private var sMushroomData: MushroomData? = null

        fun get(context: Context): MushroomData {
            if (sMushroomData == null) {
                sMushroomData = MushroomData(context)
            }
            return sMushroomData!!
        }
    }

}