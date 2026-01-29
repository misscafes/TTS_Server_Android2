package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_21_22_Impl extends Migration {
  public DatabaseManager_AutoMigration_21_22_Impl() {
    super(21, 22);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("ALTER TABLE `SystemTtsGroup` ADD COLUMN `audioParams_speed` REAL NOT NULL DEFAULT 0.0");
    db.execSQL("ALTER TABLE `SystemTtsGroup` ADD COLUMN `audioParams_volume` REAL NOT NULL DEFAULT 0.0");
    db.execSQL("ALTER TABLE `SystemTtsGroup` ADD COLUMN `audioParams_pitch` REAL NOT NULL DEFAULT 0.0");
  }
}
