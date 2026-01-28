package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_9_10_Impl extends Migration {
  public DatabaseManager_AutoMigration_9_10_Impl() {
    super(9, 10);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS `Plugin` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `isEnabled` INTEGER NOT NULL, `name` TEXT NOT NULL, `pluginId` TEXT NOT NULL, `author` TEXT NOT NULL, `code` TEXT NOT NULL)");
  }
}
