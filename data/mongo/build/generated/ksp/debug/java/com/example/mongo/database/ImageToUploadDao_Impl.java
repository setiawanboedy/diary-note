package com.example.mongo.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.mongo.database.entity.ImageToUpload;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ImageToUploadDao_Impl implements ImageToUploadDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ImageToUpload> __insertionAdapterOfImageToUpload;

  private final SharedSQLiteStatement __preparedStmtOfCleanupImage;

  public ImageToUploadDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfImageToUpload = new EntityInsertionAdapter<ImageToUpload>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `image_to_upload_table` (`id`,`remoteImagePath`,`imageUri`,`sessionUri`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ImageToUpload entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getRemoteImagePath());
        statement.bindString(3, entity.getImageUri());
        statement.bindString(4, entity.getSessionUri());
      }
    };
    this.__preparedStmtOfCleanupImage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM image_to_upload_table WHERE id=?";
        return _query;
      }
    };
  }

  @Override
  public Object addImageToUpload(final ImageToUpload imageToUpload,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfImageToUpload.insert(imageToUpload);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object cleanupImage(final int imageId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanupImage.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, imageId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfCleanupImage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllImages(final Continuation<? super List<ImageToUpload>> $completion) {
    final String _sql = "SELECT * FROM image_to_upload_table ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ImageToUpload>>() {
      @Override
      @NonNull
      public List<ImageToUpload> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRemoteImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteImagePath");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final int _cursorIndexOfSessionUri = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionUri");
          final List<ImageToUpload> _result = new ArrayList<ImageToUpload>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ImageToUpload _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpRemoteImagePath;
            _tmpRemoteImagePath = _cursor.getString(_cursorIndexOfRemoteImagePath);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpSessionUri;
            _tmpSessionUri = _cursor.getString(_cursorIndexOfSessionUri);
            _item = new ImageToUpload(_tmpId,_tmpRemoteImagePath,_tmpImageUri,_tmpSessionUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
