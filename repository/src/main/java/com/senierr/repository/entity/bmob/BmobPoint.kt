package com.senierr.repository.entity.bmob

/**
 * 一对一/多关联
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
data class BmobPoint(
    val __type: String = "Pointer",
    val className: String,
    val objectId: String
)