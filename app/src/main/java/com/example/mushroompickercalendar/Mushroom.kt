package com.example.mushroompickercalendar

import android.content.res.TypedArray
import java.util.*
import kotlin.collections.ArrayList

class Mushroom @JvmOverloads constructor(var mId: UUID = UUID.randomUUID()) {

    var mName: String? = null
    var mMonth: String? = null
    var mForestType: String? = null
    var mCookingMethod: String? = null

    fun getMonthsList(): Array<String>? {
        return mMonth?.split(";")?.toTypedArray()
    }

    fun getForestTypeList(): Array<String>? {
        return mForestType?.split(";")?.toTypedArray()
    }

    fun getCookingMethodList(): Array<String>? {
        return mCookingMethod?.split(";")?.toTypedArray()
    }

    fun getPhotoFilename(): String {
        return "IMG_${mId.toString()}.jpg"
    }

}

