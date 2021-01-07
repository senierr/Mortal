package com.senierr.mortal.repository.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * 版本 2 -> 3
 *
 * @author zhouchunjie
 * @date 2020/6/22
 */
class Migration2To3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE UserInfo ADD COLUMN nickname TEXT NOT NULL DEFAULT '' ")
        database.execSQL("ALTER TABLE UserInfo ADD COLUMN avatar TEXT NOT NULL DEFAULT '' ")
        database.execSQL("ALTER TABLE UserInfo ADD COLUMN email TEXT NOT NULL DEFAULT '' ")
    }
}