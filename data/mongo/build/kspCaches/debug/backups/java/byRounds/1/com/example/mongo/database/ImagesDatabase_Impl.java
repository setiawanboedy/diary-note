package com.example.mongo.database;

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
public final class ImagesDatabase_Impl extends ImagesDatabase {
  private volatile ImageToUploadDao _imageToUploadDao;

  private volatile ImageToDeleteDao _imageToDeleteDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `image_to_upload_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `remoteImagePath` TEXT NOT NULL, `imageUri` TEXT NOT NULL, `sessionUri` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `image_to_delete_table` (`id` INTEGER NOT NULL, `remoteImagePath` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '228b783a353073a57568ed9b5da2840e')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `image_to_upload_table`");
        db.execSQL("DROP TABLE IF EXISTS `image_to_delete_table`");
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
        final HashMap<String, TableInfo.Column> _columnsImageToUploadTable = new HashMap<String, TableInfo.Column>(4);
        _columnsImageToUploadTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImageToUploadTable.put("remoteImagePath", new TableInfo.Column("remoteImagePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImageToUploadTable.put("imageUri", new TableInfo.Column("imageUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImageToUploadTable.put("sessionUri", new TableInfo.Column("sessionUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysImageToUploadTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesImageToUploadTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoImageToUploadTable = new TableInfo("image_to_upload_table", _columnsImageToUploadTable, _foreignKeysImageToUploadTable, _indicesImageToUploadTable);
        final TableInfo _existingImageToUploadTable = TableInfo.read(db, "image_to_upload_table");
        if (!_infoImageToUploadTable.equals(_existingImageToUploadTable)) {
          return new RoomOpenHelper.ValidationResult(false, "image_to_upload_table(com.example.mongo.database.entity.ImageToUpload).\n"
                  + " Expected:\n" + _infoImageToUploadTable + "\n"
                  + " Found:\n" + _existingImageToUploadTable);
        }
        final HashMap<String, TableInfo.Column> _columnsImageToDeleteTable = new HashMap<String, TableInfo.Column>(2);
        _columnsImageToDeleteTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsImageToDeleteTable.put("remoteImagePath", new TableInfo.Column("remoteImagePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysImageToDeleteTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesImageToDeleteTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoImageToDeleteTable = new TableInfo("image_to_delete_table", _columnsImageToDeleteTable, _foreignKeysImageToDeleteTable, _indicesImageToDeleteTable);
        final TableInfo _existingImageToDeleteTable = TableInfo.read(db, "image_to_delete_table");
        if (!_infoImageToDeleteTable.equals(_existingImageToDeleteTable)) {
          return new RoomOpenHelper.ValidationResult(false, "image_to_delete_table(com.example.mongo.database.entity.ImageToDelete).\n"
                  + " Expected:\n" + _infoImageToDeleteTable + "\n"
                  + " Found:\n" + _existingImageToDeleteTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "228b783a353073a57568ed9b5da2840e", "0fd99d2f5c34b5250839312c07587f2b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "image_to_upload_table","image_to_delete_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `image_to_upload_table`");
      _db.execSQL("DELETE FROM `image_to_delete_table`");
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
    _typeConvertersMap.put(ImageToUploadDao.class, ImageToUploadDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ImageToDeleteDao.class, ImageToDeleteDao_Impl.getRequiredConverters());
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
    return _autoMigrations;
  }

  @Override
  public ImageToUploadDao imageToUploadDao() {
    if (_imageToUploadDao != null) {
      return _imageToUploadDao;
    } else {
      synchronized(this) {
        if(_imageToUploadDao == null) {
          _imageToUploadDao = new ImageToUploadDao_Impl(this);
        }
        return _imageToUploadDao;
      }
    }
  }

  @Override
  public ImageToDeleteDao imageToDeleteDao() {
    if (_imageToDeleteDao != null) {
      return _imageToDeleteDao;
    } else {
      synchronized(this) {
        if(_imageToDeleteDao == null) {
          _imageToDeleteDao = new ImageToDeleteDao_Impl(this);
        }
        return _imageToDeleteDao;
      }
    }
  }
}
