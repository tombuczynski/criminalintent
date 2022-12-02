package com.bignerdranch.android.criminalintent.data;

import android.content.Context;

import com.bignerdranch.android.criminalintent.database.CrimeDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;

/**
 * Created by Tom Buczynski on 19.02.2022.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimeList;
    private final Map<UUID, Integer> mCrimeMap;
    private final CrimeDbHelper mDbHelper;

    public static CrimeLab getCrimeLab(Context context) {
        if (sCrimeLab == null) {

            CrimeDbHelper dbHelper = context != null ? new CrimeDbHelper(context.getApplicationContext()) : null;
            sCrimeLab = new CrimeLab(dbHelper);

            if (dbHelper == null)
                sCrimeLab.populateCrimeList(75);
            else
                sCrimeLab.fetchCrimeList();
        }

        return sCrimeLab;
    }

    protected CrimeLab(CrimeDbHelper dbHelper) {
        mCrimeList = new ArrayList<>();
        mCrimeMap =  new HashMap<>();
        mDbHelper = dbHelper;
    }

    public List<Crime> getCrimeList() {
        return mCrimeList;
    }

    public int getCrimeItemPos(UUID id) {
        if (id == null)
            return -1;

        Integer pos = mCrimeMap.get(id);
        return pos != null ? pos : -1;
    }

    public Crime getCrime(UUID id) {

//      for (Crime crime : mCrimeList) {
//         if (crime.getId().equals(id))
//            return crime;
//      }
//
//      return null;
        int itemPos = getCrimeItemPos(id);
        return itemPos >= 0 ? mCrimeList.get(itemPos) : null;
    }

    public void removeCrime(UUID id) {
        int itemPos = getCrimeItemPos(id);
        if (itemPos >= 0 ) {
            mCrimeList.remove(itemPos);
            updateMap();

            if (mDbHelper != null) {
                mDbHelper.remove(id);
            }
        }
    }

    public void populateCrimeList(int cnt) {
        mCrimeList = new ArrayList<>();
        mCrimeMap.clear();

        for (int i = 0; i < cnt; i++) {
            Crime cr = new Crime("Sprawa #" + (i+1), (i % 3) == 0, (i % 10) == 5);
            add(cr);
        }
    }

    public void fetchCrimeList() {
        if (mDbHelper != null) {
            mDbHelper.openDatabase(true);
            mCrimeList = mDbHelper.fetchAll();
        }

        updateMap();
    }

    public int insert(@NonNull Crime crime) {
        if (mDbHelper != null) {
            mDbHelper.insert(crime);
        }

        return add(crime);
    }

    public void update(@NonNull Crime crime) {
        if (mDbHelper != null) {
            mDbHelper.update(crime);
        }
    }

    private int add(@NonNull Crime crime) {
        int itemPos = mCrimeList.size();

        mCrimeMap.put(crime.getId(), itemPos);
        mCrimeList.add(crime);

        return itemPos;
    }

    private void updateMap() {
        mCrimeMap.clear();

        for (int i = 0; i < mCrimeList.size(); i++) {
            Crime crime = mCrimeList.get(i);

            mCrimeMap.put(crime.getId(), i);
        }
    }
}
