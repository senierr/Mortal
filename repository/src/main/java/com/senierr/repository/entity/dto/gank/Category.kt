package com.senierr.repository.entity.dto.gank

import com.google.gson.annotations.SerializedName

/**
 * 分类实体
 *
 * @author zhouchunjie
 * @date 2020/6/18
 */
data class Category(
    @SerializedName("_id")
    val id: String = "",
    val coverImageUrl: String = "",
    val desc: String = "",
    val title: String = "",
    val type: String = ""
)