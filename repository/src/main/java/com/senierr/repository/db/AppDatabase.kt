package com.senierr.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.senierr.repository.db.dao.DBCacheDao
import com.senierr.repository.db.dao.UserInfoDao
import com.senierr.repository.entity.DBCache
import com.senierr.repository.entity.bmob.UserInfo

/**
 * 数据库入口
 *
 * @author zhouchunjie
 * @date 2018/3/13
 */
@Database(entities = [DBCache::class, UserInfo::class],
    version = DatabaseManager.DB_VERSION,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDBCacheDao(): DBCacheDao

    abstract fun getUserInfoDao(): UserInfoDao
}