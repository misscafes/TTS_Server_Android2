package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_10_11_Impl extends Migration {
  public DatabaseManager_AutoMigration_10_11_Impl() {
    super(10, 11);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("ALTER TABLE `Plugin` ADD COLUMN `version` INTEGER NOT NULL DEFAULT 0");
  }
}
