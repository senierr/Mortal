package com.senierr.repository.entity.bmob

/**
 * 返回数组结构
 */
data class BmobArray<T>(
    val results: MutableList<T> = mutableListOf(),
    val count: Int = 0
)