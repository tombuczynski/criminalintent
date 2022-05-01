package com.bignerdranch.android.criminalintent.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tom Buczynski on 19.02.2022.
 */
public class CrimeLab {
   private static CrimeLab sCrimeLab;

   private final List<Crime> mCrimeList;

   private CrimeLab() {
      mCrimeList = new ArrayList<>();
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

      for (Crime crime : mCrimeList) {
         if (crime.getId().equals(id))
            return crime;
      }

      return null;
   }

   public void populateCrimeList(int cnt) {
      mCrimeList.clear();

      for (int i = 0; i < cnt; i++) {
         Crime cr = new Crime("Sprawa nr " + (i+1), (i % 3) == 0, (i % 10) == 5);
         mCrimeList.add(cr);
      }
   }
}
