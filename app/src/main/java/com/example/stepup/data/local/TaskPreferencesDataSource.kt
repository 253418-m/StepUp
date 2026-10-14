package com.example.stepup.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.stepup.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.taskDataStore: DataStore<Preferences>
        by preferencesDataStore(name = "stepup_tasks")

class TaskPreferencesDataSource(
    context: Context,
    private val codec: TaskJsonCodec
) {

    private val dataStore =
        context.applicationContext.taskDataStore

    private companion object {
        val TASKS_KEY = stringPreferencesKey("tasks_json")
    }

    val storedTasks: Flow<List<Task>?> =
        dataStore.data.map { preferences ->

            val json = preferences[TASKS_KEY]

            if (json == null) {
                null
            } else {
                codec.decode(json)
            }
        }

    suspend fun updateTasks(
        seedTasks: List<Task>,
        transform: (List<Task>) -> List<Task>
    ) {
        dataStore.edit { preferences ->

            val savedJson = preferences[TASKS_KEY]

            val currentTasks = if (savedJson == null) {
                seedTasks
            } else {
                codec.decode(savedJson)
            }

            val updatedTasks = transform(currentTasks)

            preferences[TASKS_KEY] =
                codec.encode(updatedTasks)
        }
    }
}



