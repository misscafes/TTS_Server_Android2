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
import com.github.jing332.database.entities.systts.AudioParams;
import com.github.jing332.database.entities.systts.GroupWithSystemTts;
import com.github.jing332.database.entities.systts.IConfiguration;
import com.github.jing332.database.entities.systts.SystemTtsGroup;
import com.github.jing332.database.entities.systts.SystemTtsV2;
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
import java.util.Set;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SystemTtsV2Dao_Impl implements SystemTtsV2Dao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SystemTtsV2> __insertionAdapterOfSystemTtsV2;

  private final SystemTtsV2.Converters __converters = new SystemTtsV2.Converters();

  private final EntityInsertionAdapter<SystemTtsGroup> __insertionAdapterOfSystemTtsGroup;

  private final EntityDeletionOrUpdateAdapter<SystemTtsV2> __deletionAdapterOfSystemTtsV2;

  private final EntityDeletionOrUpdateAdapter<SystemTtsGroup> __deletionAdapterOfSystemTtsGroup;

  private final EntityDeletionOrUpdateAdapter<SystemTtsV2> __updateAdapterOfSystemTtsV2;

  private final EntityDeletionOrUpdateAdapter<SystemTtsGroup> __updateAdapterOfSystemTtsGroup;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTtsByGroup;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCategoryPath;

  public SystemTtsV2Dao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSystemTtsV2 = new EntityInsertionAdapter<SystemTtsV2>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `system_tts_v2` (`id`,`displayName`,`groupId`,`isEnabled`,`order`,`categoryPath`,`config`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTtsV2 entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDisplayName());
        statement.bindLong(3, entity.getGroupId());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getOrder());
        statement.bindString(6, entity.getCategoryPath());
        final String _tmp_1 = __converters.source2String(entity.getConfig());
        statement.bindString(7, _tmp_1);
      }
    };
    this.__insertionAdapterOfSystemTtsGroup = new EntityInsertionAdapter<SystemTtsGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `SystemTtsGroup` (`groupId`,`name`,`order`,`isExpanded`,`audioParams_speed`,`audioParams_volume`,`audioParams_pitch`) VALUES (?,?,?,?,?,?,?)";
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
    this.__deletionAdapterOfSystemTtsV2 = new EntityDeletionOrUpdateAdapter<SystemTtsV2>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `system_tts_v2` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTtsV2 entity) {
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
    this.__updateAdapterOfSystemTtsV2 = new EntityDeletionOrUpdateAdapter<SystemTtsV2>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR REPLACE `system_tts_v2` SET `id` = ?,`displayName` = ?,`groupId` = ?,`isEnabled` = ?,`order` = ?,`categoryPath` = ?,`config` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SystemTtsV2 entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDisplayName());
        statement.bindLong(3, entity.getGroupId());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getOrder());
        statement.bindString(6, entity.getCategoryPath());
        final String _tmp_1 = __converters.source2String(entity.getConfig());
        statement.bindString(7, _tmp_1);
        statement.bindLong(8, entity.getId());
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
    this.__preparedStmtOfDeleteTtsByGroup = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from system_tts_v2 WHERE groupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCategoryPath = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE system_tts_v2 SET categoryPath = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final SystemTtsV2... tts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSystemTtsV2.insert(tts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertGroup(final SystemTtsGroup... group) {
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
  public void delete(final SystemTtsV2... tts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSystemTtsV2.handleMultiple(tts);
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
  public void update(final SystemTtsV2... tts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSystemTtsV2.handleMultiple(tts);
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
  public void updateCategoryPath(final long id, final String categoryPath) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCategoryPath.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, categoryPath);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateCategoryPath.release(_stmt);
    }
  }

  @Override
  public int getCount() {
    final String _sql = "SELECT COUNT(*) FROM system_tts_v2";
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
  public int getGroupCount() {
    final String _sql = "SELECT COUNT(*) FROM SystemTtsGroup";
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
  public List<SystemTtsV2> getAll() {
    final String _sql = "SELECT `system_tts_v2`.`id` AS `id`, `system_tts_v2`.`displayName` AS `displayName`, `system_tts_v2`.`groupId` AS `groupId`, `system_tts_v2`.`isEnabled` AS `isEnabled`, `system_tts_v2`.`order` AS `order`, `system_tts_v2`.`categoryPath` AS `categoryPath`, `system_tts_v2`.`config` AS `config` FROM system_tts_v2";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfDisplayName = 1;
      final int _cursorIndexOfGroupId = 2;
      final int _cursorIndexOfIsEnabled = 3;
      final int _cursorIndexOfOrder = 4;
      final int _cursorIndexOfCategoryPath = 5;
      final int _cursorIndexOfConfig = 6;
      final List<SystemTtsV2> _result = new ArrayList<SystemTtsV2>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTtsV2 _item;
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
        final String _tmpCategoryPath;
        _tmpCategoryPath = _cursor.getString(_cursorIndexOfCategoryPath);
        final IConfiguration _tmpConfig;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfConfig);
        _tmpConfig = __converters.string2Source(_tmp_1);
        _item = new SystemTtsV2(_tmpId,_tmpDisplayName,_tmpGroupId,_tmpIsEnabled,_tmpOrder,_tmpCategoryPath,_tmpConfig);
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
    final String _sql = "SELECT `audioParams_speed`, `audioParams_volume`, `audioParams_pitch`, `SystemTtsGroup`.`groupId` AS `groupId`, `SystemTtsGroup`.`name` AS `name`, `SystemTtsGroup`.`order` AS `order`, `SystemTtsGroup`.`isExpanded` AS `isExpanded` FROM SystemTtsGroup";
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
  public SystemTtsV2 get(final long id) {
    final String _sql = "SELECT * FROM system_tts_v2 WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfCategoryPath = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryPath");
      final int _cursorIndexOfConfig = CursorUtil.getColumnIndexOrThrow(_cursor, "config");
      final SystemTtsV2 _result;
      if (_cursor.moveToFirst()) {
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
        final String _tmpCategoryPath;
        _tmpCategoryPath = _cursor.getString(_cursorIndexOfCategoryPath);
        final IConfiguration _tmpConfig;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfConfig);
        _tmpConfig = __converters.string2Source(_tmp_1);
        _result = new SystemTtsV2(_tmpId,_tmpDisplayName,_tmpGroupId,_tmpIsEnabled,_tmpOrder,_tmpCategoryPath,_tmpConfig);
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
  public List<SystemTtsV2> getEnabledListByGroupId(final long groupId) {
    final String _sql = "SELECT * FROM system_tts_v2 WHERE isEnabled = '1' AND  groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfCategoryPath = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryPath");
      final int _cursorIndexOfConfig = CursorUtil.getColumnIndexOrThrow(_cursor, "config");
      final List<SystemTtsV2> _result = new ArrayList<SystemTtsV2>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTtsV2 _item;
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
        final String _tmpCategoryPath;
        _tmpCategoryPath = _cursor.getString(_cursorIndexOfCategoryPath);
        final IConfiguration _tmpConfig;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfConfig);
        _tmpConfig = __converters.string2Source(_tmp_1);
        _item = new SystemTtsV2(_tmpId,_tmpDisplayName,_tmpGroupId,_tmpIsEnabled,_tmpOrder,_tmpCategoryPath,_tmpConfig);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<GroupWithSystemTts> getAllGroupWithTts() {
    final String _sql = "SELECT `SystemTtsGroup`.`groupId` AS `groupId`, `SystemTtsGroup`.`name` AS `name`, `SystemTtsGroup`.`order` AS `order`, `SystemTtsGroup`.`isExpanded` AS `isExpanded`, `SystemTtsGroup`.`audioParams_speed` AS `audioParams_speed`, `SystemTtsGroup`.`audioParams_volume` AS `audioParams_volume`, `SystemTtsGroup`.`audioParams_pitch` AS `audioParams_pitch` FROM SystemTtsGroup ORDER BY `order`";
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
  public Flow<List<GroupWithSystemTts>> flowAllGroupWithTts() {
    final String _sql = "SELECT `SystemTtsGroup`.`groupId` AS `groupId`, `SystemTtsGroup`.`name` AS `name`, `SystemTtsGroup`.`order` AS `order`, `SystemTtsGroup`.`isExpanded` AS `isExpanded`, `SystemTtsGroup`.`audioParams_speed` AS `audioParams_speed`, `SystemTtsGroup`.`audioParams_volume` AS `audioParams_volume`, `SystemTtsGroup`.`audioParams_pitch` AS `audioParams_pitch` FROM SystemTtsGroup ORDER BY `order`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"system_tts_v2",
        "SystemTtsGroup"}, new Callable<List<GroupWithSystemTts>>() {
      @Override
      @NonNull
      public List<GroupWithSystemTts> call() throws Exception {
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
  public List<SystemTtsV2> getAllEnabled() {
    final String _sql = "SELECT `system_tts_v2`.`id` AS `id`, `system_tts_v2`.`displayName` AS `displayName`, `system_tts_v2`.`groupId` AS `groupId`, `system_tts_v2`.`isEnabled` AS `isEnabled`, `system_tts_v2`.`order` AS `order`, `system_tts_v2`.`categoryPath` AS `categoryPath`, `system_tts_v2`.`config` AS `config` FROM system_tts_v2 WHERE isEnabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfDisplayName = 1;
      final int _cursorIndexOfGroupId = 2;
      final int _cursorIndexOfIsEnabled = 3;
      final int _cursorIndexOfOrder = 4;
      final int _cursorIndexOfCategoryPath = 5;
      final int _cursorIndexOfConfig = 6;
      final List<SystemTtsV2> _result = new ArrayList<SystemTtsV2>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTtsV2 _item;
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
        final String _tmpCategoryPath;
        _tmpCategoryPath = _cursor.getString(_cursorIndexOfCategoryPath);
        final IConfiguration _tmpConfig;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfConfig);
        _tmpConfig = __converters.string2Source(_tmp_1);
        _item = new SystemTtsV2(_tmpId,_tmpDisplayName,_tmpGroupId,_tmpIsEnabled,_tmpOrder,_tmpCategoryPath,_tmpConfig);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SystemTtsV2> getByGroup(final long groupId) {
    final String _sql = "SELECT * FROM system_tts_v2 WHERE groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfCategoryPath = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryPath");
      final int _cursorIndexOfConfig = CursorUtil.getColumnIndexOrThrow(_cursor, "config");
      final List<SystemTtsV2> _result = new ArrayList<SystemTtsV2>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTtsV2 _item;
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
        final String _tmpCategoryPath;
        _tmpCategoryPath = _cursor.getString(_cursorIndexOfCategoryPath);
        final IConfiguration _tmpConfig;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfConfig);
        _tmpConfig = __converters.string2Source(_tmp_1);
        _item = new SystemTtsV2(_tmpId,_tmpDisplayName,_tmpGroupId,_tmpIsEnabled,_tmpOrder,_tmpCategoryPath,_tmpConfig);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getCategoryPathsByGroup(final long groupId) {
    final String _sql = "SELECT DISTINCT categoryPath FROM system_tts_v2 WHERE groupId = ? AND categoryPath != '' ORDER BY categoryPath ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final String _item;
        _item = _cursor.getString(0);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<GroupWithSystemTts> allGroup() {
    final String _sql = "SELECT `SystemTtsGroup`.`groupId` AS `groupId`, `SystemTtsGroup`.`name` AS `name`, `SystemTtsGroup`.`order` AS `order`, `SystemTtsGroup`.`isExpanded` AS `isExpanded`, `SystemTtsGroup`.`audioParams_speed` AS `audioParams_speed`, `SystemTtsGroup`.`audioParams_volume` AS `audioParams_volume`, `SystemTtsGroup`.`audioParams_pitch` AS `audioParams_pitch` FROM SystemTtsGroup ORDER BY `order`";
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
  public List<SystemTtsV2> getTtsListByGroupId(final long groupId) {
    final String _sql = "SELECT * FROM system_tts_v2 WHERE groupId = ? ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfCategoryPath = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryPath");
      final int _cursorIndexOfConfig = CursorUtil.getColumnIndexOrThrow(_cursor, "config");
      final List<SystemTtsV2> _result = new ArrayList<SystemTtsV2>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SystemTtsV2 _item;
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
        final String _tmpCategoryPath;
        _tmpCategoryPath = _cursor.getString(_cursorIndexOfCategoryPath);
        final IConfiguration _tmpConfig;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfConfig);
        _tmpConfig = __converters.string2Source(_tmp_1);
        _item = new SystemTtsV2(_tmpId,_tmpDisplayName,_tmpGroupId,_tmpIsEnabled,_tmpOrder,_tmpCategoryPath,_tmpConfig);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SystemTtsV2> getEnabledListByGroupId(final long groupId, final int target,
      final boolean isStandbyType) {
    return SystemTtsV2Dao.DefaultImpls.getEnabledListByGroupId(SystemTtsV2Dao_Impl.this, groupId, target, isStandbyType);
  }

  @Override
  public void insertGroupWithTts(final GroupWithSystemTts... g) {
    SystemTtsV2Dao.DefaultImpls.insertGroupWithTts(SystemTtsV2Dao_Impl.this, g);
  }

  @Override
  public List<SystemTtsV2> getEnabledListForSort(final int target, final boolean isStandbyType) {
    return SystemTtsV2Dao.DefaultImpls.getEnabledListForSort(SystemTtsV2Dao_Impl.this, target, isStandbyType);
  }

  @Override
  public void updateAllOrder() {
    SystemTtsV2Dao.DefaultImpls.updateAllOrder(SystemTtsV2Dao_Impl.this);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
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
    _stringBuilder.append("SELECT `id`,`displayName`,`groupId`,`isEnabled`,`order`,`categoryPath`,`config` FROM `system_tts_v2` WHERE `groupId` IN (");
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
      final int _cursorIndexOfCategoryPath = 5;
      final int _cursorIndexOfConfig = 6;
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
          final String _tmpCategoryPath;
          _tmpCategoryPath = _cursor.getString(_cursorIndexOfCategoryPath);
          final IConfiguration _tmpConfig;
          final String _tmp_1;
          _tmp_1 = _cursor.getString(_cursorIndexOfConfig);
          _tmpConfig = __converters.string2Source(_tmp_1);
          _item_1 = new SystemTtsV2(_tmpId,_tmpDisplayName,_tmpGroupId,_tmpIsEnabled,_tmpOrder,_tmpCategoryPath,_tmpConfig);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
