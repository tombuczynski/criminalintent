package com.bignerdranch.android.criminalintent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.database.CrimeDbScheme.CrimeTable;
import com.bignerdranch.android.criminalintent.database.CrimeDbScheme.CrimeTable.Columns;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Tom Buczynski on 30.11.2022.
 */
public class CrimeDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "CrimeDbHelper";

    private SQLiteDatabase mDb;

    public CrimeDbHelper(@Nullable Context context) {
        super(context, CrimeDbScheme.NAME, null, CrimeDbScheme.VERSION);

        mDb = null;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CrimeTable.CREATE);

        Log.d(TAG, "DB " + getDatabaseName() + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion == 1) {
            db.execSQL(CrimeTable.DROP);
            db.execSQL(CrimeTable.CREATE);
        }

        Log.d(TAG, "DB " + getDatabaseName() +
                " upgrade from " + oldVersion +
                " to " + newVersion);
    }


    public void openDatabase(boolean writeable) {
        if (mDb == null) {
            mDb = writeable ? getWritableDatabase() : getReadableDatabase();
        }
    }

    @NonNull
    public List<Crime> fetchAll() {
        ArrayList<Crime> list = new ArrayList<>();

        try (CrimeCursor cursor = CrimeCursor.query(mDb, null, null)) {
            while (cursor.moveToNext()) {
                list.add(cursor.getCrime());
            }

            return list;
        }
    }

    public void insert(Crime crime) {
        mDb.insert(CrimeTable.NAME, null, createContentValues(crime));
    }

    public void update(Crime crime) {
        ContentValues v = createContentValues(crime);
        mDb.update(CrimeTable.NAME, v, Columns.CRIME_ID + " = ?", new String[] {v.getAsString(Columns.CRIME_ID)});
    }

    public void remove(UUID id) {
        mDb.delete(CrimeTable.NAME, Columns.CRIME_ID + " = ?", new String[] {id.toString()});
    }

    @NonNull
    private ContentValues createContentValues(@NonNull Crime crime) {
        ContentValues values = new ContentValues();

        values.put(Columns.CRIME_ID, crime.getId().toString());
        values.put(Columns.TITLE, crime.getTitle());
        values.put(Columns.DATE, crime.getDate().getTime());
        values.put(Columns.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(Columns.SUSPECT, crime.getSuspect());

        return values;
    }
}

