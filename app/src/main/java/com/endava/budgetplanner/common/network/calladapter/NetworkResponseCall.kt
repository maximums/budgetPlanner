package com.endava.budgetplanner.common.network.calladapter

import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.data.models.ErrorBody
import com.google.gson.Gson
import okhttp3.Request
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val UNKNOWN_ERROR = "Unknown error"
private const val UNSUPPORTED_OPERATION_MESSAGE = "NetworkResponseCall doesn't support execute"

class NetworkResponseCall<T : Any>(
    private val delegate: Call<T>
) : Call<Resource<T>> {

    override fun clone(): Call<Resource<T>> = NetworkResponseCall(delegate.clone())

    override fun execute(): Response<Resource<T>> {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun enqueue(callback: Callback<Resource<T>>) {
        return delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(Resource.Success(body))
                        )
                    } else
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(Resource.SuccessEmpty)
                        )

                } else {
                    val message = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorBody::class.java
                    ).message
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(Resource.Error(message))
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val res = when (t) {
                    is IOException -> Resource.ConnectionError
                    else -> Resource.Error(UNKNOWN_ERROR)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(res))
            }

        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}