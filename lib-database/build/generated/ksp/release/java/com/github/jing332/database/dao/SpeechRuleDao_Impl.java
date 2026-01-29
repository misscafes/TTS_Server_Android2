package com.github.jing332.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.github.jing332.database.entities.MapConverters;
import com.github.jing332.database.entities.SpeechRule;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SpeechRuleDao_Impl implements SpeechRuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SpeechRule> __insertionAdapterOfSpeechRule;

  private final SpeechRule.Converters __converters = new SpeechRule.Converters();

  private final EntityDeletionOrUpdateAdapter<SpeechRule> __deletionAdapterOfSpeechRule;

  private final EntityDeletionOrUpdateAdapter<SpeechRule> __updateAdapterOfSpeechRule;

  public SpeechRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSpeechRule = new EntityInsertionAdapter<SpeechRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `speech_rules` (`id`,`isEnabled`,`name`,`version`,`ruleId`,`author`,`code`,`tags`,`tagsData`,`order`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SpeechRule entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getVersion());
        statement.bindString(5, entity.getRuleId());
        statement.bindString(6, entity.getAuthor());
        statement.bindString(7, entity.getCode());
        final String _tmp_1 = MapConverters.INSTANCE.fromMap(entity.getTags());
        statement.bindString(8, _tmp_1);
        final String _tmp_2 = __converters.fromListMap(entity.getTagsData());
        statement.bindString(9, _tmp_2);
        statement.bindLong(10, entity.getOrder());
      }
    };
    this.__deletionAdapterOfSpeechRule = new EntityDeletionOrUpdateAdapter<SpeechRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `speech_rules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SpeechRule entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSpeechRule = new EntityDeletionOrUpdateAdapter<SpeechRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `speech_rules` SET `id` = ?,`isEnabled` = ?,`name` = ?,`version` = ?,`ruleId` = ?,`author` = ?,`code` = ?,`tags` = ?,`tagsData` = ?,`order` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SpeechRule entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindString(3, entity.getName());
        statement.bindLong(4, entity.getVersion());
        statement.bindString(5, entity.getRuleId());
        statement.bindString(6, entity.getAuthor());
        statement.bindString(7, entity.getCode());
        final String _tmp_1 = MapConverters.INSTANCE.fromMap(entity.getTags());
        statement.bindString(8, _tmp_1);
        final String _tmp_2 = __converters.fromListMap(entity.getTagsData());
        statement.bindString(9, _tmp_2);
        statement.bindLong(10, entity.getOrder());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public void insert(final SpeechRule... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSpeechRule.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final SpeechRule... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSpeechRule.handleMultiple(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final SpeechRule... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSpeechRule.handleMultiple(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<SpeechRule> getAll() {
    final String _sql = "SELECT `speech_rules`.`id` AS `id`, `speech_rules`.`isEnabled` AS `isEnabled`, `speech_rules`.`name` AS `name`, `speech_rules`.`version` AS `version`, `speech_rules`.`ruleId` AS `ruleId`, `speech_rules`.`author` AS `author`, `speech_rules`.`code` AS `code`, `speech_rules`.`tags` AS `tags`, `speech_rules`.`tagsData` AS `tagsData`, `speech_rules`.`order` AS `order` FROM speech_rules ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfIsEnabled = 1;
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfVersion = 3;
      final int _cursorIndexOfRuleId = 4;
      final int _cursorIndexOfAuthor = 5;
      final int _cursorIndexOfCode = 6;
      final int _cursorIndexOfTags = 7;
      final int _cursorIndexOfTagsData = 8;
      final int _cursorIndexOfOrder = 9;
      final List<SpeechRule> _result = new ArrayList<SpeechRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SpeechRule _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpVersion;
        _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
        final String _tmpRuleId;
        _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        final String _tmpCode;
        _tmpCode = _cursor.getString(_cursorIndexOfCode);
        final Map<String, String> _tmpTags;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfTags);
        _tmpTags = MapConverters.INSTANCE.toMap(_tmp_1);
        final Map<? extends String, ? extends Map<? extends String, ? extends Map<? extends String, String>>> _tmpTagsData;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfTagsData);
        _tmpTagsData = __converters.toListMap(_tmp_2);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        _item = new SpeechRule(_tmpId,_tmpIsEnabled,_tmpName,_tmpVersion,_tmpRuleId,_tmpAuthor,_tmpCode,_tmpTags,_tmpTagsData,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SpeechRule> getAllEnabled() {
    final String _sql = "SELECT `speech_rules`.`id` AS `id`, `speech_rules`.`isEnabled` AS `isEnabled`, `speech_rules`.`name` AS `name`, `speech_rules`.`version` AS `version`, `speech_rules`.`ruleId` AS `ruleId`, `speech_rules`.`author` AS `author`, `speech_rules`.`code` AS `code`, `speech_rules`.`tags` AS `tags`, `speech_rules`.`tagsData` AS `tagsData`, `speech_rules`.`order` AS `order` FROM speech_rules WHERE isEnabled = '1'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfIsEnabled = 1;
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfVersion = 3;
      final int _cursorIndexOfRuleId = 4;
      final int _cursorIndexOfAuthor = 5;
      final int _cursorIndexOfCode = 6;
      final int _cursorIndexOfTags = 7;
      final int _cursorIndexOfTagsData = 8;
      final int _cursorIndexOfOrder = 9;
      final List<SpeechRule> _result = new ArrayList<SpeechRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SpeechRule _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpVersion;
        _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
        final String _tmpRuleId;
        _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        final String _tmpCode;
        _tmpCode = _cursor.getString(_cursorIndexOfCode);
        final Map<String, String> _tmpTags;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfTags);
        _tmpTags = MapConverters.INSTANCE.toMap(_tmp_1);
        final Map<? extends String, ? extends Map<? extends String, ? extends Map<? extends String, String>>> _tmpTagsData;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfTagsData);
        _tmpTagsData = __converters.toListMap(_tmp_2);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        _item = new SpeechRule(_tmpId,_tmpIsEnabled,_tmpName,_tmpVersion,_tmpRuleId,_tmpAuthor,_tmpCode,_tmpTags,_tmpTagsData,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Flow<List<SpeechRule>> flowAll() {
    final String _sql = "SELECT `speech_rules`.`id` AS `id`, `speech_rules`.`isEnabled` AS `isEnabled`, `speech_rules`.`name` AS `name`, `speech_rules`.`version` AS `version`, `speech_rules`.`ruleId` AS `ruleId`, `speech_rules`.`author` AS `author`, `speech_rules`.`code` AS `code`, `speech_rules`.`tags` AS `tags`, `speech_rules`.`tagsData` AS `tagsData`, `speech_rules`.`order` AS `order` FROM speech_rules ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"speech_rules"}, new Callable<List<SpeechRule>>() {
      @Override
      @NonNull
      public List<SpeechRule> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfIsEnabled = 1;
          final int _cursorIndexOfName = 2;
          final int _cursorIndexOfVersion = 3;
          final int _cursorIndexOfRuleId = 4;
          final int _cursorIndexOfAuthor = 5;
          final int _cursorIndexOfCode = 6;
          final int _cursorIndexOfTags = 7;
          final int _cursorIndexOfTagsData = 8;
          final int _cursorIndexOfOrder = 9;
          final List<SpeechRule> _result = new ArrayList<SpeechRule>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SpeechRule _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            final String _tmpRuleId;
            _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final Map<String, String> _tmpTags;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfTags);
            _tmpTags = MapConverters.INSTANCE.toMap(_tmp_1);
            final Map<? extends String, ? extends Map<? extends String, ? extends Map<? extends String, String>>> _tmpTagsData;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfTagsData);
            _tmpTagsData = __converters.toListMap(_tmp_2);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            _item = new SpeechRule(_tmpId,_tmpIsEnabled,_tmpName,_tmpVersion,_tmpRuleId,_tmpAuthor,_tmpCode,_tmpTags,_tmpTagsData,_tmpOrder);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int getCount() {
    final String _sql = "SELECT count(*) FROM speech_rules";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public SpeechRule getByRuleId(final String ruleId, final boolean isEnabled) {
    final String _sql = "SELECT * FROM speech_rules WHERE ruleId = ? AND isEnabled = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, ruleId);
    _argIndex = 2;
    final int _tmp = isEnabled ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
      final int _cursorIndexOfRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleId");
      final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
      final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
      final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
      final int _cursorIndexOfTagsData = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsData");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final SpeechRule _result;
      if (_cursor.moveToFirst()) {
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final boolean _tmpIsEnabled;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp_1 != 0;
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpVersion;
        _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
        final String _tmpRuleId;
        _tmpRuleId = _cursor.getString(_cursorIndexOfRuleId);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        final String _tmpCode;
        _tmpCode = _cursor.getString(_cursorIndexOfCode);
        final Map<String, String> _tmpTags;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfTags);
        _tmpTags = MapConverters.INSTANCE.toMap(_tmp_2);
        final Map<? extends String, ? extends Map<? extends String, ? extends Map<? extends String, String>>> _tmpTagsData;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfTagsData);
        _tmpTagsData = __converters.toListMap(_tmp_3);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        _result = new SpeechRule(_tmpId,_tmpIsEnabled,_tmpName,_tmpVersion,_tmpRuleId,_tmpAuthor,_tmpCode,_tmpTags,_tmpTagsData,_tmpOrder);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public void insertOrUpdate(final SpeechRule... args) {
    SpeechRuleDao.DefaultImpls.insertOrUpdate(SpeechRuleDao_Impl.this, args);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
