package com.example.mymercadolivreapplication.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for Firebase Analytics.
 *
 * Why use a manager?
 * - Centralizes event tracking logic, making maintenance and addition of new events easier.
 * - Abstracts the complexity of the Firebase Analytics API, simplifying usage for ViewModels.
 * - Allows FirebaseAnalytics to be injected via Hilt, ensuring a single instance is used (Singleton).
 * - Makes it easier to disable or substitute Analytics in different environments (e.g., tests).
 */
@Singleton
class FirebaseAnalyticsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    /**
     * Logs an event to Firebase Analytics.
     *
     * @param eventName The name of the event to be logged. It's recommended to use predefined Firebase event names
     *                  or follow clear naming conventions (e.g., snake_case).
     * @param params An optional Bundle of parameters for the event. Can be null if there are no parameters.
     */
    fun logEvent(eventName: String, params: Bundle? = null) {
        firebaseAnalytics.logEvent(eventName, params)
    }

    /**
     * Logs a search event.
     *
     * @param searchTerm The search term used by the user.
     */
    fun logSearchEvent(searchTerm: String) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm)
        }
        logEvent(FirebaseAnalytics.Event.SEARCH, params)
    }

    /**
     * Logs a view item event.
     *
     * @param itemId The ID of the viewed item.
     * @param itemName The name of the viewed item.
     * @param itemCategory The category of the viewed item.
     * @param price The price of the viewed item.
     * @param currency The currency of the price of the item.
     */
    fun logViewItemEvent(
        itemId: String,
        itemName: String,
        itemCategory: String? = null,
        price: Double? = null,
        currency: String? = null
    ) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, itemName)
            itemCategory?.let { putString(FirebaseAnalytics.Param.ITEM_CATEGORY, it) }
            price?.let { putDouble(FirebaseAnalytics.Param.PRICE, it) }
            currency?.let { putString(FirebaseAnalytics.Param.CURRENCY, it) }
        }
        logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params)
    }
}