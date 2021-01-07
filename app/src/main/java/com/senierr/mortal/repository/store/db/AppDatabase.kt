package com.senierr.mortal.repository.store.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.senierr.mortal.repository.store.db.dao.DBCacheDao
import com.senierr.mortal.repository.store.db.dao.UserInfoDao
import com.senierr.mortal.repository.entity.DBCache
import com.senierr.mortal.repository.entity.bmob.UserInfo

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