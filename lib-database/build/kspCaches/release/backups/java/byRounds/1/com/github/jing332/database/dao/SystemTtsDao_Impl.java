package com.github.jing332.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.github.jing332.database.entities.MapConverters;
import com.github.jing332.database.entities.systts.AudioParams;
import com.github.jing332.database.entities.systts.GroupWithSystemTts;
import com.github.jing332.database.entities.systts.IConfiguration;
import com.github.jing332.database.entities.systts.SpeechRuleInfo;
import com.github.jing332.database.entities.systts.SystemTtsGroup;
import com.github.jing332.database.entities.systts.SystemTtsV2;
import com.github.jing332.database.entities.systts.v1.GroupWithV1TTS;
import com.github.jing332.database.entities.systts.v1.SystemTts;
import com.github.jing332.database.entities.systts.v1.tts.ITextToSpeechEngine;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SystemTtsDao_Impl implements SystemTtsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SystemTts> __insertionAdapterOfSystemTts;

  private final SystemTts.Converters __converters = new SystemTts.Converters();

  private final EntityInsertionAdapter<SystemTtsGroup> __insertionAdapterOfSystemTtsGroup;

  private final EntityDeletionOrUpdateAdapter<SystemTts> __deletionAdapterOfSystemTts;

  private final EntityDeletionOrUpdateAdapter<SystemTtsGroup> __deletionAdapterOfSystemTtsGroup;

  private final EntityDeletionOrUpdateAdapter<SystemTts> __updateAdapterOfSystemTts;

  private final EntityDeletionOrUpdateAdapter<SystemTtsGroup> __updateAdapterOfSystemTtsGroup;

  private final SharedSQLiteStatement __preparedStmtOfSetAllTtsEnabled;

  private final SharedSQLiteStatement __preparedStmtOfSetTtsEnabledInGroup;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTtsByGroup;

  private final SystemTtsV2.Converters __converters_1 = new SystemTtsV2.Converters();

  public SystemTtsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSystemTts = new EntityInsertionAdapter<SystemTts>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sysTts` (`id`,`groupId`,`displayName`,`isEnabled`,`tts`,`order`,`speechRule_target`,`speechRule_isStandby`,`speechRule_specifiedStandbyId`,`speechRule_tag`,`speechRule_tagRuleId`,`speechRule_tagName`,`speechRule_tagData`,`speechRule_configId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTts entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getGroupId());
        if (entity.getDisplayName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDisplayName());
        }
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final String _tmp_1 = __converters.ttsToString(entity.getTts());
        statement.bindString(5, _tmp_1);
        statement.bindLong(6, entity.getOrder());
        final SpeechRuleInfo _tmpSpeechRule = entity.getSpeechRule();
        statement.bindLong(7, _tmpSpeechRule.getTarget());
        final int _tmp_2 = _tmpSpeechRule.isStandby() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        if (_tmpSpeechRule.getSpecifiedStandbyId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmpSpeechRule.getSpecifiedStandbyId());
        }
        statement.bindString(10, _tmpSpeechRule.getTag());
        statement.bindString(11, _tmpSpeechRule.getTagRuleId());
        statement.bindString(12, _tmpSpeechRule.getTagName());
        final String _tmp_3 = MapConverters.INSTANCE.fromMap(_tmpSpeechRule.getTagData());
        statement.bindString(13, _tmp_3);
        statement.bindLong(14, _tmpSpeechRule.getConfigId());
      }
    };
    this.__insertionAdapterOfSystemTtsGroup = new EntityInsertionAdapter<SystemTtsGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `SystemTtsGroup` (`groupId`,`name`,`order`,`isExpanded`,`audioParams_speed`,`audioParams_volume`,`audioParams_pitch`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTtsGroup entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getOrder());
        final int _tmp = entity.isExpanded() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final AudioParams _tmpAudioParams = entity.getAudioParams();
        statement.bindDouble(5, _tmpAudioParams.getSpeed());
        statement.bindDouble(6, _tmpAudioParams.getVolume());
        statement.bindDouble(7, _tmpAudioParams.getPitch());
      }
    };
    this.__deletionAdapterOfSystemTts = new EntityDeletionOrUpdateAdapter<SystemTts>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sysTts` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTts entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfSystemTtsGroup = new EntityDeletionOrUpdateAdapter<SystemTtsGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `SystemTtsGroup` WHERE `groupId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTtsGroup entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSystemTts = new EntityDeletionOrUpdateAdapter<SystemTts>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR REPLACE `sysTts` SET `id` = ?,`groupId` = ?,`displayName` = ?,`isEnabled` = ?,`tts` = ?,`order` = ?,`speechRule_target` = ?,`speechRule_isStandby` = ?,`speechRule_specifiedStandbyId` = ?,`speechRule_tag` = ?,`speechRule_tagRuleId` = ?,`speechRule_tagName` = ?,`speechRule_tagData` = ?,`speechRule_configId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTts entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getGroupId());
        if (entity.getDisplayName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDisplayName());
        }
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final String _tmp_1 = __converters.ttsToString(entity.getTts());
        statement.bindString(5, _tmp_1);
        statement.bindLong(6, entity.getOrder());
        final SpeechRuleInfo _tmpSpeechRule = entity.getSpeechRule();
        statement.bindLong(7, _tmpSpeechRule.getTarget());
        final int _tmp_2 = _tmpSpeechRule.isStandby() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        if (_tmpSpeechRule.getSpecifiedStandbyId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmpSpeechRule.getSpecifiedStandbyId());
        }
        statement.bindString(10, _tmpSpeechRule.getTag());
        statement.bindString(11, _tmpSpeechRule.getTagRuleId());
        statement.bindString(12, _tmpSpeechRule.getTagName());
        final String _tmp_3 = MapConverters.INSTANCE.fromMap(_tmpSpeechRule.getTagData());
        statement.bindString(13, _tmp_3);
        statement.bindLong(14, _tmpSpeechRule.getConfigId());
        statement.bindLong(15, entity.getId());
      }
    };
    this.__updateAdapterOfSystemTtsGroup = new EntityDeletionOrUpdateAdapter<SystemTtsGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR REPLACE `SystemTtsGroup` SET `groupId` = ?,`name` = ?,`order` = ?,`isExpanded` = ?,`audioParams_speed` = ?,`audioParams_volume` = ?,`audioParams_pitch` = ? WHERE `groupId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTtsGroup entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getOrder());
        final int _tmp = entity.isExpanded() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final AudioParams _tmpAudioParams = entity.getAudioParams();
        statement.bindDouble(5, _tmpAudioParams.getSpeed());
        statement.bindDouble(6, _tmpAudioParams.getVolume());
        statement.bindDouble(7, _tmpAudioParams.getPitch());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfSetAllTtsEnabled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sysTts SET isEnabled = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetTtsEnabledInGroup = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sysTts SET isEnabled = ? WHERE groupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTtsByGroup = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from sysTts WHERE groupId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertTts(final SystemTts... items) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSystemTts.insert(items);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertGroup(final SystemTtsGroup group) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSystemTtsGroup.insert(group);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteTts(final SystemTts... items) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSystemTts.handleMultiple(items);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteGroup(final SystemTtsGroup group) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSystemTtsGroup.handle(group);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTts(final SystemTts... items) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSystemTts.handleMultiple(items);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateGroup(final SystemTtsGroup group) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSystemTtsGroup.handle(group);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void setAllTtsEnabled(final boolean isEnabled) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfSetAllTtsEnabled.acquire();
    int _argIndex = 1;
    final int _tmp = isEnabled ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfSetAllTtsEnabled.release(_stmt);
    }
  }

  @Override
  public void setTtsEnabledInGroup(final long groupId, final boolean isEnabled) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfSetTtsEnabledInGroup.acquire();
    int _argIndex = 1;
    final int _tmp = isEnabled ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, groupId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfSetTtsEnabledInGroup.release(_stmt);
    }
  }

  @Override
  public void deleteTtsByGroup(final long groupId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTtsByGroup.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, groupId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteTtsByGroup.release(_stmt);
    }
  }

  @Override
  public List<SystemTts> getAllTts() {
    final String _sql = "SELECT `speechRule_target`, `speechRule_isStandby`, `speechRule_specifiedStandbyId`, `speechRule_tag`, `speechRule_tagRuleId`, `speechRule_tagName`, `speechRule_tagData`, `speechRule_configId`, `sysTts`.`id` AS `id`, `sysTts`.`groupId` AS `groupId`, `sysTts`.`displayName` AS `displayName`, `sysTts`.`isEnabled` AS `isEnabled`, `sysTts`.`tts` AS `tts`, `sysTts`.`order` AS `order` FROM sysTts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTarget = 0;
      final int _cursorIndexOfIsStandby = 1;
      final int _cursorIndexOfSpecifiedStandbyId = 2;
      final int _cursorIndexOfTag = 3;
      final int _cursorIndexOfTagRuleId = 4;
      final int _cursorIndexOfTagName = 5;
      final int _cursorIndexOfTagData = 6;
      final int _cursorIndexOfConfigId = 7;
      final int _cursorIndexOfId = 8;
      final int _cursorIndexOfGroupId = 9;
      final int _cursorIndexOfDisplayName = 10;
      final int _cursorIndexOfIsEnabled = 11;
      final int _cursorIndexOfTts = 12;
      final int _cursorIndexOfOrder = 13;
      final List<SystemTts> _result = new ArrayList<SystemTts>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTts _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final ITextToSpeechEngine _tmpTts;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfTts);
        _tmpTts = __converters.stringToTts(_tmp_1);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final SpeechRuleInfo _tmpSpeechRule;
        final int _tmpTarget;
        _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
        final boolean _tmpIsStandby;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsStandby);
        _tmpIsStandby = _tmp_2 != 0;
        final Long _tmpSpecifiedStandbyId;
        if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
          _tmpSpecifiedStandbyId = null;
        } else {
          _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
        }
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        final String _tmpTagRuleId;
        _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
        final String _tmpTagName;
        _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
        final Map<String, String> _tmpTagData;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfTagData);
        _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_3);
        final long _tmpConfigId;
        _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
        _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
        _item = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getTtsCount() {
    final String _sql = "SELECT count(*) FROM sysTts";
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
  public Flow<List<SystemTts>> getFlowAllTts() {
    final String _sql = "SELECT `speechRule_target`, `speechRule_isStandby`, `speechRule_specifiedStandbyId`, `speechRule_tag`, `speechRule_tagRuleId`, `speechRule_tagName`, `speechRule_tagData`, `speechRule_configId`, `sysTts`.`id` AS `id`, `sysTts`.`groupId` AS `groupId`, `sysTts`.`displayName` AS `displayName`, `sysTts`.`isEnabled` AS `isEnabled`, `sysTts`.`tts` AS `tts`, `sysTts`.`order` AS `order` FROM sysTts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sysTts"}, new Callable<List<SystemTts>>() {
      @Override
      @NonNull
      public List<SystemTts> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTarget = 0;
          final int _cursorIndexOfIsStandby = 1;
          final int _cursorIndexOfSpecifiedStandbyId = 2;
          final int _cursorIndexOfTag = 3;
          final int _cursorIndexOfTagRuleId = 4;
          final int _cursorIndexOfTagName = 5;
          final int _cursorIndexOfTagData = 6;
          final int _cursorIndexOfConfigId = 7;
          final int _cursorIndexOfId = 8;
          final int _cursorIndexOfGroupId = 9;
          final int _cursorIndexOfDisplayName = 10;
          final int _cursorIndexOfIsEnabled = 11;
          final int _cursorIndexOfTts = 12;
          final int _cursorIndexOfOrder = 13;
          final List<SystemTts> _result = new ArrayList<SystemTts>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SystemTts _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpGroupId;
            _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
            final String _tmpDisplayName;
            if (_cursor.isNull(_cursorIndexOfDisplayName)) {
              _tmpDisplayName = null;
            } else {
              _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            }
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final ITextToSpeechEngine _tmpTts;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfTts);
            _tmpTts = __converters.stringToTts(_tmp_1);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final SpeechRuleInfo _tmpSpeechRule;
            final int _tmpTarget;
            _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
            final boolean _tmpIsStandby;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsStandby);
            _tmpIsStandby = _tmp_2 != 0;
            final Long _tmpSpecifiedStandbyId;
            if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
              _tmpSpecifiedStandbyId = null;
            } else {
              _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
            }
            final String _tmpTag;
            _tmpTag = _cursor.getString(_cursorIndexOfTag);
            final String _tmpTagRuleId;
            _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
            final String _tmpTagName;
            _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
            final Map<String, String> _tmpTagData;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfTagData);
            _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_3);
            final long _tmpConfigId;
            _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
            _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
            _item = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
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
  public int getStandbyTtsCount() {
    final String _sql = "SELECT count(speechRule_isStandby = '1') FROM sysTts";
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
  public List<SystemTts> getEnabledList(final int target, final boolean isStandbyType) {
    final String _sql = "SELECT * FROM sysTts WHERE isEnabled = '1' AND  speechRule_target = ? AND speechRule_isStandby = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, target);
    _argIndex = 2;
    final int _tmp = isStandbyType ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfTts = CursorUtil.getColumnIndexOrThrow(_cursor, "tts");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_target");
      final int _cursorIndexOfIsStandby = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_isStandby");
      final int _cursorIndexOfSpecifiedStandbyId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_specifiedStandbyId");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tag");
      final int _cursorIndexOfTagRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagRuleId");
      final int _cursorIndexOfTagName = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagName");
      final int _cursorIndexOfTagData = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagData");
      final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_configId");
      final List<SystemTts> _result = new ArrayList<SystemTts>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTts _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final boolean _tmpIsEnabled;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp_1 != 0;
        final ITextToSpeechEngine _tmpTts;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfTts);
        _tmpTts = __converters.stringToTts(_tmp_2);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final SpeechRuleInfo _tmpSpeechRule;
        final int _tmpTarget;
        _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
        final boolean _tmpIsStandby;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfIsStandby);
        _tmpIsStandby = _tmp_3 != 0;
        final Long _tmpSpecifiedStandbyId;
        if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
          _tmpSpecifiedStandbyId = null;
        } else {
          _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
        }
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        final String _tmpTagRuleId;
        _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
        final String _tmpTagName;
        _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
        final Map<String, String> _tmpTagData;
        final String _tmp_4;
        _tmp_4 = _cursor.getString(_cursorIndexOfTagData);
        _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_4);
        final long _tmpConfigId;
        _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
        _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
        _item = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SystemTts> getEnabledListByGroupId(final long groupId, final int target,
      final boolean isStandbyType) {
    final String _sql = "SELECT * FROM sysTts WHERE isEnabled = '1' AND  speechRule_target = ? AND speechRule_isStandby = ? AND groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, target);
    _argIndex = 2;
    final int _tmp = isStandbyType ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    _argIndex = 3;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfTts = CursorUtil.getColumnIndexOrThrow(_cursor, "tts");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_target");
      final int _cursorIndexOfIsStandby = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_isStandby");
      final int _cursorIndexOfSpecifiedStandbyId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_specifiedStandbyId");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tag");
      final int _cursorIndexOfTagRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagRuleId");
      final int _cursorIndexOfTagName = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagName");
      final int _cursorIndexOfTagData = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagData");
      final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_configId");
      final List<SystemTts> _result = new ArrayList<SystemTts>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTts _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final boolean _tmpIsEnabled;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp_1 != 0;
        final ITextToSpeechEngine _tmpTts;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfTts);
        _tmpTts = __converters.stringToTts(_tmp_2);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final SpeechRuleInfo _tmpSpeechRule;
        final int _tmpTarget;
        _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
        final boolean _tmpIsStandby;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfIsStandby);
        _tmpIsStandby = _tmp_3 != 0;
        final Long _tmpSpecifiedStandbyId;
        if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
          _tmpSpecifiedStandbyId = null;
        } else {
          _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
        }
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        final String _tmpTagRuleId;
        _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
        final String _tmpTagName;
        _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
        final Map<String, String> _tmpTagData;
        final String _tmp_4;
        _tmp_4 = _cursor.getString(_cursorIndexOfTagData);
        _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_4);
        final long _tmpConfigId;
        _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
        _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
        _item = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SystemTts> getTtsByGroup(final long groupId) {
    final String _sql = "SELECT * FROM sysTts WHERE groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfTts = CursorUtil.getColumnIndexOrThrow(_cursor, "tts");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_target");
      final int _cursorIndexOfIsStandby = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_isStandby");
      final int _cursorIndexOfSpecifiedStandbyId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_specifiedStandbyId");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tag");
      final int _cursorIndexOfTagRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagRuleId");
      final int _cursorIndexOfTagName = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagName");
      final int _cursorIndexOfTagData = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagData");
      final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_configId");
      final List<SystemTts> _result = new ArrayList<SystemTts>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTts _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final ITextToSpeechEngine _tmpTts;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfTts);
        _tmpTts = __converters.stringToTts(_tmp_1);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final SpeechRuleInfo _tmpSpeechRule;
        final int _tmpTarget;
        _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
        final boolean _tmpIsStandby;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsStandby);
        _tmpIsStandby = _tmp_2 != 0;
        final Long _tmpSpecifiedStandbyId;
        if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
          _tmpSpecifiedStandbyId = null;
        } else {
          _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
        }
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        final String _tmpTagRuleId;
        _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
        final String _tmpTagName;
        _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
        final Map<String, String> _tmpTagData;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfTagData);
        _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_3);
        final long _tmpConfigId;
        _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
        _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
        _item = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public SystemTts getTts(final long id) {
    final String _sql = "SELECT * FROM sysTts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfTts = CursorUtil.getColumnIndexOrThrow(_cursor, "tts");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_target");
      final int _cursorIndexOfIsStandby = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_isStandby");
      final int _cursorIndexOfSpecifiedStandbyId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_specifiedStandbyId");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tag");
      final int _cursorIndexOfTagRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagRuleId");
      final int _cursorIndexOfTagName = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagName");
      final int _cursorIndexOfTagData = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagData");
      final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_configId");
      final SystemTts _result;
      if (_cursor.moveToFirst()) {
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final ITextToSpeechEngine _tmpTts;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfTts);
        _tmpTts = __converters.stringToTts(_tmp_1);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final SpeechRuleInfo _tmpSpeechRule;
        final int _tmpTarget;
        _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
        final boolean _tmpIsStandby;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsStandby);
        _tmpIsStandby = _tmp_2 != 0;
        final Long _tmpSpecifiedStandbyId;
        if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
          _tmpSpecifiedStandbyId = null;
        } else {
          _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
        }
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        final String _tmpTagRuleId;
        _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
        final String _tmpTagName;
        _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
        final Map<String, String> _tmpTagData;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfTagData);
        _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_3);
        final long _tmpConfigId;
        _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
        _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
        _result = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
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
  public List<SystemTts> getAllEnabledTts() {
    final String _sql = "SELECT `speechRule_target`, `speechRule_isStandby`, `speechRule_specifiedStandbyId`, `speechRule_tag`, `speechRule_tagRuleId`, `speechRule_tagName`, `speechRule_tagData`, `speechRule_configId`, `sysTts`.`id` AS `id`, `sysTts`.`groupId` AS `groupId`, `sysTts`.`displayName` AS `displayName`, `sysTts`.`isEnabled` AS `isEnabled`, `sysTts`.`tts` AS `tts`, `sysTts`.`order` AS `order` FROM sysTts WHERE isEnabled = '1'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTarget = 0;
      final int _cursorIndexOfIsStandby = 1;
      final int _cursorIndexOfSpecifiedStandbyId = 2;
      final int _cursorIndexOfTag = 3;
      final int _cursorIndexOfTagRuleId = 4;
      final int _cursorIndexOfTagName = 5;
      final int _cursorIndexOfTagData = 6;
      final int _cursorIndexOfConfigId = 7;
      final int _cursorIndexOfId = 8;
      final int _cursorIndexOfGroupId = 9;
      final int _cursorIndexOfDisplayName = 10;
      final int _cursorIndexOfIsEnabled = 11;
      final int _cursorIndexOfTts = 12;
      final int _cursorIndexOfOrder = 13;
      final List<SystemTts> _result = new ArrayList<SystemTts>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTts _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final ITextToSpeechEngine _tmpTts;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfTts);
        _tmpTts = __converters.stringToTts(_tmp_1);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final SpeechRuleInfo _tmpSpeechRule;
        final int _tmpTarget;
        _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
        final boolean _tmpIsStandby;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsStandby);
        _tmpIsStandby = _tmp_2 != 0;
        final Long _tmpSpecifiedStandbyId;
        if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
          _tmpSpecifiedStandbyId = null;
        } else {
          _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
        }
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        final String _tmpTagRuleId;
        _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
        final String _tmpTagName;
        _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
        final Map<String, String> _tmpTagData;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfTagData);
        _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_3);
        final long _tmpConfigId;
        _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
        _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
        _item = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SystemTtsGroup> getAllGroup() {
    final String _sql = "SELECT `audioParams_speed`, `audioParams_volume`, `audioParams_pitch`, `SystemTtsGroup`.`groupId` AS `groupId`, `SystemTtsGroup`.`name` AS `name`, `SystemTtsGroup`.`order` AS `order`, `SystemTtsGroup`.`isExpanded` AS `isExpanded` FROM SystemTtsGroup ORDER by `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSpeed = 0;
      final int _cursorIndexOfVolume = 1;
      final int _cursorIndexOfPitch = 2;
      final int _cursorIndexOfId = 3;
      final int _cursorIndexOfName = 4;
      final int _cursorIndexOfOrder = 5;
      final int _cursorIndexOfIsExpanded = 6;
      final List<SystemTtsGroup> _result = new ArrayList<SystemTtsGroup>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTtsGroup _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final boolean _tmpIsExpanded;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsExpanded);
        _tmpIsExpanded = _tmp != 0;
        final AudioParams _tmpAudioParams;
        final float _tmpSpeed;
        _tmpSpeed = _cursor.getFloat(_cursorIndexOfSpeed);
        final float _tmpVolume;
        _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
        final float _tmpPitch;
        _tmpPitch = _cursor.getFloat(_cursorIndexOfPitch);
        _tmpAudioParams = new AudioParams(_tmpSpeed,_tmpVolume,_tmpPitch);
        _item = new SystemTtsGroup(_tmpId,_tmpName,_tmpOrder,_tmpIsExpanded,_tmpAudioParams);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getGroupCount() {
    final String _sql = "SELECT count(*) FROM SystemTtsGroup";
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
  public List<GroupWithV1TTS> getAllGroupWithTts() {
    final String _sql = "SELECT `SystemTtsGroup`.`groupId` AS `groupId`, `SystemTtsGroup`.`name` AS `name`, `SystemTtsGroup`.`order` AS `order`, `SystemTtsGroup`.`isExpanded` AS `isExpanded`, `SystemTtsGroup`.`audioParams_speed` AS `audioParams_speed`, `SystemTtsGroup`.`audioParams_volume` AS `audioParams_volume`, `SystemTtsGroup`.`audioParams_pitch` AS `audioParams_pitch` FROM SystemTtsGroup ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfId = 0;
        final int _cursorIndexOfName = 1;
        final int _cursorIndexOfOrder = 2;
        final int _cursorIndexOfIsExpanded = 3;
        final int _cursorIndexOfSpeed = 4;
        final int _cursorIndexOfVolume = 5;
        final int _cursorIndexOfPitch = 6;
        final HashMap<Long, ArrayList<SystemTts>> _collectionList = new HashMap<Long, ArrayList<SystemTts>>();
        while (_cursor.moveToNext()) {
          final long _tmpKey;
          _tmpKey = _cursor.getLong(_cursorIndexOfId);
          if (!_collectionList.containsKey(_tmpKey)) {
            _collectionList.put(_tmpKey, new ArrayList<SystemTts>());
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshipsysTtsAscomGithubJing332DatabaseEntitiesSysttsV1SystemTts(_collectionList);
        final List<GroupWithV1TTS> _result = new ArrayList<GroupWithV1TTS>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final GroupWithV1TTS _item;
          final SystemTtsGroup _tmpGroup;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final int _tmpOrder;
          _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
          final boolean _tmpIsExpanded;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsExpanded);
          _tmpIsExpanded = _tmp != 0;
          final AudioParams _tmpAudioParams;
          final float _tmpSpeed;
          _tmpSpeed = _cursor.getFloat(_cursorIndexOfSpeed);
          final float _tmpVolume;
          _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
          final float _tmpPitch;
          _tmpPitch = _cursor.getFloat(_cursorIndexOfPitch);
          _tmpAudioParams = new AudioParams(_tmpSpeed,_tmpVolume,_tmpPitch);
          _tmpGroup = new SystemTtsGroup(_tmpId,_tmpName,_tmpOrder,_tmpIsExpanded,_tmpAudioParams);
          final ArrayList<SystemTts> _tmpListCollection;
          final long _tmpKey_1;
          _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
          _tmpListCollection = _collectionList.get(_tmpKey_1);
          _item = new GroupWithV1TTS(_tmpGroup,_tmpListCollection);
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flow<List<GroupWithV1TTS>> getFlowAllGroupWithTts() {
    final String _sql = "SELECT `SystemTtsGroup`.`groupId` AS `groupId`, `SystemTtsGroup`.`name` AS `name`, `SystemTtsGroup`.`order` AS `order`, `SystemTtsGroup`.`isExpanded` AS `isExpanded`, `SystemTtsGroup`.`audioParams_speed` AS `audioParams_speed`, `SystemTtsGroup`.`audioParams_volume` AS `audioParams_volume`, `SystemTtsGroup`.`audioParams_pitch` AS `audioParams_pitch` FROM SystemTtsGroup ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"sysTts",
        "SystemTtsGroup"}, new Callable<List<GroupWithV1TTS>>() {
      @Override
      @NonNull
      public List<GroupWithV1TTS> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = 0;
            final int _cursorIndexOfName = 1;
            final int _cursorIndexOfOrder = 2;
            final int _cursorIndexOfIsExpanded = 3;
            final int _cursorIndexOfSpeed = 4;
            final int _cursorIndexOfVolume = 5;
            final int _cursorIndexOfPitch = 6;
            final HashMap<Long, ArrayList<SystemTts>> _collectionList = new HashMap<Long, ArrayList<SystemTts>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionList.containsKey(_tmpKey)) {
                _collectionList.put(_tmpKey, new ArrayList<SystemTts>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipsysTtsAscomGithubJing332DatabaseEntitiesSysttsV1SystemTts(_collectionList);
            final List<GroupWithV1TTS> _result = new ArrayList<GroupWithV1TTS>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final GroupWithV1TTS _item;
              final SystemTtsGroup _tmpGroup;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpName;
              _tmpName = _cursor.getString(_cursorIndexOfName);
              final int _tmpOrder;
              _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
              final boolean _tmpIsExpanded;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsExpanded);
              _tmpIsExpanded = _tmp != 0;
              final AudioParams _tmpAudioParams;
              final float _tmpSpeed;
              _tmpSpeed = _cursor.getFloat(_cursorIndexOfSpeed);
              final float _tmpVolume;
              _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
              final float _tmpPitch;
              _tmpPitch = _cursor.getFloat(_cursorIndexOfPitch);
              _tmpAudioParams = new AudioParams(_tmpSpeed,_tmpVolume,_tmpPitch);
              _tmpGroup = new SystemTtsGroup(_tmpId,_tmpName,_tmpOrder,_tmpIsExpanded,_tmpAudioParams);
              final ArrayList<SystemTts> _tmpListCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpListCollection = _collectionList.get(_tmpKey_1);
              _item = new GroupWithV1TTS(_tmpGroup,_tmpListCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public SystemTtsGroup getGroup(final long id) {
    final String _sql = "SELECT * FROM SystemTtsGroup WHERE groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfIsExpanded = CursorUtil.getColumnIndexOrThrow(_cursor, "isExpanded");
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "audioParams_speed");
      final int _cursorIndexOfVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "audioParams_volume");
      final int _cursorIndexOfPitch = CursorUtil.getColumnIndexOrThrow(_cursor, "audioParams_pitch");
      final SystemTtsGroup _result;
      if (_cursor.moveToFirst()) {
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final boolean _tmpIsExpanded;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsExpanded);
        _tmpIsExpanded = _tmp != 0;
        final AudioParams _tmpAudioParams;
        final float _tmpSpeed;
        _tmpSpeed = _cursor.getFloat(_cursorIndexOfSpeed);
        final float _tmpVolume;
        _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
        final float _tmpPitch;
        _tmpPitch = _cursor.getFloat(_cursorIndexOfPitch);
        _tmpAudioParams = new AudioParams(_tmpSpeed,_tmpVolume,_tmpPitch);
        _result = new SystemTtsGroup(_tmpId,_tmpName,_tmpOrder,_tmpIsExpanded,_tmpAudioParams);
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
  public List<SystemTts> getTtsListByGroupId(final long groupId) {
    final String _sql = "SELECT * FROM sysTts WHERE groupId = ? ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfTts = CursorUtil.getColumnIndexOrThrow(_cursor, "tts");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_target");
      final int _cursorIndexOfIsStandby = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_isStandby");
      final int _cursorIndexOfSpecifiedStandbyId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_specifiedStandbyId");
      final int _cursorIndexOfTag = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tag");
      final int _cursorIndexOfTagRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagRuleId");
      final int _cursorIndexOfTagName = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagName");
      final int _cursorIndexOfTagData = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_tagData");
      final int _cursorIndexOfConfigId = CursorUtil.getColumnIndexOrThrow(_cursor, "speechRule_configId");
      final List<SystemTts> _result = new ArrayList<SystemTts>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTts _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpDisplayName;
        if (_cursor.isNull(_cursorIndexOfDisplayName)) {
          _tmpDisplayName = null;
        } else {
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
        }
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final ITextToSpeechEngine _tmpTts;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfTts);
        _tmpTts = __converters.stringToTts(_tmp_1);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final SpeechRuleInfo _tmpSpeechRule;
        final int _tmpTarget;
        _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
        final boolean _tmpIsStandby;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfIsStandby);
        _tmpIsStandby = _tmp_2 != 0;
        final Long _tmpSpecifiedStandbyId;
        if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
          _tmpSpecifiedStandbyId = null;
        } else {
          _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
        }
        final String _tmpTag;
        _tmpTag = _cursor.getString(_cursorIndexOfTag);
        final String _tmpTagRuleId;
        _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
        final String _tmpTagName;
        _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
        final Map<String, String> _tmpTagData;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfTagData);
        _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_3);
        final long _tmpConfigId;
        _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
        _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
        _item = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<GroupWithSystemTts> getSysTtsWithGroups() {
    final String _sql = "SELECT `SystemTtsGroup`.`groupId` AS `groupId`, `SystemTtsGroup`.`name` AS `name`, `SystemTtsGroup`.`order` AS `order`, `SystemTtsGroup`.`isExpanded` AS `isExpanded`, `SystemTtsGroup`.`audioParams_speed` AS `audioParams_speed`, `SystemTtsGroup`.`audioParams_volume` AS `audioParams_volume`, `SystemTtsGroup`.`audioParams_pitch` AS `audioParams_pitch` FROM SystemTtsGroup ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfId = 0;
        final int _cursorIndexOfName = 1;
        final int _cursorIndexOfOrder = 2;
        final int _cursorIndexOfIsExpanded = 3;
        final int _cursorIndexOfSpeed = 4;
        final int _cursorIndexOfVolume = 5;
        final int _cursorIndexOfPitch = 6;
        final HashMap<Long, ArrayList<SystemTtsV2>> _collectionList = new HashMap<Long, ArrayList<SystemTtsV2>>();
        while (_cursor.moveToNext()) {
          final long _tmpKey;
          _tmpKey = _cursor.getLong(_cursorIndexOfId);
          if (!_collectionList.containsKey(_tmpKey)) {
            _collectionList.put(_tmpKey, new ArrayList<SystemTtsV2>());
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshipsystemTtsV2AscomGithubJing332DatabaseEntitiesSysttsSystemTtsV2(_collectionList);
        final List<GroupWithSystemTts> _result = new ArrayList<GroupWithSystemTts>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final GroupWithSystemTts _item;
          final SystemTtsGroup _tmpGroup;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final int _tmpOrder;
          _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
          final boolean _tmpIsExpanded;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsExpanded);
          _tmpIsExpanded = _tmp != 0;
          final AudioParams _tmpAudioParams;
          final float _tmpSpeed;
          _tmpSpeed = _cursor.getFloat(_cursorIndexOfSpeed);
          final float _tmpVolume;
          _tmpVolume = _cursor.getFloat(_cursorIndexOfVolume);
          final float _tmpPitch;
          _tmpPitch = _cursor.getFloat(_cursorIndexOfPitch);
          _tmpAudioParams = new AudioParams(_tmpSpeed,_tmpVolume,_tmpPitch);
          _tmpGroup = new SystemTtsGroup(_tmpId,_tmpName,_tmpOrder,_tmpIsExpanded,_tmpAudioParams);
          final ArrayList<SystemTtsV2> _tmpListCollection;
          final long _tmpKey_1;
          _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
          _tmpListCollection = _collectionList.get(_tmpKey_1);
          _item = new GroupWithSystemTts(_tmpGroup,_tmpListCollection);
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteGroupAndTts(final SystemTtsGroup group) {
    SystemTtsDao.DefaultImpls.deleteGroupAndTts(SystemTtsDao_Impl.this, group);
  }

  @Override
  public List<SystemTts> getEnabledListForSort(final int target, final boolean isStandbyType) {
    return SystemTtsDao.DefaultImpls.getEnabledListForSort(SystemTtsDao_Impl.this, target, isStandbyType);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipsysTtsAscomGithubJing332DatabaseEntitiesSysttsV1SystemTts(
      @NonNull final HashMap<Long, ArrayList<SystemTts>> _map) {
    final Set<Long> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchHashMap(_map, true, (map) -> {
        __fetchRelationshipsysTtsAscomGithubJing332DatabaseEntitiesSysttsV1SystemTts(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`groupId`,`displayName`,`isEnabled`,`tts`,`order`,`speechRule_target`,`speechRule_isStandby`,`speechRule_specifiedStandbyId`,`speechRule_tag`,`speechRule_tagRuleId`,`speechRule_tagName`,`speechRule_tagData`,`speechRule_configId` FROM `sysTts` WHERE `groupId` IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : __mapKeySet) {
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "groupId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfGroupId = 1;
      final int _cursorIndexOfDisplayName = 2;
      final int _cursorIndexOfIsEnabled = 3;
      final int _cursorIndexOfTts = 4;
      final int _cursorIndexOfOrder = 5;
      final int _cursorIndexOfTarget = 6;
      final int _cursorIndexOfIsStandby = 7;
      final int _cursorIndexOfSpecifiedStandbyId = 8;
      final int _cursorIndexOfTag = 9;
      final int _cursorIndexOfTagRuleId = 10;
      final int _cursorIndexOfTagName = 11;
      final int _cursorIndexOfTagData = 12;
      final int _cursorIndexOfConfigId = 13;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<SystemTts> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final SystemTts _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpGroupId;
          _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
          final String _tmpDisplayName;
          if (_cursor.isNull(_cursorIndexOfDisplayName)) {
            _tmpDisplayName = null;
          } else {
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
          }
          final boolean _tmpIsEnabled;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
          _tmpIsEnabled = _tmp != 0;
          final ITextToSpeechEngine _tmpTts;
          final String _tmp_1;
          _tmp_1 = _cursor.getString(_cursorIndexOfTts);
          _tmpTts = __converters.stringToTts(_tmp_1);
          final int _tmpOrder;
          _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
          final SpeechRuleInfo _tmpSpeechRule;
          final int _tmpTarget;
          _tmpTarget = _cursor.getInt(_cursorIndexOfTarget);
          final boolean _tmpIsStandby;
          final int _tmp_2;
          _tmp_2 = _cursor.getInt(_cursorIndexOfIsStandby);
          _tmpIsStandby = _tmp_2 != 0;
          final Long _tmpSpecifiedStandbyId;
          if (_cursor.isNull(_cursorIndexOfSpecifiedStandbyId)) {
            _tmpSpecifiedStandbyId = null;
          } else {
            _tmpSpecifiedStandbyId = _cursor.getLong(_cursorIndexOfSpecifiedStandbyId);
          }
          final String _tmpTag;
          _tmpTag = _cursor.getString(_cursorIndexOfTag);
          final String _tmpTagRuleId;
          _tmpTagRuleId = _cursor.getString(_cursorIndexOfTagRuleId);
          final String _tmpTagName;
          _tmpTagName = _cursor.getString(_cursorIndexOfTagName);
          final Map<String, String> _tmpTagData;
          final String _tmp_3;
          _tmp_3 = _cursor.getString(_cursorIndexOfTagData);
          _tmpTagData = MapConverters.INSTANCE.toMap(_tmp_3);
          final long _tmpConfigId;
          _tmpConfigId = _cursor.getLong(_cursorIndexOfConfigId);
          _tmpSpeechRule = new SpeechRuleInfo(_tmpTarget,_tmpIsStandby,_tmpSpecifiedStandbyId,_tmpTag,_tmpTagRuleId,_tmpTagName,_tmpTagData,_tmpConfigId);
          _item_1 = new SystemTts(_tmpId,_tmpGroupId,_tmpDisplayName,_tmpIsEnabled,_tmpSpeechRule,_tmpTts,_tmpOrder);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipsystemTtsV2AscomGithubJing332DatabaseEntitiesSysttsSystemTtsV2(
      @NonNull final HashMap<Long, ArrayList<SystemTtsV2>> _map) {
    final Set<Long> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchHashMap(_map, true, (map) -> {
        __fetchRelationshipsystemTtsV2AscomGithubJing332DatabaseEntitiesSysttsSystemTtsV2(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`displayName`,`groupId`,`isEnabled`,`order`,`config` FROM `system_tts_v2` WHERE `groupId` IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : __mapKeySet) {
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "groupId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfDisplayName = 1;
      final int _cursorIndexOfGroupId = 2;
      final int _cursorIndexOfIsEnabled = 3;
      final int _cursorIndexOfOrder = 4;
      final int _cursorIndexOfConfig = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<SystemTtsV2> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final SystemTtsV2 _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpDisplayName;
          _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
          final long _tmpGroupId;
          _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
          final boolean _tmpIsEnabled;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
          _tmpIsEnabled = _tmp != 0;
          final int _tmpOrder;
          _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
          final IConfiguration _tmpConfig;
          final String _tmp_1;
          _tmp_1 = _cursor.getString(_cursorIndexOfConfig);
          _tmpConfig = __converters_1.string2Source(_tmp_1);
          _item_1 = new SystemTtsV2(_tmpId,_tmpDisplayName,_tmpGroupId,_tmpIsEnabled,_tmpOrder,_tmpConfig);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
