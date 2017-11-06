package com.dallasgutauckis.configurator.shared.gson


import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import java.io.IOException
import java.util.LinkedHashMap

/**
 * Adapted from [RuntimeTypeAdapterFactory] but with support for enum fields that determine the type
 */
class PolymorphicTypeAdapterFactory<T, E : Enum<*>> private constructor(private val baseType: Class<T>?, private val enumeration: Class<E>?, private val typeFieldName: String?) : TypeAdapterFactory {

    private var defaultType: Class<out T>? = null

    private val enumToSubtype = LinkedHashMap<E, Class<out T>>()

    private val subtypeToEnum = LinkedHashMap<Class<out T>, E>()


    init {
        if (typeFieldName == null || baseType == null || enumeration == null) {
            throw NullPointerException()
        }
    }

    /**
     * Registers `type` identified by `typeEnum`. Type enum is the is the instance of
     * the identifying enum
     *
     * @throws IllegalArgumentException if either `type` or `typeEnum`
     * have already been registered on this type adapter.
     */
    fun registerSubtype(type: Class<out T>?, typeEnum: E?): PolymorphicTypeAdapterFactory<T, E> {
        if (type == null || typeEnum == null) {
            throw NullPointerException()
        }

        if (subtypeToEnum.containsKey(type) || enumToSubtype.containsKey(typeEnum)) {
            throw IllegalArgumentException("types and typeEnums must be unique")
        }

        enumToSubtype.put(typeEnum, type)
        subtypeToEnum.put(type, typeEnum)

        return this
    }

    /**
     * Registers a default type to be parse the stream into if one of the enumerated types is
     * not found.
     * @param defaultType
     */
    fun registerDefaultSubtype(defaultType: Class<out T>?): TypeAdapterFactory {
        if (defaultType == null) {
            throw NullPointerException()
        }

        this.defaultType = defaultType

        return this
    }

    override fun <R> create(gson: Gson, type: TypeToken<R>): TypeAdapter<R>? {
        if (type.rawType != baseType) {
            return null
        }

        val enumTypeToDelegate = LinkedHashMap<E?, TypeAdapter<*>>()
        val subtypeToDelegate = LinkedHashMap<Class<out T>, TypeAdapter<*>>()

        for ((key, value) in enumToSubtype) {
            val delegate = gson.getDelegateAdapter(this, TypeToken.get(value))

            enumTypeToDelegate.put(key, delegate)
            subtypeToDelegate.put(value, delegate)
        }

        val tmpDefaultType = defaultType

        if (tmpDefaultType != null) {
            val defaultDelegate = gson.getDelegateAdapter(this, TypeToken.get(tmpDefaultType))

            enumTypeToDelegate.put(null, defaultDelegate)
            subtypeToDelegate.put(tmpDefaultType, defaultDelegate)
        }

        return object : TypeAdapter<R>() {
            @Throws(IOException::class)
            override fun read(`in`: JsonReader): R? {
                val jsonElement = Streams.parse(`in`)

                val typeJsonElement = jsonElement.asJsonObject.get(typeFieldName)

                var typeEnum: E? = null

                if (typeJsonElement != null) {
                    typeEnum = gson.fromJson(typeJsonElement, enumeration)
                }

                val delegate = enumTypeToDelegate[typeEnum] ?: throw JsonParseException("cannot deserialize " + baseType + " subtype named "
                        + (typeJsonElement ?: "null") + "; did you forget to register a subtype or a default?")// registration requires that subtype extends T

                return delegate.fromJsonTree(jsonElement) as R
            }

            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: R) {
                val srcType = value.javaClass

                val delegate = subtypeToDelegate[srcType] as TypeAdapter<R> ?: throw JsonParseException("cannot serialize " + srcType.getName()
                        + "; did you forget to register a subtype?")// registration requires that subtype extends T

                Streams.write(delegate.toJsonTree(value), out)
            }
        }.nullSafe()
    }

    companion object {

        /**
         * Creates a new runtime type adapter using for `baseType` using `typeFieldName` as the type field name. Type field names are case sensitive.
         */
        fun <T, E : Enum<*>> of(baseType: Class<T>,
                                enumeration: Class<E>,
                                typeFieldName: String): PolymorphicTypeAdapterFactory<T, E> {
            return PolymorphicTypeAdapterFactory(baseType, enumeration, typeFieldName)
        }

        /**
         * Creates a new runtime type adapter for `baseType` using `"type"` as
         * the type field name.
         * @param baseType
         * @param enumeration
         */
        fun <T, E : Enum<*>> of(baseType: Class<T>,
                                enumeration: Class<E>): PolymorphicTypeAdapterFactory<T, E> {
            return PolymorphicTypeAdapterFactory(baseType, enumeration, "type")
        }
    }
}