package com.github.nahratzah.speakerphone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Closeable;

/**
 * Database module for storing and retrieving speech buttons.
 */
public class SpeechButtonDb extends SQLiteOpenHelper {
    private static class SpeechButtonEntry implements BaseColumns {
        static final String TABLE_NAME = "speech_button";
        static final String COLUMN_KEY = "key";
        static final String COLUMN_LABEL = "label";
        static final String COLUMN_TEXT = "text";
    }

    private static final String DATABASE_NAME = "speech_button.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE = "create table " + SpeechButtonEntry.TABLE_NAME + "("
            + SpeechButtonEntry._ID + " integer primary key autoincrement, "
            + SpeechButtonEntry.COLUMN_KEY + " varchar unique not null, "
            + SpeechButtonEntry.COLUMN_LABEL + " varchar not null, "
            + SpeechButtonEntry.COLUMN_TEXT + " text not null"
            + ")";

    public SpeechButtonDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("upgrading logic is not implemented");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("upgrading logic is not implemented");
    }

    public DatabaseRO getReadOnlyDatabase() {
        return new DatabaseRO(this);
    }

    public DatabaseRW getReadWriteDatabase() {
        return new DatabaseRW(this);
    }

    public static class DatabaseRO implements Closeable {
        final SQLiteDatabase db;

        DatabaseRO(SQLiteDatabase db) {
            this.db = db;
        }

        DatabaseRO(SpeechButtonDb sbdb) {
            this(sbdb.getReadableDatabase());
        }

        @Nullable Integer getIdByKey(@NonNull String key) {
            final Cursor cursor = db.query(
                    SpeechButtonEntry.TABLE_NAME,  // Table
                    new String[]{ SpeechButtonEntry._ID },  // Selected columns
                    SpeechButtonEntry.COLUMN_KEY + " = ?",  // Where clause
                    new String[]{ key },  // Arguments in where clause
                    null,  // Group by
                    null,  // Having
                    null  // Order by
            );
            //noinspection TryFinallyCanBeTryWithResources
            try {
                if (cursor.moveToNext())
                    return cursor.getInt(0);
                return null;
            } finally {
                cursor.close();
            }
        }

        public @Nullable Entity getByKey(@NonNull String key) {
            final Cursor cursor = db.query(
                    SpeechButtonEntry.TABLE_NAME,  // Table
                    new String[]{ SpeechButtonEntry._ID, SpeechButtonEntry.COLUMN_KEY, SpeechButtonEntry.COLUMN_LABEL, SpeechButtonEntry.COLUMN_TEXT },  // Selected columns
                    SpeechButtonEntry.COLUMN_KEY + " = ?",  // Where clause
                    new String[]{ key },  // Arguments in where clause
                    null,  // Group by
                    null,  // Having
                    null  // Order by
            );
            //noinspection TryFinallyCanBeTryWithResources
            try {
                if (cursor.moveToNext())
                    return new Entity(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                return null;
            } finally {
                cursor.close();
            }
        }

        @Override
        public void close() {
            db.close();
        }
    }

    public static class DatabaseRW extends DatabaseRO {
        DatabaseRW(SpeechButtonDb sbdb) {
            super(sbdb.getWritableDatabase());
        }

        public void setEntity(@NonNull String key, @NonNull String label, @NonNull String text) {
            final Integer id = getIdByKey(key);
            final ContentValues newEntity = new ContentValues();
            newEntity.put(SpeechButtonEntry.COLUMN_KEY, key);
            newEntity.put(SpeechButtonEntry.COLUMN_LABEL, label);
            newEntity.put(SpeechButtonEntry.COLUMN_TEXT, text);
            if (id == null)
                db.insert(SpeechButtonEntry.TABLE_NAME, null, newEntity);
            else
                db.update(SpeechButtonEntry.TABLE_NAME, newEntity, SpeechButtonEntry._ID + " = ?", new String[]{ id.toString() });
        }

        public void deleteEntity(@NonNull String key) {
            final Integer id = getIdByKey(key);
            if (id != null)
                db.delete(SpeechButtonEntry.TABLE_NAME, SpeechButtonEntry._ID + " = ?", new String[]{ id.toString() });
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class Entity {
        @Nullable
        public Integer id;
        @Nullable
        public String key;
        @Nullable
        public String label;
        @Nullable
        public String text;

        public Entity() {}

        public Entity(@Nullable Integer id, @Nullable String key, @Nullable String label, @Nullable String text) {
            this.id = id;
            this.key = key;
            this.label = label;
            this.text = text;
        }
    }
}
