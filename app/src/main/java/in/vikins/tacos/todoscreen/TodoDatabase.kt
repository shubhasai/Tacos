package `in`.vikins.tacos.todoscreen

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(task::class), version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun gettodoDao():todoDao
    companion object{
        @Volatile
        private var INSTANCE:TodoDatabase?=null
        fun getDatabase(context: Context): TodoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todolist"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}