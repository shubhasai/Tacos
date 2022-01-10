package `in`.vikins.tacos.todoscreen

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todolist")
class task (@ColumnInfo(name = "word") val dos: String){
    @PrimaryKey(autoGenerate = true) var id: Int =0
}