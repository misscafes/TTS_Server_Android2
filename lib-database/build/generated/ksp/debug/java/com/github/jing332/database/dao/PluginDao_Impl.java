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
import com.github.jing332.database.entities.plugin.Plugin;
import com.github.jing332.database.entities.systts.AudioParams;
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
public final class PluginDao_Impl implements PluginDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Plugin> __insertionAdapterOfPlugin;

  private final EntityDeletionOrUpdateAdapter<Plugin> __deletionAdapterOfPlugin;

  private final EntityDeletionOrUpdateAdapter<Plugin> __updateAdapterOfPlugin;

  public PluginDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlugin = new EntityInsertionAdapter<Plugin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `Plugin` (`id`,`isEnabled`,`version`,`name`,`pluginId`,`author`,`iconUrl`,`code`,`defVars`,`userVars`,`order`,`audioParams`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Plugin entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getVersion());
        statement.bindString(4, entity.getName());
        statement.bindString(5, entity.getPluginId());
        statement.bindString(6, entity.getAuthor());
        statement.bindString(7, entity.getIconUrl());
        statement.bindString(8, entity.getCode());
        final String _tmp_1 = MapConverters.INSTANCE.fromNestMap(entity.getDefVars());
        statement.bindString(9, _tmp_1);
        final String _tmp_2 = MapConverters.INSTANCE.fromMap(entity.getUserVars());
        statement.bindString(10, _tmp_2);
        statement.bindLong(11, entity.getOrder());
        final String _tmp_3 = MapConverters.INSTANCE.fromAudioParams(entity.getAudioParams());
        statement.bindString(12, _tmp_3);
      }
    };
    this.__deletionAdapterOfPlugin = new EntityDeletionOrUpdateAdapter<Plugin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Plugin` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Plugin entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPlugin = new EntityDeletionOrUpdateAdapter<Plugin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Plugin` SET `id` = ?,`isEnabled` = ?,`version` = ?,`name` = ?,`pluginId` = ?,`author` = ?,`iconUrl` = ?,`code` = ?,`defVars` = ?,`userVars` = ?,`order` = ?,`audioParams` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Plugin entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getVersion());
        statement.bindString(4, entity.getName());
        statement.bindString(5, entity.getPluginId());
        statement.bindString(6, entity.getAuthor());
        statement.bindString(7, entity.getIconUrl());
        statement.bindString(8, entity.getCode());
        final String _tmp_1 = MapConverters.INSTANCE.fromNestMap(entity.getDefVars());
        statement.bindString(9, _tmp_1);
        final String _tmp_2 = MapConverters.INSTANCE.fromMap(entity.getUserVars());
        statement.bindString(10, _tmp_2);
        statement.bindLong(11, entity.getOrder());
        final String _tmp_3 = MapConverters.INSTANCE.fromAudioParams(entity.getAudioParams());
        statement.bindString(12, _tmp_3);
        statement.bindLong(13, entity.getId());
      }
    };
  }

  @Override
  public void insert(final Plugin... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPlugin.insert(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Plugin... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPlugin.handleMultiple(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Plugin... data) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPlugin.handleMultiple(data);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Plugin> getAll() {
    final String _sql = "SELECT `plugin`.`id` AS `id`, `plugin`.`isEnabled` AS `isEnabled`, `plugin`.`version` AS `version`, `plugin`.`name` AS `name`, `plugin`.`pluginId` AS `pluginId`, `plugin`.`author` AS `author`, `plugin`.`iconUrl` AS `iconUrl`, `plugin`.`code` AS `code`, `plugin`.`defVars` AS `defVars`, `plugin`.`userVars` AS `userVars`, `plugin`.`order` AS `order`, `plugin`.`audioParams` AS `audioParams` FROM plugin ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfIsEnabled = 1;
      final int _cursorIndexOfVersion = 2;
      final int _cursorIndexOfName = 3;
      final int _cursorIndexOfPluginId = 4;
      final int _cursorIndexOfAuthor = 5;
      final int _cursorIndexOfIconUrl = 6;
      final int _cursorIndexOfCode = 7;
      final int _cursorIndexOfDefVars = 8;
      final int _cursorIndexOfUserVars = 9;
      final int _cursorIndexOfOrder = 10;
      final int _cursorIndexOfAudioParams = 11;
      final List<Plugin> _result = new ArrayList<Plugin>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Plugin _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final int _tmpVersion;
        _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpPluginId;
        _tmpPluginId = _cursor.getString(_cursorIndexOfPluginId);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        final String _tmpIconUrl;
        _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
        final String _tmpCode;
        _tmpCode = _cursor.getString(_cursorIndexOfCode);
        final Map<String, ? extends Map<String, String>> _tmpDefVars;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfDefVars);
        _tmpDefVars = MapConverters.INSTANCE.toNestMap(_tmp_1);
        final Map<String, String> _tmpUserVars;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfUserVars);
        _tmpUserVars = MapConverters.INSTANCE.toMap(_tmp_2);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final AudioParams _tmpAudioParams;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfAudioParams);
        _tmpAudioParams = MapConverters.INSTANCE.toAudioParams(_tmp_3);
        _item = new Plugin(_tmpId,_tmpIsEnabled,_tmpVersion,_tmpName,_tmpPluginId,_tmpAuthor,_tmpIconUrl,_tmpCode,_tmpDefVars,_tmpUserVars,_tmpOrder,_tmpAudioParams);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Plugin> getAllEnabled() {
    final String _sql = "SELECT `plugin`.`id` AS `id`, `plugin`.`isEnabled` AS `isEnabled`, `plugin`.`version` AS `version`, `plugin`.`name` AS `name`, `plugin`.`pluginId` AS `pluginId`, `plugin`.`author` AS `author`, `plugin`.`iconUrl` AS `iconUrl`, `plugin`.`code` AS `code`, `plugin`.`defVars` AS `defVars`, `plugin`.`userVars` AS `userVars`, `plugin`.`order` AS `order`, `plugin`.`audioParams` AS `audioParams` FROM plugin WHERE isEnabled = '1' ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfIsEnabled = 1;
      final int _cursorIndexOfVersion = 2;
      final int _cursorIndexOfName = 3;
      final int _cursorIndexOfPluginId = 4;
      final int _cursorIndexOfAuthor = 5;
      final int _cursorIndexOfIconUrl = 6;
      final int _cursorIndexOfCode = 7;
      final int _cursorIndexOfDefVars = 8;
      final int _cursorIndexOfUserVars = 9;
      final int _cursorIndexOfOrder = 10;
      final int _cursorIndexOfAudioParams = 11;
      final List<Plugin> _result = new ArrayList<Plugin>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Plugin _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final int _tmpVersion;
        _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpPluginId;
        _tmpPluginId = _cursor.getString(_cursorIndexOfPluginId);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        final String _tmpIconUrl;
        _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
        final String _tmpCode;
        _tmpCode = _cursor.getString(_cursorIndexOfCode);
        final Map<String, ? extends Map<String, String>> _tmpDefVars;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfDefVars);
        _tmpDefVars = MapConverters.INSTANCE.toNestMap(_tmp_1);
        final Map<String, String> _tmpUserVars;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfUserVars);
        _tmpUserVars = MapConverters.INSTANCE.toMap(_tmp_2);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final AudioParams _tmpAudioParams;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfAudioParams);
        _tmpAudioParams = MapConverters.INSTANCE.toAudioParams(_tmp_3);
        _item = new Plugin(_tmpId,_tmpIsEnabled,_tmpVersion,_tmpName,_tmpPluginId,_tmpAuthor,_tmpIconUrl,_tmpCode,_tmpDefVars,_tmpUserVars,_tmpOrder,_tmpAudioParams);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Flow<List<Plugin>> flowAll() {
    final String _sql = "SELECT `plugin`.`id` AS `id`, `plugin`.`isEnabled` AS `isEnabled`, `plugin`.`version` AS `version`, `plugin`.`name` AS `name`, `plugin`.`pluginId` AS `pluginId`, `plugin`.`author` AS `author`, `plugin`.`iconUrl` AS `iconUrl`, `plugin`.`code` AS `code`, `plugin`.`defVars` AS `defVars`, `plugin`.`userVars` AS `userVars`, `plugin`.`order` AS `order`, `plugin`.`audioParams` AS `audioParams` FROM plugin ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plugin"}, new Callable<List<Plugin>>() {
      @Override
      @NonNull
      public List<Plugin> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfIsEnabled = 1;
          final int _cursorIndexOfVersion = 2;
          final int _cursorIndexOfName = 3;
          final int _cursorIndexOfPluginId = 4;
          final int _cursorIndexOfAuthor = 5;
          final int _cursorIndexOfIconUrl = 6;
          final int _cursorIndexOfCode = 7;
          final int _cursorIndexOfDefVars = 8;
          final int _cursorIndexOfUserVars = 9;
          final int _cursorIndexOfOrder = 10;
          final int _cursorIndexOfAudioParams = 11;
          final List<Plugin> _result = new ArrayList<Plugin>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Plugin _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final int _tmpVersion;
            _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPluginId;
            _tmpPluginId = _cursor.getString(_cursorIndexOfPluginId);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpIconUrl;
            _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final Map<String, ? extends Map<String, String>> _tmpDefVars;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDefVars);
            _tmpDefVars = MapConverters.INSTANCE.toNestMap(_tmp_1);
            final Map<String, String> _tmpUserVars;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfUserVars);
            _tmpUserVars = MapConverters.INSTANCE.toMap(_tmp_2);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final AudioParams _tmpAudioParams;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfAudioParams);
            _tmpAudioParams = MapConverters.INSTANCE.toAudioParams(_tmp_3);
            _item = new Plugin(_tmpId,_tmpIsEnabled,_tmpVersion,_tmpName,_tmpPluginId,_tmpAuthor,_tmpIconUrl,_tmpCode,_tmpDefVars,_tmpUserVars,_tmpOrder,_tmpAudioParams);
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
    final String _sql = "SELECT count(*) FROM plugin";
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
  public Plugin getByPluginId(final String pluginId) {
    final String _sql = "SELECT * FROM plugin WHERE pluginId = ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, pluginId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfPluginId = CursorUtil.getColumnIndexOrThrow(_cursor, "pluginId");
      final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
      final int _cursorIndexOfIconUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "iconUrl");
      final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
      final int _cursorIndexOfDefVars = CursorUtil.getColumnIndexOrThrow(_cursor, "defVars");
      final int _cursorIndexOfUserVars = CursorUtil.getColumnIndexOrThrow(_cursor, "userVars");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfAudioParams = CursorUtil.getColumnIndexOrThrow(_cursor, "audioParams");
      final Plugin _result;
      if (_cursor.moveToFirst()) {
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final int _tmpVersion;
        _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpPluginId;
        _tmpPluginId = _cursor.getString(_cursorIndexOfPluginId);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        final String _tmpIconUrl;
        _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
        final String _tmpCode;
        _tmpCode = _cursor.getString(_cursorIndexOfCode);
        final Map<String, ? extends Map<String, String>> _tmpDefVars;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfDefVars);
        _tmpDefVars = MapConverters.INSTANCE.toNestMap(_tmp_1);
        final Map<String, String> _tmpUserVars;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfUserVars);
        _tmpUserVars = MapConverters.INSTANCE.toMap(_tmp_2);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final AudioParams _tmpAudioParams;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfAudioParams);
        _tmpAudioParams = MapConverters.INSTANCE.toAudioParams(_tmp_3);
        _result = new Plugin(_tmpId,_tmpIsEnabled,_tmpVersion,_tmpName,_tmpPluginId,_tmpAuthor,_tmpIconUrl,_tmpCode,_tmpDefVars,_tmpUserVars,_tmpOrder,_tmpAudioParams);
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
  public Plugin getEnabled(final String pluginId) {
    final String _sql = "SELECT * FROM plugin WHERE pluginId = ? AND isEnabled";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, pluginId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
      final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfPluginId = CursorUtil.getColumnIndexOrThrow(_cursor, "pluginId");
      final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
      final int _cursorIndexOfIconUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "iconUrl");
      final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
      final int _cursorIndexOfDefVars = CursorUtil.getColumnIndexOrThrow(_cursor, "defVars");
      final int _cursorIndexOfUserVars = CursorUtil.getColumnIndexOrThrow(_cursor, "userVars");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfAudioParams = CursorUtil.getColumnIndexOrThrow(_cursor, "audioParams");
      final Plugin _result;
      if (_cursor.moveToFirst()) {
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final boolean _tmpIsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
        _tmpIsEnabled = _tmp != 0;
        final int _tmpVersion;
        _tmpVersion = _cursor.getInt(_cursorIndexOfVersion);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpPluginId;
        _tmpPluginId = _cursor.getString(_cursorIndexOfPluginId);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        final String _tmpIconUrl;
        _tmpIconUrl = _cursor.getString(_cursorIndexOfIconUrl);
        final String _tmpCode;
        _tmpCode = _cursor.getString(_cursorIndexOfCode);
        final Map<String, ? extends Map<String, String>> _tmpDefVars;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfDefVars);
        _tmpDefVars = MapConverters.INSTANCE.toNestMap(_tmp_1);
        final Map<String, String> _tmpUserVars;
        final String _tmp_2;
        _tmp_2 = _cursor.getString(_cursorIndexOfUserVars);
        _tmpUserVars = MapConverters.INSTANCE.toMap(_tmp_2);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final AudioParams _tmpAudioParams;
        final String _tmp_3;
        _tmp_3 = _cursor.getString(_cursorIndexOfAudioParams);
        _tmpAudioParams = MapConverters.INSTANCE.toAudioParams(_tmp_3);
        _result = new Plugin(_tmpId,_tmpIsEnabled,_tmpVersion,_tmpName,_tmpPluginId,_tmpAuthor,_tmpIconUrl,_tmpCode,_tmpDefVars,_tmpUserVars,_tmpOrder,_tmpAudioParams);
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
  public void insertOrUpdate(final Plugin... args) {
    PluginDao.DefaultImpls.insertOrUpdate(PluginDao_Impl.this, args);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
