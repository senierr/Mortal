package com.senierr.mortal.repository.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * 数据库缓存实体
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
@Entity(tableName = "DBCache")
data class DBCache(
    @PrimaryKey
    var key: String,
    var value: String? = null
) {
    @Ignore
    constructor() : this("")
}