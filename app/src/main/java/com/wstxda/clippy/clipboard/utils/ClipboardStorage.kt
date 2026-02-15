package com.wstxda.clippy.clipboard.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.edit
import com.wstxda.clippy.clipboard.data.ClipboardCategory
import com.wstxda.clippy.clipboard.data.ClipboardLinkItem
import com.wstxda.clippy.utils.Constants

class ClipboardStorage private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    suspend fun getSavedLinks(): List<ClipboardLinkItem> = withContext(Dispatchers.IO) {
        val json = prefs.getString(Constants.KEY_SAVED_LINKS, "[]") ?: "[]"
        parseLinksJson(json, ClipboardCategory.SAVED)
    }

    suspend fun getHistoryLinks(): List<ClipboardLinkItem> = withContext(Dispatchers.IO) {
        val json = prefs.getString(Constants.KEY_HISTORY_LINKS, "[]") ?: "[]"
        parseLinksJson(json, ClipboardCategory.HISTORY)
    }

    suspend fun saveLink(url: String) = withContext(Dispatchers.IO) {
        val currentLinks = getSavedLinks().toMutableList()
        val newLink = ClipboardLinkItem(id = url, url = url, category = ClipboardCategory.SAVED)

        currentLinks.removeAll { it.url == url }
        currentLinks.add(0, newLink)

        saveLinksList(Constants.KEY_SAVED_LINKS, currentLinks)
    }

    suspend fun addToHistory(url: String) = withContext(Dispatchers.IO) {
        val currentLinks = getHistoryLinks().toMutableList()
        val newLink = ClipboardLinkItem(id = url, url = url, category = ClipboardCategory.HISTORY)

        currentLinks.removeAll { it.url == url }
        currentLinks.add(0, newLink)

        if (currentLinks.size > Constants.MAX_HISTORY_SIZE) {
            currentLinks.subList(Constants.MAX_HISTORY_SIZE, currentLinks.size).clear()
        }

        saveLinksList(Constants.KEY_HISTORY_LINKS, currentLinks)
    }

    suspend fun removeFromSaved(id: String) = withContext(Dispatchers.IO) {
        val currentLinks = getSavedLinks().toMutableList()
        currentLinks.removeAll { it.id == id }
        saveLinksList(Constants.KEY_SAVED_LINKS, currentLinks)
    }

    suspend fun removeFromHistory(id: String) = withContext(Dispatchers.IO) {
        val currentLinks = getHistoryLinks().toMutableList()
        currentLinks.removeAll { it.id == id }
        saveLinksList(Constants.KEY_HISTORY_LINKS, currentLinks)
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        prefs.edit { remove(Constants.KEY_HISTORY_LINKS) }
    }

    private fun parseLinksJson(json: String, category: ClipboardCategory): List<ClipboardLinkItem> {
        val links = mutableListOf<ClipboardLinkItem>()
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val url = jsonObject.getString("url")
                links.add(
                    ClipboardLinkItem(
                        id = jsonObject.optString("id", url), // Fallback to url if id is missing
                        url = url,
                        timestamp = jsonObject.getLong("timestamp"),
                        category = category
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return links
    }

    private fun saveLinksList(key: String, links: List<ClipboardLinkItem>) {
        val jsonArray = JSONArray()
        links.forEach { link ->
            val jsonObject = JSONObject().apply {
                put("id", link.id)
                put("url", link.url)
                put("timestamp", link.timestamp)
            }
            jsonArray.put(jsonObject)
        }
        prefs.edit { putString(key, jsonArray.toString()) }
    }
}