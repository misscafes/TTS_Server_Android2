package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.lang.Override;
import java.lang.SuppressWarnings;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
final class DatabaseManager_AutoMigration_24_25_Impl extends Migration {
  public DatabaseManager_AutoMigration_24_25_Impl() {
    super(24, 25);
  }

  @Override
  public void migrate(@NonNull final SupportSQLiteDatabase db) {
    db.execSQL("CREATE TABLE IF NOT EXISTS `system_tts_v2` (`id` INTEGER NOT NULL, `displayName` TEXT NOT NULL, `groupId` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, `order` INTEGER NOT NULL, `config` TEXT NOT NULL, PRIMARY KEY(`id`))");
    db.execSQL("CREATE TABLE IF NOT EXISTS `_new_sysTts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` INTEGER NOT NULL DEFAULT 1, `displayName` TEXT, `isEnabled` INTEGER NOT NULL, `tts` TEXT NOT NULL, `order` INTEGER NOT NULL DEFAULT 0, `speechRule_target` INTEGER NOT NULL, `speechRule_isStandby` INTEGER NOT NULL, `speechRule_specifiedStandbyId` INTEGER, `speechRule_tag` TEXT NOT NULL, `speechRule_tagRuleId` TEXT NOT NULL, `speechRule_tagName` TEXT NOT NULL, `speechRule_tagData` TEXT NOT NULL, `speechRule_configId` INTEGER NOT NULL)");
    db.execSQL("INSERT INTO `_new_sysTts` (`id`,`groupId`,`displayName`,`isEnabled`,`tts`,`order`,`speechRule_target`,`speechRule_isStandby`,`speechRule_tag`,`speechRule_tagRuleId`,`speechRule_tagName`,`speechRule_tagData`,`speechRule_configId`) SELECT `id`,`groupId`,`displayName`,`isEnabled`,`tts`,`order`,`speechRule_target`,`speechRule_isStandby`,`speechRule_tag`,`speechRule_tagRuleId`,`speechRule_tagName`,`speechRule_tagData`,`speechRule_configId` FROM `sysTts`");
    db.execSQL("DROP TABLE `sysTts`");
    db.execSQL("ALTER TABLE `_new_sysTts` RENAME TO `sysTts`");
    db.execSQL("CREATE TABLE IF NOT EXISTS `_new_SystemTtsGroup` (`groupId` INTEGER NOT NULL, `name` TEXT NOT NULL, `order` INTEGER NOT NULL DEFAULT 0, `isExpanded` INTEGER NOT NULL, `audioParams_speed` REAL NOT NULL, `audioParams_volume` REAL NOT NULL, `audioParams_pitch` REAL NOT NULL, PRIMARY KEY(`groupId`))");
    db.execSQL("INSERT INTO `_new_SystemTtsGroup` (`groupId`,`name`,`order`,`isExpanded`,`audioParams_speed`,`audioParams_volume`,`audioParams_pitch`) SELECT `groupId`,`name`,`order`,`isExpanded`,`audioParams_speed`,`audioParams_volume`,`audioParams_pitch` FROM `SystemTtsGroup`");
    db.execSQL("DROP TABLE `SystemTtsGroup`");
    db.execSQL("ALTER TABLE `_new_SystemTtsGroup` RENAME TO `SystemTtsGroup`");
  }
}
