package com.senierr.repository.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * 版本 1 -> 2
 *
 * @author zhouchunjie
 * @date 2020/6/22
 */
class Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE UserInfo (" +
                    "objectId TEXT NOT NULL DEFAULT ''," +
                    "username TEXT NOT NULL DEFAULT '', " +
                    "password TEXT NOT NULL DEFAULT '', " +
                    "sessionToken TEXT NOT NULL DEFAULT '', " +
                    "createdAt TEXT NOT NULL DEFAULT '', " +
                    "updatedAt TEXT NOT NULL DEFAULT '', " +
                    "PRIMARY KEY(objectId)" +
                    ")"
        )
    }
}