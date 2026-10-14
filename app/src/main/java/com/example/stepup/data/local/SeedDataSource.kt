package com.example.stepup.data.local

import android.content.res.AssetManager
import com.example.stepup.data.model.Task

class SeedDataSource(
    private val assets: AssetManager,
    private val codec: TaskJsonCodec
) {

    fun loadTasks(): List<Task> {
        val json = assets.open("tasks_seed.json")
            .bufferedReader()
            .use { it.readText() }

        return codec.decode(json)
    }
}

