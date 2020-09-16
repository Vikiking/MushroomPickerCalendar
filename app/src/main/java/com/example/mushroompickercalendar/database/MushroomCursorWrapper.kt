package com.example.mushroompickercalendar.database

import android.database.Cursor
import android.database.CursorWrapper
import com.example.mushroompickercalendar.Mushroom
import com.example.mushroompickercalendar.database.MushroomDBSchema.MushroomTable
import java.util.*

class MushroomCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    fun getMushroom(): Mushroom {
        val uuidString = getString(getColumnIndex(MushroomTable.Cols.UUID))
        val name = getString(getColumnIndex(MushroomTable.Cols.NAME))
        val month = getString(getColumnIndex(MushroomTable.Cols.MONTH))
        val forest = getString(getColumnIndex(MushroomTable.Cols.FOREST))
        val cooking = getString(getColumnIndex(MushroomTable.Cols.COOKING))

        val mushroom = Mushroom(UUID.fromString(uuidString))
        mushroom.mName = name
        mushroom.mMonth = month
        mushroom.mForestType = forest
        mushroom.mCookingMethod = cooking

        return mushroom
    }

}