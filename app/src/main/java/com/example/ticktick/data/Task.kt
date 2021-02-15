package com.example.ticktick.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

/**
  One single Activity the main Activity which will be basically container for different fragments
 **/

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val name: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0    
): Parcelable {

    //changing date format
    val createdDateFormatted: String
        get() = DateFormat.getDateInstance().format(created)

}