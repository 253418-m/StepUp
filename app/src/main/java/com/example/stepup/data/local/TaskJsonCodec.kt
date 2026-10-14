package com.example.stepup.data.local

import com.example.stepup.data.model.Task
import org.json.JSONArray
import org.json.JSONObject

class TaskJsonCodec {

    fun decode(json: String): List<Task> {
        val array = JSONArray(json)

        return (0 until array.length()).map { index ->
            val item = array.getJSONObject(index)

            Task(
                id = item.getString("id"),
                title = item.getString("title"),
                category = item.getString("category"),
                priority = item.getInt("priority"),
                effort = item.getInt("effort"),
                estimatedMinutes =
                    item.getInt("estimatedMinutes"),
                isCompleted =
                    item.getBoolean("isCompleted")
            )
        }
    }

    fun encode(tasks: List<Task>): String {
        val array = JSONArray()

        tasks.forEach { task ->
            val item = JSONObject()
                .put("id", task.id)
                .put("title", task.title)
                .put("category", task.category)
                .put("priority", task.priority)
                .put("effort", task.effort)
                .put(
                    "estimatedMinutes",
                    task.estimatedMinutes
                )
                .put("isCompleted", task.isCompleted)

            array.put(item)
        }

        return array.toString()
    }
}
