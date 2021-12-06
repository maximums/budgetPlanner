package com.endava.budgetplanner.common.network.deserializer

import com.endava.budgetplanner.common.utils.CategoryProperties
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object CategoryPropertiesDeserializer : JsonDeserializer<CategoryProperties> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CategoryProperties {
        return CategoryProperties.fromCode(json?.asString)
    }
}