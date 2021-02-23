package com.example.ticktick.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao  //Data Access Objects (Dao) are the main classes where you define your database interactions. They can include a variety of query methods.
interface TaskDao {

    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY important DESC")
    fun getTasks(searchQuery: String): Flow<List<Task>>  // Flow represents stream of data and Flow can only be used inside the coroutines that is why we don't need suspend modifier
    //Flow is asynchronous stream of data

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task) // creates new thread for room suspend function

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}