package com.example.mymercadolivreapplication.utils

/**
 * Sealed class representing the different states of an asynchronous operation.
 *
 * Why use a sealed class?
 * - Ensures that all possible states are handled (Loading, Success, Error)
 * - Allows safe pattern matching with 'when' expressions
 * - Facilitates consistent state handling in the UI
 * - Prevents bugs related to unhandled states
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Loading state - used when an operation is in progress.
     * It can optionally contain data if needed.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * Success state - used when an operation has completed successfully.
     * @param data The data returned by the operation.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Error state - used when an operation has failed.
     * @param message The error message to display to the user.
     * @param data Partial data that may still be available even in case of failure.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}