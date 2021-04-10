package com.sample.commonlibrary.repository.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.sample.commonlibrary.logger.LogUtils
import com.sample.commonlibrary.repository.storage.Character
import com.sample.commonlibrary.utils.Constants
import java.lang.reflect.Type

internal class MeaningsJsonDeserializer : JsonDeserializer<Any> {

    private val TAG = MeaningsJsonDeserializer::class.java.simpleName

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any? {
        // Jim Morris, 12/9/2019
        // This code does not execute, although it did while I was using Retrofit and OkHttp callbacks
        // This code stopped executing when I added the line
        //     .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        // to APIClient, which was added for the purpose of using RxJava calls for Retrofit and OkHttp, instead of callbacks
        var characters: ArrayList<Character>? = null
        try {
            val jsonObject = json.asJsonObject
            val charactersJsonArray = jsonObject.getAsJsonArray(Constants.CHARACTERS_DATA_TAG)
            characters = ArrayList(charactersJsonArray.size())
            for (i in 0 until charactersJsonArray.size()) {
                val dematerialized = context.deserialize<Any>(charactersJsonArray.get(i), Character::class.java)
                characters.add(dematerialized as Character)
            }
        } catch (e: JsonParseException) {
            LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.EXC), e)
        }
        return characters
    }

}