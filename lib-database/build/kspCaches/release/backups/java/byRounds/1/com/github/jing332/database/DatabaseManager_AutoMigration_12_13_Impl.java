package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_12_13_Impl extends Migration {
  private final AutoMigrationSpec callback = new DatabaseManager.DeleteSystemTtsColumn();

  public DatabaseManager_AutoMigration_12_13_Impl() {
    super(12, 13);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS `_new_sysTts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` INTEGER NOT NULL DEFAULT 1, `displayName` TEXT, `isEnabled` INTEGER NOT NULL, `isStandby` INTEGER NOT NULL DEFAULT 0, `readAloudTarget` INTEGER NOT NULL, `tts` TEXT NOT NULL)");
    db.execSQL("INSERT INTO `_new_sysTts` (`id`,`groupId`,`displayName`,`isEnabled`,`isStandby`,`readAloudTarget`,`tts`) SELECT `id`,`groupId`,`displayName`,`isEnabled`,`isStandby`,`readAloudTarget`,`tts` FROM `sysTts`");
    db.execSQL("DROP TABLE `sysTts`");
    db.execSQL("ALTER TABLE `_new_sysTts` RENAME TO `sysTts`");
    callback.onPostMigrate(db);
  }
}
