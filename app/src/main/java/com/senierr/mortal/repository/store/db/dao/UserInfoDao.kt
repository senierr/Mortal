package com.senierr.mortal.repository.store.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.senierr.mortal.repository.entity.bmob.UserInfo

/**
 * 用户详情
 *
 * @author zhouchunjie
 * @date 2018/3/29
 */
@Dao
interface UserInfoDao {

    @Query("SELECT * FROM UserInfo WHERE `objectId` = :objectId")
    fun get(objectId: String): UserInfo?

    @Query("SELECT * FROM UserInfo")
    fun getAll(): MutableList<UserInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(userInfo: UserInfo)

    @Query("DELETE FROM UserInfo WHERE `objectId` = :objectId")
    fun deleteById(objectId: String)

    @Query("DELETE FROM UserInfo")
    fun deleteAll()
}