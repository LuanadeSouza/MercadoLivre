package com.example.mymercadolivreapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the response returned by the search API.
 *
 * @property siteId The site identifier, typically "MLA" for Mercado Libre.
 * @property query The search query used for the search operation.
 * @property paging The paging details of the response.
 * @property results A list of products found based on the search query.
 */
data class SearchResponse(
    @SerializedName("site_id")
    val siteId: String,
    @SerializedName("query")
    val query: String?,
    @SerializedName("paging")
    val paging: Paging,
    @SerializedName("results")
    val results: List<Product>
)

/**
 * Represents paging information for the search results.
 *
 * @property total Total number of results available.
 * @property offset The current offset for the pagination.
 * @property limit The number of results per page.
 * @property primaryResults The number of primary results.
 */
data class Paging(
    @SerializedName("total")
    val total: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("primary_results")
    val primaryResults: Int
)

/**
 * Represents a product found in the search results.
 *
 * @property id The unique identifier of the product.
 * @property title The title or name of the product.
 * @property price The price of the product.
 * @property currencyId The currency used for the price (e.g., ARS, USD).
 * @property availableQuantity The available quantity of the product.
 * @property thumbnail The URL of the product's thumbnail image.
 * @property condition The condition of the product (e.g., new, used).
 * @property shipping Shipping information related to the product.
 * @property seller Information about the seller offering the product.
 * @property attributes The list of product attributes (optional).
 */
data class Product(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("currency_id")
    val currencyId: String,
    @SerializedName("available_quantity")
    val availableQuantity: Int,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("shipping")
    val shipping: Shipping?,
    @SerializedName("seller")
    val seller: Seller?,
    @SerializedName("attributes")
    val attributes: List<Attribute>?
)

/**
 * Represents shipping information for a product.
 *
 * @property freeShipping Indicates whether the product is eligible for free shipping.
 */
data class Shipping(
    @SerializedName("free_shipping")
    val freeShipping: Boolean
)

/**
 * Represents the seller offering the product.
 *
 * @property id The unique identifier of the seller.
 * @property nickname The nickname or username of the seller.
 */
data class Seller(
    @SerializedName("id")
    val id: Long,
    @SerializedName("nickname")
    val nickname: String
)

/**
 * Represents a product attribute, such as brand or model.
 *
 * @property id The unique identifier of the attribute.
 * @property name The name of the attribute (e.g., brand, model).
 * @property valueName The value of the attribute (e.g., Samsung, AU8000).
 */
data class Attribute(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("value_name")
    val valueName: String?
)

