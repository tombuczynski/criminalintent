package com.bignerdranch.android.criminalintent.data;

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

    private final List<Crime> mCrimeList;
    private final Map<UUID, Integer> mCrimeMap;

    public static CrimeLab getCrimeLab() {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab();

            //sCrimeLab.populateCrimeList(75);
        }

        return sCrimeLab;
    }

    protected CrimeLab() {
        mCrimeList = new ArrayList<>();
        mCrimeMap =  new HashMap<>();
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
        }
    }

    public void populateCrimeList(int cnt) {
        mCrimeList.clear();
        mCrimeMap.clear();

        for (int i = 0; i < cnt; i++) {
            Crime cr = new Crime("Sprawa #" + (i+1), (i % 3) == 0, (i % 10) == 5);
            add(cr);
        }
    }

    public int add(@NonNull Crime crime) {
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
