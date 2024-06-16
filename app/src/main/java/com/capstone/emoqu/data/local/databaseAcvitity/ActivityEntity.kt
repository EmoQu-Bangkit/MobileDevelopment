package com.capstone.emoqu.data.local.databaseAcvitity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ActivityEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "time_stamp")
    var date: String,

    @ColumnInfo(name = "quality")
    var quality: Int = 0,

    @ColumnInfo(name = "activity")
    var activity: String,

    @ColumnInfo(name = "duration")
    var duration: Int,

    @ColumnInfo(name = "notes")
    var notes: String? = null
) : Parcelable