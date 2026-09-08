package com.example.data.sync

import com.example.data.local.LearnedObjectEntity
import com.example.ml.LearnedObjectManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Manages global sharing and automatic cloud synchronization of learned objects across all users.
 * Whenever any user teaches an object, its compact 48-dimensional fingerprint (~250 bytes)
 * is automatically published to the global cloud pool, and downloaded by all other users.
 */
class CloudLearnedObjectSync(
    private val learnedObjectManager: LearnedObjectManager,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    companion object {
        // Dedicated persistent ObjectLens global pool over HTTPS
        private const val GLOBAL_CLOUD_ENDPOINT = "https://extendsclass.com/api/json-storage/bin/aaedcec"
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Serializes locally learned objects into an interchangeable JSON string.
     * [includePrepopulated] can be set to false to only export user-taught custom objects.
     */
    suspend fun exportToJson(includePrepopulated: Boolean = true): String = withContext(Dispatchers.Default) {
        val allObjects = learnedObjectManager.getAllLearned()
        val objects = if (includePrepopulated) allObjects else allObjects.filter { !it.id.startsWith("pre_") }
        val jsonArray = JSONArray()

        for (obj in objects) {
            val json = JSONObject().apply {
                put("id", obj.id)
                put("name", obj.name)
                put("category", obj.category)
                put("featureVector", obj.featureVector)
                put("sampleCount", obj.sampleCount)
                put("createdAt", obj.createdAt)
                put("lastRecognizedAt", obj.lastRecognizedAt)
                put("notes", obj.notes)
            }
            jsonArray.put(json)
        }

        jsonArray.toString(2)
    }

    /**
     * Imports and merges learned objects from a JSON string.
     * Returns the count of objects merged.
     */
    suspend fun importFromJson(jsonStr: String): Int = withContext(Dispatchers.IO) {
        try {
            val jsonArray = JSONArray(jsonStr.trim())
            val entities = mutableListOf<LearnedObjectEntity>()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val id = if (item.has("id")) item.getString("id") else UUID.randomUUID().toString()
                val name = item.getString("name")
                val category = if (item.has("category")) item.getString("category") else "Learned"
                val vector = item.getString("featureVector")
                val sampleCount = if (item.has("sampleCount")) item.getInt("sampleCount") else 1
                val createdAt = if (item.has("createdAt")) item.getLong("createdAt") else System.currentTimeMillis()
                val lastRecognizedAt = if (item.has("lastRecognizedAt")) item.getLong("lastRecognizedAt") else System.currentTimeMillis()
                val notes = if (item.has("notes")) item.getString("notes") else ""

                entities.add(
                    LearnedObjectEntity(
                        id = id,
                        name = name,
                        category = category,
                        featureVector = vector,
                        sampleCount = sampleCount,
                        createdAt = createdAt,
                        lastRecognizedAt = lastRecognizedAt,
                        notes = notes
                    )
                )
            }

            learnedObjectManager.mergeObjects(entities)
        } catch (_: Exception) {
            0
        }
    }

    /**
     * Automatically called when an object is taught on this device.
     * Pushes the updated learned memory to the global cloud pool in background.
     */
    fun publishObjectToCloud(entity: LearnedObjectEntity) {
        scope.launch(Dispatchers.IO) {
            try {
                // Two-way sync: fetch existing remote objects first, merge, and re-upload
                syncWithCloud { _, _, _ -> }
            } catch (_: Exception) {}
        }
    }

    /**
     * Performs a bidirectional sync with the Global Cloud Pool:
     * 1. Fetches all objects taught by other users globally.
     * 2. Merges them into local visual memory (Room database).
     * 3. Uploads any local objects so other users can see them.
     */
    suspend fun syncWithCloud(
        onResult: (success: Boolean, count: Int, message: String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            // 1. Fetch remote shared objects (GET)
            val getRequest = Request.Builder()
                .url(GLOBAL_CLOUD_ENDPOINT)
                .get()
                .build()

            val getResponse = httpClient.newCall(getRequest).execute()
            if (getResponse.isSuccessful) {
                val responseBody = getResponse.body?.string()
                if (!responseBody.isNullOrBlank()) {
                    importFromJson(responseBody)
                }
            }
            getResponse.close()

            // 2. Upload custom user-taught objects to cloud pool (PUT)
            val customJson = exportToJson(includePrepopulated = false)
            val requestBody = customJson.toRequestBody(JSON_MEDIA_TYPE)
            val putRequest = Request.Builder()
                .url(GLOBAL_CLOUD_ENDPOINT)
                .put(requestBody)
                .build()

            val putResponse = httpClient.newCall(putRequest).execute()
            putResponse.close()

            val totalCount = learnedObjectManager.getObjectCount()
            onResult(
                true,
                totalCount,
                "Synchronized $totalCount object(s) with the global cloud pool!"
            )
        } catch (e: Exception) {
            val localCount = learnedObjectManager.getObjectCount()
            onResult(
                false,
                localCount,
                "Using offline memory ($localCount objects). Cloud sync will resume when online."
            )
        }
    }
}
