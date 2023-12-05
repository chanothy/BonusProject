package project.c323.bonusproject

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Schema for the notes database
 */

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,
    @ColumnInfo(name = "task_name")
    var taskName: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "time")
    var time: String = "",
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "task_done")
    var taskDone: Boolean = false
)
