package com.yelysei.hobbyharbor.sources.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yelysei.foundation.model.BackendException
import com.yelysei.foundation.model.ConnectionException
import com.yelysei.foundation.model.ParseBackendResponseException
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

open class BaseOkHttpSource(
    private val config: OkHttpConfig
) {

    val gson: Gson = config.gson
    val client: OkHttpClient = config.client

    private val contentType = "application/json; charset=utf-8".toMediaType()

    /**
     * Suspending function which wraps OkHttp [Call.enqueue] method for making
     * HTTP requests and wraps external exceptions into subclasses for [AppException].
     *
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     * */
    suspend fun Call.suspendEnqueue(): Response {
        return suspendCancellableCoroutine { continuation ->
            // when coroutine cancel the http request cancel also
            continuation.invokeOnCancellation {
                cancel()
            }
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val appException = ConnectionException(e)
                    continuation.resumeWithException(appException)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        continuation.resume(response)
                    } else {
                        handleErrorResponse(response, continuation)
                    }
                }
            })
        }
    }

    private fun handleErrorResponse(response: Response, continuation: CancellableContinuation<Response>) {
        val httpCode = response.code
        try {
            val map = gson.fromJson(response.body!!.string(), Map::class.java)
            val message = map["error"].toString()
            continuation.resumeWithException(BackendException(httpCode, message))
        } catch (e: Exception) {
            val appException = ParseBackendResponseException(e)
            continuation.resumeWithException(appException)
        }
    }

    fun Request.Builder.endpoint(endpoint: String): Request.Builder {
        url("${config.baseUrl}$endpoint")
        return this
    }

    /**
     * Convert data class into [RequestBody] in JSON-format
     */
    fun <T> T.toJsonRequestBody(): RequestBody {
        val json = gson.toJson(this)
        return json.toRequestBody(contentType)
    }

    /**
     * Parse OkHttp [Response] instance into data object. The type is derived from
     * TypeToken passed to this function as a second argument. Usually this method is
     * used to parse JSON arrays.
     *
     * @throws ParseBackendResponseException
     */
    fun <T> Response.parseJsonResponse(typeToken: TypeToken<T>): T {
        try {
            return gson.fromJson(this.body!!.string(), typeToken.type)
        } catch (e: Exception){
            throw ParseBackendResponseException(e)
        }
    }

    /**
     * Parse OkHttp [Response] instance into data object. The type is derived from
     * the generic type [T]. Usually this method is used to parse JSON objects.
     *
     * @throws ParseBackendResponseException
     */
    inline fun <reified T> Response.parseJsonResponse(): T {
        try {
            return gson.fromJson(this.body!!.string(), T::class.java)
        } catch (e: Exception){
            throw ParseBackendResponseException(e)
        }
    }
}