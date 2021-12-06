package com.endava.budgetplanner.common.network.calladapter

import com.endava.budgetplanner.common.network.Resource
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

private const val PARAMETRIZED_TYPE_ERROR = "Response must be parameterized "
private const val UPPER_BOUND_INDEX = 0

class NetworkResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (getRawType(returnType) != Call::class.java)
            return null

        check(returnType is ParameterizedType) {
            PARAMETRIZED_TYPE_ERROR
        }

        val responseType = getParameterUpperBound(UPPER_BOUND_INDEX, returnType)

        if (getRawType(responseType) != Resource::class.java) {
            return null
        }

        check(responseType is ParameterizedType) {
            PARAMETRIZED_TYPE_ERROR
        }

        val successBodyType = getParameterUpperBound(UPPER_BOUND_INDEX, responseType)

        return NetworkResponseAdapter<Any>(successBodyType)
    }
}