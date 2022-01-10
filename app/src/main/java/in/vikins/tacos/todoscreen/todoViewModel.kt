package `in`.vikins.tacos.todoscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class todoViewModel(application: Application):AndroidViewModel(application) {
    val alltask:LiveData<List<task>>
    private val repository: todoRepository
    init {
        val dao = TodoDatabase.getDatabase(application).gettodoDao()
        repository = todoRepository(dao)
        alltask = repository.alltask
    }
    fun deletenotes(task:task)=viewModelScope.launch(Dispatchers.IO){
        repository.delete(task)
    }
    fun addtask(task: task)=viewModelScope.launch (Dispatchers.IO){
        repository.insert(task)
    }
}