package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_8_9_Impl extends Migration {
  public DatabaseManager_AutoMigration_8_9_Impl() {
    super(8, 9);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("ALTER TABLE `replaceRule` ADD COLUMN `groupId` INTEGER NOT NULL DEFAULT 1");
    db.execSQL("CREATE TABLE IF NOT EXISTS `replaceRuleGroup` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `order` INTEGER NOT NULL, `isExpanded` INTEGER NOT NULL, PRIMARY KEY(`id`))");
  }
}
