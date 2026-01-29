package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_17_18_Impl extends Migration {
  public DatabaseManager_AutoMigration_17_18_Impl() {
    super(17, 18);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("ALTER TABLE `sysTts` ADD COLUMN `speechRule_tagData` TEXT NOT NULL DEFAULT ''");
    db.execSQL("ALTER TABLE `sysTts` ADD COLUMN `speechRule_configId` INTEGER NOT NULL DEFAULT 0");
    db.execSQL("ALTER TABLE `speech_rules` ADD COLUMN `tagsData` TEXT NOT NULL DEFAULT ''");
  }
}
