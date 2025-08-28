package com.mh.http.moshi

import com.mh.http.config.NetworkConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

class ResponseDataAdapter<T>(private val config: NetworkConfig,
                             private val delegate: JsonAdapter<T>,
                             private val moshi: Moshi
) : JsonAdapter<T>() {
    override fun fromJson(reader: JsonReader): T? {
        return reader.run {
            var code = "-1"
            var message: String? = null
            var data: T? = null

            beginObject()
            while (hasNext()) {
                when (nextName()) {
                    config.codeKey -> code = nextString()
                    config.messageKey -> message = nextString()
                    co
                    else -> skipValue()
                }
            }



            if ()

            endObject()




            data
        }




    }

    override fun toJson(p0: JsonWriter, p1: T?) {
        TODO("Not yet implemented")
    }
}