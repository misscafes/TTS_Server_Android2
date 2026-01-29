package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_16_17_Impl extends Migration {
  public DatabaseManager_AutoMigration_16_17_Impl() {
    super(16, 17);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS `speech_rules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `isEnabled` INTEGER NOT NULL, `name` TEXT NOT NULL, `version` INTEGER NOT NULL, `ruleId` TEXT NOT NULL, `author` TEXT NOT NULL, `code` TEXT NOT NULL, `tags` TEXT NOT NULL DEFAULT '', `order` INTEGER NOT NULL DEFAULT 0)");
  }
}
