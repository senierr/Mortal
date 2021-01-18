package com.senierr.repository.entity.bmob

/**
 * 默认返回结构
 */
data class BmobDefaultResponse(
    val msg: String = "ok"
) {
    fun isSuccessful(): Boolean = msg == "ok"
}

/**
 * 添加数据返回实体
 */
data class BmobInsertResponse(
        var objectId: String = "",
        var createdAt: String = ""
)

/**
 * 更新数据返回实体
 */
data class BmobUpdateResponse(
        var updatedAt: String = ""
)

/**
 * 返回数组结构
 */
data class BmobArrayResponse<T>(
        val results: MutableList<T> = mutableListOf(),
        val count: Int = 0
)