package com.example.mymercadolivreapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents detailed information about a product.
 *
 * @property id The unique identifier of the product.
 * @property title The title or name of the product.
 * @property price The price of the product.
 * @property currencyId The currency used for the price (e.g., ARS, USD).
 * @property availableQuantity The available quantity of the product.
 * @property condition The condition of the product (e.g., new, used).
 * @property pictures A list of pictures of the product.
 * @property shipping Shipping information related to the product.
 * @property sellerId The unique identifier of the seller.
 * @property categoryId The category ID of the product.
 * @property attributes The list of product attributes (optional).
 * @property warranty Warranty information for the product (optional).
 * @property soldQuantity The quantity of products sold.
 */
data class ProductDetail(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("original_price")
    val originalPrice: Double? = null,
    @SerializedName("currency_id")
    val currencyId: String,
    @SerializedName("available_quantity")
    val availableQuantity: Int,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("pictures")
    val pictures: List<Picture>,
    @SerializedName("shipping")
    val shipping: Shipping?,
    @SerializedName("seller_id")
    val sellerId: Long,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("attributes")
    val attributes: List<Attribute>?,
    @SerializedName("warranty")
    val warranty: String?,
    @SerializedName("sold_quantity")
    val soldQuantity: Int
)

/**
 * Represents a picture of the product.
 *
 * @property id The unique identifier of the picture.
 * @property url The URL of the picture.
 * @property secureUrl The secure URL of the picture.
 * @property size The size of the picture.
 * @property maxSize The maximum size of the picture.
 */
data class Picture(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("secure_url")
    val secureUrl: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("max_size")
    val maxSize: String
)