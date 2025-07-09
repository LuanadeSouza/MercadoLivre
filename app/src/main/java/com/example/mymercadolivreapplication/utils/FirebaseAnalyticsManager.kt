package com.example.mymercadolivreapplication.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FirebaseAnalyticsManager centralizes all Firebase Analytics interactions.
 *
 * Purpose:
 * - Abstracts the FirebaseAnalytics API to make event tracking easier and more maintainable.
 * - Injected via Hilt as a Singleton to ensure consistent use throughout the app.
 * - Promotes usage of clearly defined and reusable tracking methods for each type of event.
 *
 * Usage examples:
 * - logSearchEvent("Smartphones")
 * - logViewItemEvent(itemId = "123", itemName = "iPhone 13", price = 599.99, currency = "BRL")
 */
@Singleton
class FirebaseAnalyticsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Underlying Firebase Analytics instance
    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    /**
     * Generic method to log any custom or predefined event.
     *
     * @param eventName Firebase event name (e.g., FirebaseAnalytics.Event.SEARCH)
     * @param params Optional parameters related to the event (e.g., item_id, screen_name)
     */
    fun logEvent(eventName: String, params: Bundle? = null) {
        firebaseAnalytics.logEvent(eventName, params)
    }

    /**
     * Logs a standardized search event to Firebase Analytics.
     *
     * Important:
     * - Should be used whenever a user submits a search query.
     * - Helps track user intent and popular keywords.
     *
     * @param searchTerm The exact query entered by the user.
     */
    fun logSearchEvent(searchTerm: String) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm)
        }
        logEvent(FirebaseAnalytics.Event.SEARCH, params)
    }

    /**
     * Logs a view_item event when the user views the details of a product.
     *
     * Firebase uses this event to populate e-commerce funnels and conversion tracking.
     *
     * @param itemId Unique identifier of the product
     * @param itemName Name of the product
     * @param itemCategory Optional category (e.g., electronics, fashion)
     * @param price Optional price of the product
     * @param currency Optional currency code (e.g., "BRL", "USD")
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
