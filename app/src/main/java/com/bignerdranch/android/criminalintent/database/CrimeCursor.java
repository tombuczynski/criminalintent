package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.database.CrimeDbScheme.CrimeTable;
import com.bignerdranch.android.criminalintent.database.CrimeDbScheme.CrimeTable.Columns;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Tom Buczynski on 02.12.2022.
 */
public class CrimeCursor extends CursorWrapper {

    public static CrimeCursor query(SQLiteDatabase db, String selection, String[] selectionArgs) {
        Cursor c = db.query(
                CrimeTable.NAME,
                new String[]{Columns.CRIME_ID, Columns.TITLE, Columns.DATE, Columns.SOLVED},
                selection,
                selectionArgs,
                null,
                null,
                null
                );

        return new CrimeCursor(c);
    }

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    protected CrimeCursor(Cursor cursor) {
        super(cursor);
    }

    public UUID getId() {
        String s = getString(getColumnIndex(Columns.CRIME_ID));
        return UUID.fromString(s);
    }

    public String getTitle() {
        return getString(getColumnIndex(Columns.TITLE));
    }

    public Date getDate() {
        long l = getLong(getColumnIndex(Columns.DATE));
        return new Date(l);
    }

    public boolean getSolved() {
        int i = getInt(getColumnIndex(Columns.SOLVED));
        return i != 0;
    }

    public Crime getCrime() {
        return new Crime(getId(), getTitle(), getSolved(), getDate(), false);
    }
}
