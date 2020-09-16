package com.example.mushroompickercalendar.database

class MushroomDBSchema {
    object MushroomTable {
        const val NAME = "mushrooms"

        object Cols {
            const val UUID = "uuid"
            const val NAME = "name"
            const val MONTH = "month"
            const val FOREST = "forest"
            const val COOKING = "cooking"
        }
    }
}