package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_19_20_Impl extends Migration {
  public DatabaseManager_AutoMigration_19_20_Impl() {
    super(19, 20);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("ALTER TABLE `Plugin` ADD COLUMN `defVars` TEXT NOT NULL DEFAULT '{}'");
    db.execSQL("ALTER TABLE `Plugin` ADD COLUMN `userVars` TEXT NOT NULL DEFAULT '{}'");
  }
}
