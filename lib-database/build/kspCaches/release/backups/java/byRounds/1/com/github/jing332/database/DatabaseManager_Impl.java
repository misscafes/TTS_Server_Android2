package com.github.jing332.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.github.jing332.database.dao.PluginDao;
import com.github.jing332.database.dao.PluginDao_Impl;
import com.github.jing332.database.dao.ReplaceRuleDao;
import com.github.jing332.database.dao.ReplaceRuleDao_Impl;
import com.github.jing332.database.dao.SpeechRuleDao;
import com.github.jing332.database.dao.SpeechRuleDao_Impl;
import com.github.jing332.database.dao.SystemTtsDao;
import com.github.jing332.database.dao.SystemTtsDao_Impl;
import com.github.jing332.database.dao.SystemTtsV2Dao;
import com.github.jing332.database.dao.SystemTtsV2Dao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DatabaseManager_Impl extends DatabaseManager {
  private volatile SystemTtsDao _systemTtsDao;

  private volatile SystemTtsV2Dao _systemTtsV2Dao;

  private volatile ReplaceRuleDao _replaceRuleDao;

  private volatile PluginDao _pluginDao;

  private volatile SpeechRuleDao _speechRuleDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(26) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `sysTts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` INTEGER NOT NULL DEFAULT 1, `displayName` TEXT, `isEnabled` INTEGER NOT NULL, `tts` TEXT NOT NULL, `order` INTEGER NOT NULL DEFAULT 0, `speechRule_target` INTEGER NOT NULL, `speechRule_isStandby` INTEGER NOT NULL, `speechRule_specifiedStandbyId` INTEGER, `speechRule_tag` TEXT NOT NULL, `speechRule_tagRuleId` TEXT NOT NULL, `speechRule_tagName` TEXT NOT NULL, `speechRule_tagData` TEXT NOT NULL, `speechRule_configId` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `system_tts_v2` (`id` INTEGER NOT NULL, `displayName` TEXT NOT NULL, `groupId` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, `order` INTEGER NOT NULL, `config` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `SystemTtsGroup` (`groupId` INTEGER NOT NULL, `name` TEXT NOT NULL, `order` INTEGER NOT NULL DEFAULT 0, `isExpanded` INTEGER NOT NULL, `audioParams_speed` REAL NOT NULL, `audioParams_volume` REAL NOT NULL, `audioParams_pitch` REAL NOT NULL, PRIMARY KEY(`groupId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `replaceRule` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` INTEGER NOT NULL DEFAULT 1, `name` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, `isRegex` INTEGER NOT NULL, `pattern` TEXT NOT NULL, `replacement` TEXT NOT NULL, `order` INTEGER NOT NULL DEFAULT 0, `sampleText` TEXT NOT NULL DEFAULT '')");
        db.execSQL("CREATE TABLE IF NOT EXISTS `replaceRuleGroup` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `order` INTEGER NOT NULL, `isExpanded` INTEGER NOT NULL, `onExecution` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Plugin` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `isEnabled` INTEGER NOT NULL, `version` INTEGER NOT NULL DEFAULT 0, `name` TEXT NOT NULL, `pluginId` TEXT NOT NULL, `author` TEXT NOT NULL, `iconUrl` TEXT NOT NULL DEFAULT '', `code` TEXT NOT NULL, `defVars` TEXT NOT NULL DEFAULT '{}', `userVars` TEXT NOT NULL DEFAULT '{}', `order` INTEGER NOT NULL DEFAULT 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `speech_rules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `isEnabled` INTEGER NOT NULL, `name` TEXT NOT NULL, `version` INTEGER NOT NULL, `ruleId` TEXT NOT NULL, `author` TEXT NOT NULL, `code` TEXT NOT NULL, `tags` TEXT NOT NULL DEFAULT '', `tagsData` TEXT NOT NULL DEFAULT '', `order` INTEGER NOT NULL DEFAULT 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8f9dad68ec3eb912a2afba6b7913ebb9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `sysTts`");
        db.execSQL("DROP TABLE IF EXISTS `system_tts_v2`");
        db.execSQL("DROP TABLE IF EXISTS `SystemTtsGroup`");
        db.execSQL("DROP TABLE IF EXISTS `replaceRule`");
        db.execSQL("DROP TABLE IF EXISTS `replaceRuleGroup`");
        db.execSQL("DROP TABLE IF EXISTS `Plugin`");
        db.execSQL("DROP TABLE IF EXISTS `speech_rules`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSysTts = new HashMap<String, TableInfo.Column>(14);
        _columnsSysTts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("groupId", new TableInfo.Column("groupId", "INTEGER", true, 0, "1", TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("displayName", new TableInfo.Column("displayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("tts", new TableInfo.Column("tts", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("order", new TableInfo.Column("order", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("speechRule_target", new TableInfo.Column("speechRule_target", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("speechRule_isStandby", new TableInfo.Column("speechRule_isStandby", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("speechRule_specifiedStandbyId", new TableInfo.Column("speechRule_specifiedStandbyId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("speechRule_tag", new TableInfo.Column("speechRule_tag", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("speechRule_tagRuleId", new TableInfo.Column("speechRule_tagRuleId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("speechRule_tagName", new TableInfo.Column("speechRule_tagName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("speechRule_tagData", new TableInfo.Column("speechRule_tagData", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSysTts.put("speechRule_configId", new TableInfo.Column("speechRule_configId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSysTts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSysTts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSysTts = new TableInfo("sysTts", _columnsSysTts, _foreignKeysSysTts, _indicesSysTts);
        final TableInfo _existingSysTts = TableInfo.read(db, "sysTts");
        if (!_infoSysTts.equals(_existingSysTts)) {
          return new RoomOpenHelper.ValidationResult(false, "sysTts(com.github.jing332.database.entities.systts.v1.SystemTts).\n"
                  + " Expected:\n" + _infoSysTts + "\n"
                  + " Found:\n" + _existingSysTts);
        }
        final HashMap<String, TableInfo.Column> _columnsSystemTtsV2 = new HashMap<String, TableInfo.Column>(6);
        _columnsSystemTtsV2.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsV2.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsV2.put("groupId", new TableInfo.Column("groupId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsV2.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsV2.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsV2.put("config", new TableInfo.Column("config", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSystemTtsV2 = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSystemTtsV2 = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSystemTtsV2 = new TableInfo("system_tts_v2", _columnsSystemTtsV2, _foreignKeysSystemTtsV2, _indicesSystemTtsV2);
        final TableInfo _existingSystemTtsV2 = TableInfo.read(db, "system_tts_v2");
        if (!_infoSystemTtsV2.equals(_existingSystemTtsV2)) {
          return new RoomOpenHelper.ValidationResult(false, "system_tts_v2(com.github.jing332.database.entities.systts.SystemTtsV2).\n"
                  + " Expected:\n" + _infoSystemTtsV2 + "\n"
                  + " Found:\n" + _existingSystemTtsV2);
        }
        final HashMap<String, TableInfo.Column> _columnsSystemTtsGroup = new HashMap<String, TableInfo.Column>(7);
        _columnsSystemTtsGroup.put("groupId", new TableInfo.Column("groupId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsGroup.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsGroup.put("order", new TableInfo.Column("order", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsGroup.put("isExpanded", new TableInfo.Column("isExpanded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsGroup.put("audioParams_speed", new TableInfo.Column("audioParams_speed", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsGroup.put("audioParams_volume", new TableInfo.Column("audioParams_volume", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSystemTtsGroup.put("audioParams_pitch", new TableInfo.Column("audioParams_pitch", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSystemTtsGroup = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSystemTtsGroup = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSystemTtsGroup = new TableInfo("SystemTtsGroup", _columnsSystemTtsGroup, _foreignKeysSystemTtsGroup, _indicesSystemTtsGroup);
        final TableInfo _existingSystemTtsGroup = TableInfo.read(db, "SystemTtsGroup");
        if (!_infoSystemTtsGroup.equals(_existingSystemTtsGroup)) {
          return new RoomOpenHelper.ValidationResult(false, "SystemTtsGroup(com.github.jing332.database.entities.systts.SystemTtsGroup).\n"
                  + " Expected:\n" + _infoSystemTtsGroup + "\n"
                  + " Found:\n" + _existingSystemTtsGroup);
        }
        final HashMap<String, TableInfo.Column> _columnsReplaceRule = new HashMap<String, TableInfo.Column>(9);
        _columnsReplaceRule.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRule.put("groupId", new TableInfo.Column("groupId", "INTEGER", true, 0, "1", TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRule.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRule.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRule.put("isRegex", new TableInfo.Column("isRegex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRule.put("pattern", new TableInfo.Column("pattern", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRule.put("replacement", new TableInfo.Column("replacement", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRule.put("order", new TableInfo.Column("order", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRule.put("sampleText", new TableInfo.Column("sampleText", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReplaceRule = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReplaceRule = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReplaceRule = new TableInfo("replaceRule", _columnsReplaceRule, _foreignKeysReplaceRule, _indicesReplaceRule);
        final TableInfo _existingReplaceRule = TableInfo.read(db, "replaceRule");
        if (!_infoReplaceRule.equals(_existingReplaceRule)) {
          return new RoomOpenHelper.ValidationResult(false, "replaceRule(com.github.jing332.database.entities.replace.ReplaceRule).\n"
                  + " Expected:\n" + _infoReplaceRule + "\n"
                  + " Found:\n" + _existingReplaceRule);
        }
        final HashMap<String, TableInfo.Column> _columnsReplaceRuleGroup = new HashMap<String, TableInfo.Column>(5);
        _columnsReplaceRuleGroup.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRuleGroup.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRuleGroup.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRuleGroup.put("isExpanded", new TableInfo.Column("isExpanded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReplaceRuleGroup.put("onExecution", new TableInfo.Column("onExecution", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReplaceRuleGroup = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReplaceRuleGroup = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReplaceRuleGroup = new TableInfo("replaceRuleGroup", _columnsReplaceRuleGroup, _foreignKeysReplaceRuleGroup, _indicesReplaceRuleGroup);
        final TableInfo _existingReplaceRuleGroup = TableInfo.read(db, "replaceRuleGroup");
        if (!_infoReplaceRuleGroup.equals(_existingReplaceRuleGroup)) {
          return new RoomOpenHelper.ValidationResult(false, "replaceRuleGroup(com.github.jing332.database.entities.replace.ReplaceRuleGroup).\n"
                  + " Expected:\n" + _infoReplaceRuleGroup + "\n"
                  + " Found:\n" + _existingReplaceRuleGroup);
        }
        final HashMap<String, TableInfo.Column> _columnsPlugin = new HashMap<String, TableInfo.Column>(11);
        _columnsPlugin.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("version", new TableInfo.Column("version", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("pluginId", new TableInfo.Column("pluginId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("author", new TableInfo.Column("author", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("iconUrl", new TableInfo.Column("iconUrl", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("defVars", new TableInfo.Column("defVars", "TEXT", true, 0, "'{}'", TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("userVars", new TableInfo.Column("userVars", "TEXT", true, 0, "'{}'", TableInfo.CREATED_FROM_ENTITY));
        _columnsPlugin.put("order", new TableInfo.Column("order", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlugin = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlugin = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlugin = new TableInfo("Plugin", _columnsPlugin, _foreignKeysPlugin, _indicesPlugin);
        final TableInfo _existingPlugin = TableInfo.read(db, "Plugin");
        if (!_infoPlugin.equals(_existingPlugin)) {
          return new RoomOpenHelper.ValidationResult(false, "Plugin(com.github.jing332.database.entities.plugin.Plugin).\n"
                  + " Expected:\n" + _infoPlugin + "\n"
                  + " Found:\n" + _existingPlugin);
        }
        final HashMap<String, TableInfo.Column> _columnsSpeechRules = new HashMap<String, TableInfo.Column>(10);
        _columnsSpeechRules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("version", new TableInfo.Column("version", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("ruleId", new TableInfo.Column("ruleId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("author", new TableInfo.Column("author", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("tagsData", new TableInfo.Column("tagsData", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeechRules.put("order", new TableInfo.Column("order", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpeechRules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpeechRules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSpeechRules = new TableInfo("speech_rules", _columnsSpeechRules, _foreignKeysSpeechRules, _indicesSpeechRules);
        final TableInfo _existingSpeechRules = TableInfo.read(db, "speech_rules");
        if (!_infoSpeechRules.equals(_existingSpeechRules)) {
          return new RoomOpenHelper.ValidationResult(false, "speech_rules(com.github.jing332.database.entities.SpeechRule).\n"
                  + " Expected:\n" + _infoSpeechRules + "\n"
                  + " Found:\n" + _existingSpeechRules);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "8f9dad68ec3eb912a2afba6b7913ebb9", "99d3a4a239dbddeb269e4e7248d9ff3b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "sysTts","system_tts_v2","SystemTtsGroup","replaceRule","replaceRuleGroup","Plugin","speech_rules");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `sysTts`");
      _db.execSQL("DELETE FROM `system_tts_v2`");
      _db.execSQL("DELETE FROM `SystemTtsGroup`");
      _db.execSQL("DELETE FROM `replaceRule`");
      _db.execSQL("DELETE FROM `replaceRuleGroup`");
      _db.execSQL("DELETE FROM `Plugin`");
      _db.execSQL("DELETE FROM `speech_rules`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SystemTtsDao.class, SystemTtsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SystemTtsV2Dao.class, SystemTtsV2Dao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReplaceRuleDao.class, ReplaceRuleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PluginDao.class, PluginDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SpeechRuleDao.class, SpeechRuleDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    _autoMigrations.add(new DatabaseManager_AutoMigration_7_8_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_8_9_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_9_10_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_10_11_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_11_12_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_12_13_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_13_14_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_14_15_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_16_17_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_17_18_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_18_19_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_19_20_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_20_21_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_21_22_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_22_23_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_23_24_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_24_25_Impl());
    _autoMigrations.add(new DatabaseManager_AutoMigration_25_26_Impl());
    return _autoMigrations;
  }

  @Override
  public SystemTtsDao getSystemTtsDao() {
    if (_systemTtsDao != null) {
      return _systemTtsDao;
    } else {
      synchronized(this) {
        if(_systemTtsDao == null) {
          _systemTtsDao = new SystemTtsDao_Impl(this);
        }
        return _systemTtsDao;
      }
    }
  }

  @Override
  public SystemTtsV2Dao getSystemTtsV2() {
    if (_systemTtsV2Dao != null) {
      return _systemTtsV2Dao;
    } else {
      synchronized(this) {
        if(_systemTtsV2Dao == null) {
          _systemTtsV2Dao = new SystemTtsV2Dao_Impl(this);
        }
        return _systemTtsV2Dao;
      }
    }
  }

  @Override
  public ReplaceRuleDao getReplaceRuleDao() {
    if (_replaceRuleDao != null) {
      return _replaceRuleDao;
    } else {
      synchronized(this) {
        if(_replaceRuleDao == null) {
          _replaceRuleDao = new ReplaceRuleDao_Impl(this);
        }
        return _replaceRuleDao;
      }
    }
  }

  @Override
  public PluginDao getPluginDao() {
    if (_pluginDao != null) {
      return _pluginDao;
    } else {
      synchronized(this) {
        if(_pluginDao == null) {
          _pluginDao = new PluginDao_Impl(this);
        }
        return _pluginDao;
      }
    }
  }

  @Override
  public SpeechRuleDao getSpeechRuleDao() {
    if (_speechRuleDao != null) {
      return _speechRuleDao;
    } else {
      synchronized(this) {
        if(_speechRuleDao == null) {
          _speechRuleDao = new SpeechRuleDao_Impl(this);
        }
        return _speechRuleDao;
      }
    }
  }
}
