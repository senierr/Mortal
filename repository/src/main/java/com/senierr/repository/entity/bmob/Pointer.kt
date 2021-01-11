package com.senierr.repository.entity.bmob

/**
 * 用于表示一对一及一对多的关系
 *
 * @author chunjiezhou
 * @date 2021/01/11
 */
data class Pointer(
    val __type: String = "Pointer",
    val className: String = "",
    val objectId: String = ""
)