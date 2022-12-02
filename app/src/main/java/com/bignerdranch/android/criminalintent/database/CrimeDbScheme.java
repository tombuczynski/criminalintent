package com.bignerdranch.android.criminalintent.database;

import android.provider.BaseColumns;

/**
 * Created by Tom Buczynski on 30.11.2022.
 */
public final class CrimeDbScheme {
    public static final String NAME = "crimes.db";
    public static final int VERSION = 1;

    private CrimeDbScheme() {
    }

    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Columns implements BaseColumns {
            public static final String _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";

            public static final String CRIME_ID = "uuid";
            public static final String CRIME_ID_TYPE = "TEXT";

            public static final String TITLE = "title";
            public static final String TITLE_TYPE = "TEXT";

            public static final String DATE = "date";
            public static final String DATE_TYPE = "INTEGER";

            public static final String SOLVED = "solved";
            public static final String SOLVED_TYPE = "INTEGER";

        }

        public static final String CREATE =
                "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
                        Columns._ID + " " + Columns._ID_TYPE + ", " +
                        Columns.CRIME_ID + " " + Columns.CRIME_ID_TYPE + ", " +
                        Columns.TITLE + " " + Columns.TITLE_TYPE + ", " +
                        Columns.DATE + " " + Columns.DATE_TYPE + ", " +
                        Columns.SOLVED + " " + Columns.SOLVED_TYPE +
                        ")";

    }
}
