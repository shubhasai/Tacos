package `in`.vikins.tacos.todoscreen

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class todoRepository (private val todoDao: todoDao){
    val alltask:LiveData<List<task>> = todoDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(tast:task){
        todoDao.insert(tast)
    }
    suspend fun delete(tast:task){
        todoDao.delete(tast)
    }

}