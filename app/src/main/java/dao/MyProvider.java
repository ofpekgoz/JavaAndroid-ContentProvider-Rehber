package dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class MyProvider extends ContentProvider {

    static final String PROVİDER_NAME = "com.omerfpekgoz.contentprovider_rehberuygulama.dao.MyProvider";
    static final String URL = "content://" + PROVİDER_NAME + "/rehber";
    public static final Uri CONTENT_URL = Uri.parse(URL);

    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    static final String TABLE_NAME = "rehber";
    static final String ID = "id";
    static final String AD = "ad";
    static final String TEL = "tel";

    private static HashMap<String, String> RehberMap;

    static final int REHBER = 1;
    static final int REHBER_PARAMETRE = 2;

    static UriMatcher URI_MATCHER = null;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);  //İlk oluşturduk
        URI_MATCHER.addURI(PROVİDER_NAME, "rehber", REHBER);
        URI_MATCHER.addURI(PROVİDER_NAME, "rehber/*", REHBER_PARAMETRE);
    }


    @Override
    public boolean onCreate() {

        Context context = getContext();
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        if (sqLiteDatabase == null) {
            return false;
        } else {
            return true;
        }

    }

    //Veri Çekme
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(TABLE_NAME);

        switch (URI_MATCHER.match(uri)) {
            case REHBER:
                sqLiteQueryBuilder.setProjectionMap(RehberMap);
                break;
            case REHBER_PARAMETRE:
                sqLiteQueryBuilder.appendWhere(ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Bilinmeyen URI " + uri);
        }

        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, ID);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long row = sqLiteDatabase.insert(TABLE_NAME, "", values);
        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URL, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }

        throw new SQLException("Yeni kayıt oluşturma hatası " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int count = 0;
        switch (URI_MATCHER.match(uri)) {
            case REHBER:
                count = sqLiteDatabase.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case REHBER_PARAMETRE:
                count = sqLiteDatabase.delete(TABLE_NAME, ID + "=" + uri.getLastPathSegment(), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Bilinmeyen URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int count = 0;
        switch (URI_MATCHER.match(uri)) {
            case REHBER:
                count = sqLiteDatabase.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case REHBER_PARAMETRE:
                count = sqLiteDatabase.update(TABLE_NAME, values, ID + "=" + uri.getLastPathSegment(), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Bilinmeyen URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }
}
