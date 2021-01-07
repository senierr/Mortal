package com.senierr.mortal.repository.entity.gank

import com.google.gson.annotations.SerializedName

/**
 * 干货实体
 *
 * @author zhouchunjie
 * @date 2020/6/18
 */
data class GanHuo(
    @SerializedName("_id")
    val id: String = "",
    val author: String = "",
    val category: String = "",
    val createdAt: String = "",
    val desc: String = "",
    val images: MutableList<String?> = mutableListOf(),
    val likeCounts: Int = 0,
    val publishedAt: String = "",
    val stars: Int = 0,
    val title: String = "",
    val type: String = "",
    val url: String = "",
    val views: Int = 0
)