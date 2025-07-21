package com.example.mymercadolivreapplication.utils

import android.content.Context
import timber.log.Timber
import java.io.IOException
import java.nio.charset.Charset


/**
 * Utility class for loading files from the 'assets' folder.
 */
object AssetUtils {

    /**
     * Loads a JSON file from the assets directory.
     *
     * @param context The application context.
     * @param fileName The name of the JSON file to be loaded.
     * @return The content of the JSON file as a String.
     * @throws IOException If there is a failure loading the file.
     */
    fun loadJsonFromAssets(context: Context, fileName: String): String {
        return try {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charset.forName("UTF-8"))
            // Adding log to check if the file is loaded correctly
            Timber.d("JSON Loaded Successfully: $json")
            json
        } catch (e: IOException) {
            Timber.e(e, "Error loading JSON file: $fileName")
            throw IOException("Failed to load the file $fileName from the assets folder.", e)
        }
    }
}