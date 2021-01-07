package com.senierr.mortal.repository.store.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.senierr.mortal.repository.entity.DBCache

/**
 * 数据库缓存接口
 *
 * @author zhouchunjie
 * @date 2018/3/29
 */
@Dao
interface DBCacheDao {

    @Query("SELECT * FROM DBCache WHERE `key` = :key")
    fun get(key: String): DBCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(dbCache: DBCache)

    @Query("DELETE FROM DBCache WHERE `key` = :key")
    fun deleteByKey(key: String)

    @Query("DELETE FROM DBCache")
    fun deleteAll()
}