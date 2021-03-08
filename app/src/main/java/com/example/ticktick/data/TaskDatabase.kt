package com.example.ticktick.data

import android.content.pm.ApplicationInfo
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ticktick.DependencyInjection.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
      private val database: Provider<TaskDatabase>,
      @ApplicationScope private val applicationScope: CoroutineScope  // ot just CoroutineScope but ApplicationScope
    ) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            // coroutine is a piece of code that actually knows how to suspend execution and let the program continue with something else
            applicationScope.launch {
                dao.insert(Task("Solve DSA Problems"))
                dao.insert(Task("Git commit to ECommerce repository", important = true))
                dao.insert(Task("Call home", completed = true))
                dao.insert(Task("Go for launch"))
                dao.insert(Task("Apply for a job"))
                dao.insert(Task("Call Elon Musk"))
            }

        }
    }

}