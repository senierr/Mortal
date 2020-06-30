package com.senierr.repository.entity.bmob

/**
 * 用户
 */
data class Advert(
    var objectId: String = "",
    var title: String = "",
    var url: String = "",
    var desc: String = "",
    var image: String = "",
    var position: Int = -1, // 0:引导页，1:首页
    var createdAt: String = "",
    var updatedAt: String = ""
)