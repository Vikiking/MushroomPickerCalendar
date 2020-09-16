package com.example.mushroompickercalendar.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mushroompickercalendar.database.MushroomDBSchema.MushroomTable

class MushroomBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "create table " + MushroomTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    MushroomTable.Cols.UUID + ", " +
                    MushroomTable.Cols.NAME + ", " +
                    MushroomTable.Cols.MONTH + ", " +
                    MushroomTable.Cols.FOREST + ", " +
                    MushroomTable.Cols.COOKING +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        private const val VERSION = 1
        private const val DATABASE_NAME = "mushroomBase.db"
    }
}