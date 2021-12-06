package com.endava.budgetplanner.common.network.calladapter

import com.endava.budgetplanner.common.network.Resource
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResponseAdapter<T : Any>(
    private val type: Type
) : CallAdapter<T, Call<Resource<T>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<Resource<T>> = NetworkResponseCall(call)
}