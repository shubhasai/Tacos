package `in`.vikins.tacos.todoscreen

import `in`.vikins.tacos.todoscreen.task
import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.android.gms.tasks.Task

@Dao
interface todoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: task)
    @Delete
    suspend fun delete(todo: task)
    @Query(value = "Select * from todolist order by id DESC ")
    fun getAll():LiveData<List<task>>
}