package com.bignerdranch.android.criminalintent.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Tom Buczynski on 19.02.2022.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private final List<Crime> mCrimeList;
    private final Map<UUID, Crime> mCrimeMap;

    private CrimeLab() {
        mCrimeList = new ArrayList<>();
        mCrimeMap =  new HashMap<>();
    }

    public static CrimeLab getCrimeLab() {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab();

            sCrimeLab.populateCrimeList(75);
        }

        return sCrimeLab;
    }

    public List<Crime> getCrimeList() {
        return mCrimeList;
    }

    public Crime getCrime(UUID id) {

//      for (Crime crime : mCrimeList) {
//         if (crime.getId().equals(id))
//            return crime;
//      }
//
//      return null;

        return mCrimeMap.get(id);
    }

    public void populateCrimeList(int cnt) {
        mCrimeList.clear();
        mCrimeMap.clear();

        for (int i = 0; i < cnt; i++) {
            Crime cr = new Crime("Sprawa nr " + (i+1), (i % 3) == 0, (i % 10) == 5);
            add(cr);
        }
    }

    public void add(Crime cr) {
        mCrimeList.add(cr);
        mCrimeMap.put(cr.getId(), cr);
    }
}
