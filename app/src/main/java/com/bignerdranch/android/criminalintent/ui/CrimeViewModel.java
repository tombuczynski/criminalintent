package com.bignerdranch.android.criminalintent.ui;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.data.CrimeLab;

import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrimeViewModel extends ViewModel {
    private CrimeLab mCrimeLab;
    private MutableLiveData<Integer> mSelectedItemPos = new MutableLiveData<>(-1);
    private MutableLiveData<Integer> mLastChangedItemPos;

    public LiveData<Integer> getSelectedItemPos() {
        if (mSelectedItemPos == null) {
            mSelectedItemPos = new MutableLiveData<>(-1);
        }
        return mSelectedItemPos;
    }

    public void setSelectedItemPos(Integer selectedItemPos) {
        if (mSelectedItemPos == null) {
            mSelectedItemPos = new MutableLiveData<>(selectedItemPos);
        } else {
            mSelectedItemPos.setValue(selectedItemPos);
        }
    }

    public LiveData<Integer> getLastChangedItemPos() {
        if (mLastChangedItemPos == null) {
            mLastChangedItemPos = new MutableLiveData<>(-1);
        }
        return mLastChangedItemPos;
    }

    public void setLastChangedItemPos(Integer lastChangedItemPos) {
        if (mLastChangedItemPos == null) {
            mLastChangedItemPos = new MutableLiveData<>(lastChangedItemPos);
        } else {
            mLastChangedItemPos.setValue(lastChangedItemPos);
        }
    }

    public List<Crime> getCrimeList() {
        loadCrimeLab();
        return mCrimeLab.getCrimeList();
    }

    public Crime getCrime(UUID id) {
        loadCrimeLab();
        return  mCrimeLab.getCrime(id);
    }

    public Crime getCrime(int itemPos) {
        loadCrimeLab();
        return  mCrimeLab.getCrimeList().get(itemPos);
    }

    private void loadCrimeLab() {
        if (mCrimeLab == null) {
            mCrimeLab = CrimeLab.getCrimeLab();
        }
    }
}