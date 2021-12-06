package com.endava.budgetplanner.common.network.deserializer

import com.endava.budgetplanner.common.utils.TransactionType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object TransactionTypeDeserializer : JsonDeserializer<TransactionType> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TransactionType {
        return TransactionType.fromType(json?.asString)
    }
}