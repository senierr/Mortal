package com.senierr.base.support.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 获取Type
 *
 * @author zhouchunjie
 * @date 2019/7/10
 */
object TypeUtil {

    /**
     * 解析单个泛型参数的类
     *
     * 例如：解析 MutableList<UserInfo>
     *      TypeUtil.parseType(MutableList::class.java, arrayOf(UserInfo::class.java))
     */
    fun parseType(rawType: Type, types: Array<Type>): Type {
        val length = types.size
        if (length > 1) {
            val parameterizedType = ParameterizedTypeImpl(types[length - 2], arrayOf(types[length - 1]))
            val newTypes = types.copyOfRange(0, length - 1)
            newTypes[newTypes.size - 1] = parameterizedType
            return parseType(rawType, newTypes)
        }
        return ParameterizedTypeImpl(rawType, types)
    }

    class ParameterizedTypeImpl(
        private val raw: Type,
        private val actualTypes: Array<Type>
    ) : ParameterizedType {

        override fun getRawType(): Type? {
            return raw
        }

        override fun getOwnerType(): Type? {
            return null
        }

        override fun getActualTypeArguments(): Array<Type> {
            return actualTypes
        }
    }
}