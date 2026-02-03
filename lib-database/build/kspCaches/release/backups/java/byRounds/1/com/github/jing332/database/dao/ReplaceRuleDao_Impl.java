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
import com.github.jing332.database.entities.replace.GroupWithReplaceRule;
import com.github.jing332.database.entities.replace.ReplaceRule;
import com.github.jing332.database.entities.replace.ReplaceRuleGroup;
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
public final class ReplaceRuleDao_Impl implements ReplaceRuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReplaceRuleGroup> __insertionAdapterOfReplaceRuleGroup;

  private final EntityInsertionAdapter<ReplaceRule> __insertionAdapterOfReplaceRule;

  private final EntityDeletionOrUpdateAdapter<ReplaceRuleGroup> __deletionAdapterOfReplaceRuleGroup;

  private final EntityDeletionOrUpdateAdapter<ReplaceRule> __deletionAdapterOfReplaceRule;

  private final EntityDeletionOrUpdateAdapter<ReplaceRuleGroup> __updateAdapterOfReplaceRuleGroup;

  private final EntityDeletionOrUpdateAdapter<ReplaceRule> __updateAdapterOfReplaceRule;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllByGroup;

  public ReplaceRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReplaceRuleGroup = new EntityInsertionAdapter<ReplaceRuleGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `replaceRuleGroup` (`id`,`name`,`order`,`isExpanded`,`onExecution`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReplaceRuleGroup entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getOrder());
        final int _tmp = entity.isExpanded() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getOnExecution());
      }
    };
    this.__insertionAdapterOfReplaceRule = new EntityInsertionAdapter<ReplaceRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `replaceRule` (`id`,`groupId`,`name`,`isEnabled`,`isRegex`,`pattern`,`replacement`,`order`,`sampleText`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReplaceRule entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getGroupId());
        statement.bindString(3, entity.getName());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isRegex() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindString(6, entity.getPattern());
        statement.bindString(7, entity.getReplacement());
        statement.bindLong(8, entity.getOrder());
        statement.bindString(9, entity.getSampleText());
      }
    };
    this.__deletionAdapterOfReplaceRuleGroup = new EntityDeletionOrUpdateAdapter<ReplaceRuleGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `replaceRuleGroup` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReplaceRuleGroup entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfReplaceRule = new EntityDeletionOrUpdateAdapter<ReplaceRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `replaceRule` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReplaceRule entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfReplaceRuleGroup = new EntityDeletionOrUpdateAdapter<ReplaceRuleGroup>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `replaceRuleGroup` SET `id` = ?,`name` = ?,`order` = ?,`isExpanded` = ?,`onExecution` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReplaceRuleGroup entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getOrder());
        final int _tmp = entity.isExpanded() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getOnExecution());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__updateAdapterOfReplaceRule = new EntityDeletionOrUpdateAdapter<ReplaceRule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `replaceRule` SET `id` = ?,`groupId` = ?,`name` = ?,`isEnabled` = ?,`isRegex` = ?,`pattern` = ?,`replacement` = ?,`order` = ?,`sampleText` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReplaceRule entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getGroupId());
        statement.bindString(3, entity.getName());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isRegex() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindString(6, entity.getPattern());
        statement.bindString(7, entity.getReplacement());
        statement.bindLong(8, entity.getOrder());
        statement.bindString(9, entity.getSampleText());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllByGroup = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE from ReplaceRule WHERE groupId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertGroup(final ReplaceRuleGroup... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfReplaceRuleGroup.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final ReplaceRule... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfReplaceRule.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteGroup(final ReplaceRuleGroup... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfReplaceRuleGroup.handleMultiple(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ReplaceRule... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfReplaceRule.handleMultiple(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateGroup(final ReplaceRuleGroup... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfReplaceRuleGroup.handleMultiple(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ReplaceRule... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfReplaceRule.handleMultiple(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<GroupWithReplaceRule> allGroupWithReplaceRules() {
    __db.beginTransaction();
    try {
      final List<GroupWithReplaceRule> _result;
      _result = ReplaceRuleDao.DefaultImpls.allGroupWithReplaceRules(ReplaceRuleDao_Impl.this);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllByGroup(final long groupId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllByGroup.acquire();
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
      __preparedStmtOfDeleteAllByGroup.release(_stmt);
    }
  }

  @Override
  public List<ReplaceRule> getAll() {
    final String _sql = "SELECT `ReplaceRule`.`id` AS `id`, `ReplaceRule`.`groupId` AS `groupId`, `ReplaceRule`.`name` AS `name`, `ReplaceRule`.`isEnabled` AS `isEnabled`, `ReplaceRule`.`isRegex` AS `isRegex`, `ReplaceRule`.`pattern` AS `pattern`, `ReplaceRule`.`replacement` AS `replacement`, `ReplaceRule`.`order` AS `order`, `ReplaceRule`.`sampleText` AS `sampleText` FROM ReplaceRule ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfGroupId = 1;
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfIsEnabled = 3;
      final int _cursorIndexOfIsRegex = 4;
      final int _cursorIndexOfPattern = 5;
      final int _cursorIndexOfReplacement = 6;
      final int _cursorIndexOfOrder = 7;
      final int _cursorIndexOfSampleText = 8;
      final List<ReplaceRule> _result = new ArrayList<ReplaceRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReplaceRule _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final boolean _tmpIsRegex;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsRegex);
        _tmpIsRegex = _tmp_1 != 0;
        final String _tmpPattern;
        _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
        final String _tmpReplacement;
        _tmpReplacement = _cursor.getString(_cursorIndexOfReplacement);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final String _tmpSampleText;
        _tmpSampleText = _cursor.getString(_cursorIndexOfSampleText);
        _item = new ReplaceRule(_tmpId,_tmpGroupId,_tmpName,_tmpIsEnabled,_tmpIsRegex,_tmpPattern,_tmpReplacement,_tmpOrder,_tmpSampleText);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ReplaceRule> getAllEnabled() {
    final String _sql = "SELECT `ReplaceRule`.`id` AS `id`, `ReplaceRule`.`groupId` AS `groupId`, `ReplaceRule`.`name` AS `name`, `ReplaceRule`.`isEnabled` AS `isEnabled`, `ReplaceRule`.`isRegex` AS `isRegex`, `ReplaceRule`.`pattern` AS `pattern`, `ReplaceRule`.`replacement` AS `replacement`, `ReplaceRule`.`order` AS `order`, `ReplaceRule`.`sampleText` AS `sampleText` FROM ReplaceRule WHERE isEnabled = '1' ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfGroupId = 1;
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfIsEnabled = 3;
      final int _cursorIndexOfIsRegex = 4;
      final int _cursorIndexOfPattern = 5;
      final int _cursorIndexOfReplacement = 6;
      final int _cursorIndexOfOrder = 7;
      final int _cursorIndexOfSampleText = 8;
      final List<ReplaceRule> _result = new ArrayList<ReplaceRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReplaceRule _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final boolean _tmpIsRegex;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsRegex);
        _tmpIsRegex = _tmp_1 != 0;
        final String _tmpPattern;
        _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
        final String _tmpReplacement;
        _tmpReplacement = _cursor.getString(_cursorIndexOfReplacement);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final String _tmpSampleText;
        _tmpSampleText = _cursor.getString(_cursorIndexOfSampleText);
        _item = new ReplaceRule(_tmpId,_tmpGroupId,_tmpName,_tmpIsEnabled,_tmpIsRegex,_tmpPattern,_tmpReplacement,_tmpOrder,_tmpSampleText);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Flow<List<ReplaceRule>> flowAll() {
    final String _sql = "SELECT `ReplaceRule`.`id` AS `id`, `ReplaceRule`.`groupId` AS `groupId`, `ReplaceRule`.`name` AS `name`, `ReplaceRule`.`isEnabled` AS `isEnabled`, `ReplaceRule`.`isRegex` AS `isRegex`, `ReplaceRule`.`pattern` AS `pattern`, `ReplaceRule`.`replacement` AS `replacement`, `ReplaceRule`.`order` AS `order`, `ReplaceRule`.`sampleText` AS `sampleText` FROM ReplaceRule ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ReplaceRule"}, new Callable<List<ReplaceRule>>() {
      @Override
      @NonNull
      public List<ReplaceRule> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfGroupId = 1;
          final int _cursorIndexOfName = 2;
          final int _cursorIndexOfIsEnabled = 3;
          final int _cursorIndexOfIsRegex = 4;
          final int _cursorIndexOfPattern = 5;
          final int _cursorIndexOfReplacement = 6;
          final int _cursorIndexOfOrder = 7;
          final int _cursorIndexOfSampleText = 8;
          final List<ReplaceRule> _result = new ArrayList<ReplaceRule>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReplaceRule _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpGroupId;
            _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final boolean _tmpIsRegex;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRegex);
            _tmpIsRegex = _tmp_1 != 0;
            final String _tmpPattern;
            _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
            final String _tmpReplacement;
            _tmpReplacement = _cursor.getString(_cursorIndexOfReplacement);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final String _tmpSampleText;
            _tmpSampleText = _cursor.getString(_cursorIndexOfSampleText);
            _item = new ReplaceRule(_tmpId,_tmpGroupId,_tmpName,_tmpIsEnabled,_tmpIsRegex,_tmpPattern,_tmpReplacement,_tmpOrder,_tmpSampleText);
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
    final String _sql = "SELECT count(*) FROM ReplaceRule";
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
  public ReplaceRule get(final long id) {
    final String _sql = "SELECT * FROM ReplaceRule WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfIsRegex = CursorUtil.getColumnIndexOrThrow(_cursor, "isRegex");
      final int _cursorIndexOfPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "pattern");
      final int _cursorIndexOfReplacement = CursorUtil.getColumnIndexOrThrow(_cursor, "replacement");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfSampleText = CursorUtil.getColumnIndexOrThrow(_cursor, "sampleText");
      final ReplaceRule _result;
      if (_cursor.moveToFirst()) {
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final boolean _tmpIsRegex;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsRegex);
        _tmpIsRegex = _tmp_1 != 0;
        final String _tmpPattern;
        _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
        final String _tmpReplacement;
        _tmpReplacement = _cursor.getString(_cursorIndexOfReplacement);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final String _tmpSampleText;
        _tmpSampleText = _cursor.getString(_cursorIndexOfSampleText);
        _result = new ReplaceRule(_tmpId,_tmpGroupId,_tmpName,_tmpIsEnabled,_tmpIsRegex,_tmpPattern,_tmpReplacement,_tmpOrder,_tmpSampleText);
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
  public List<ReplaceRuleGroup> getAllGroup() {
    final String _sql = "SELECT `replaceRuleGroup`.`id` AS `id`, `replaceRuleGroup`.`name` AS `name`, `replaceRuleGroup`.`order` AS `order`, `replaceRuleGroup`.`isExpanded` AS `isExpanded`, `replaceRuleGroup`.`onExecution` AS `onExecution` FROM replaceRuleGroup ORDER by `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfOrder = 2;
      final int _cursorIndexOfIsExpanded = 3;
      final int _cursorIndexOfOnExecution = 4;
      final List<ReplaceRuleGroup> _result = new ArrayList<ReplaceRuleGroup>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReplaceRuleGroup _item;
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
        final int _tmpOnExecution;
        _tmpOnExecution = _cursor.getInt(_cursorIndexOfOnExecution);
        _item = new ReplaceRuleGroup(_tmpId,_tmpName,_tmpOrder,_tmpIsExpanded,_tmpOnExecution);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Flow<List<GroupWithReplaceRule>> internalFlowAllGroupWithReplaceRules() {
    final String _sql = "SELECT `replaceRuleGroup`.`id` AS `id`, `replaceRuleGroup`.`name` AS `name`, `replaceRuleGroup`.`order` AS `order`, `replaceRuleGroup`.`isExpanded` AS `isExpanded`, `replaceRuleGroup`.`onExecution` AS `onExecution` FROM replaceRuleGroup ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"replaceRule",
        "replaceRuleGroup"}, new Callable<List<GroupWithReplaceRule>>() {
      @Override
      @NonNull
      public List<GroupWithReplaceRule> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = 0;
            final int _cursorIndexOfName = 1;
            final int _cursorIndexOfOrder = 2;
            final int _cursorIndexOfIsExpanded = 3;
            final int _cursorIndexOfOnExecution = 4;
            final HashMap<Long, ArrayList<ReplaceRule>> _collectionList = new HashMap<Long, ArrayList<ReplaceRule>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionList.containsKey(_tmpKey)) {
                _collectionList.put(_tmpKey, new ArrayList<ReplaceRule>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipreplaceRuleAscomGithubJing332DatabaseEntitiesReplaceReplaceRule(_collectionList);
            final List<GroupWithReplaceRule> _result = new ArrayList<GroupWithReplaceRule>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final GroupWithReplaceRule _item;
              final ReplaceRuleGroup _tmpGroup;
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
              final int _tmpOnExecution;
              _tmpOnExecution = _cursor.getInt(_cursorIndexOfOnExecution);
              _tmpGroup = new ReplaceRuleGroup(_tmpId,_tmpName,_tmpOrder,_tmpIsExpanded,_tmpOnExecution);
              final ArrayList<ReplaceRule> _tmpListCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpListCollection = _collectionList.get(_tmpKey_1);
              _item = new GroupWithReplaceRule(_tmpGroup,_tmpListCollection);
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
  public ReplaceRuleGroup getGroup(final long id) {
    final String _sql = "SELECT * FROM replaceRuleGroup WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfIsExpanded = CursorUtil.getColumnIndexOrThrow(_cursor, "isExpanded");
      final int _cursorIndexOfOnExecution = CursorUtil.getColumnIndexOrThrow(_cursor, "onExecution");
      final ReplaceRuleGroup _result;
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
        final int _tmpOnExecution;
        _tmpOnExecution = _cursor.getInt(_cursorIndexOfOnExecution);
        _result = new ReplaceRuleGroup(_tmpId,_tmpName,_tmpOrder,_tmpIsExpanded,_tmpOnExecution);
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
  public List<ReplaceRule> getListInGroup(final long groupId) {
    final String _sql = "SELECT * FROM ReplaceRule WHERE groupId = ? ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, groupId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfIsRegex = CursorUtil.getColumnIndexOrThrow(_cursor, "isRegex");
      final int _cursorIndexOfPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "pattern");
      final int _cursorIndexOfReplacement = CursorUtil.getColumnIndexOrThrow(_cursor, "replacement");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfSampleText = CursorUtil.getColumnIndexOrThrow(_cursor, "sampleText");
      final List<ReplaceRule> _result = new ArrayList<ReplaceRule>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ReplaceRule _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpGroupId;
        _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final boolean _tmpIsRegex;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsRegex);
        _tmpIsRegex = _tmp_1 != 0;
        final String _tmpPattern;
        _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
        final String _tmpReplacement;
        _tmpReplacement = _cursor.getString(_cursorIndexOfReplacement);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final String _tmpSampleText;
        _tmpSampleText = _cursor.getString(_cursorIndexOfSampleText);
        _item = new ReplaceRule(_tmpId,_tmpGroupId,_tmpName,_tmpIsEnabled,_tmpIsRegex,_tmpPattern,_tmpReplacement,_tmpOrder,_tmpSampleText);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Flow<List<GroupWithReplaceRule>> flowAllGroupWithReplaceRules() {
    return ReplaceRuleDao.DefaultImpls.flowAllGroupWithReplaceRules(ReplaceRuleDao_Impl.this);
  }

  @Override
  public void updateDataAndRefreshOrder(final ReplaceRule data) {
    ReplaceRuleDao.DefaultImpls.updateDataAndRefreshOrder(ReplaceRuleDao_Impl.this, data);
  }

  @Override
  public void updateAllOrder() {
    ReplaceRuleDao.DefaultImpls.updateAllOrder(ReplaceRuleDao_Impl.this);
  }

  @Override
  public void insertRuleWithGroup(final GroupWithReplaceRule... args) {
    ReplaceRuleDao.DefaultImpls.insertRuleWithGroup(ReplaceRuleDao_Impl.this, args);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipreplaceRuleAscomGithubJing332DatabaseEntitiesReplaceReplaceRule(
      @NonNull final HashMap<Long, ArrayList<ReplaceRule>> _map) {
    final Set<Long> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchHashMap(_map, true, (map) -> {
        __fetchRelationshipreplaceRuleAscomGithubJing332DatabaseEntitiesReplaceReplaceRule(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`groupId`,`name`,`isEnabled`,`isRegex`,`pattern`,`replacement`,`order`,`sampleText` FROM `replaceRule` WHERE `groupId` IN (");
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
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfIsEnabled = 3;
      final int _cursorIndexOfIsRegex = 4;
      final int _cursorIndexOfPattern = 5;
      final int _cursorIndexOfReplacement = 6;
      final int _cursorIndexOfOrder = 7;
      final int _cursorIndexOfSampleText = 8;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<ReplaceRule> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ReplaceRule _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpGroupId;
          _tmpGroupId = _cursor.getLong(_cursorIndexOfGroupId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final boolean _tmpIsEnabled;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
          _tmpIsEnabled = _tmp != 0;
          final boolean _tmpIsRegex;
          final int _tmp_1;
          _tmp_1 = _cursor.getInt(_cursorIndexOfIsRegex);
          _tmpIsRegex = _tmp_1 != 0;
          final String _tmpPattern;
          _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
          final String _tmpReplacement;
          _tmpReplacement = _cursor.getString(_cursorIndexOfReplacement);
          final int _tmpOrder;
          _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
          final String _tmpSampleText;
          _tmpSampleText = _cursor.getString(_cursorIndexOfSampleText);
          _item_1 = new ReplaceRule(_tmpId,_tmpGroupId,_tmpName,_tmpIsEnabled,_tmpIsRegex,_tmpPattern,_tmpReplacement,_tmpOrder,_tmpSampleText);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
