package com.bignerdranch.android.criminalintent.ui;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.data.CrimeLab;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class CrimeViewModel extends ViewModel {
    List<Crime> mCrimes = null;

    public List<Crime> getCrimes() {
        if (mCrimes == null) {
            CrimeLab crimeLab = CrimeLab.getCrimeLab();
            mCrimes = crimeLab.getCrimeList();
        }
        return mCrimes;
    }

}