package com.bignerdranch.android.criminalintent.ui;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.data.CrimeLab;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrimeViewModel extends ViewModel {
    private CrimeLab mCrimeLab;
    private MutableLiveData<Integer> mSelectedItemPos;
    private MutableLiveData<HashSet<UUID>> mModifiedItems;

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

    public LiveData<HashSet<UUID>> getModifiedItems() {
        if (mModifiedItems == null) {
            mModifiedItems = new MutableLiveData<>(new HashSet<>());
        }
        return mModifiedItems;
    }

    public void setModifiedItems(HashSet<UUID> modifiedItems) {
        if (mModifiedItems == null) {
            mModifiedItems = new MutableLiveData<>(modifiedItems);
        } else {
            mModifiedItems.setValue(modifiedItems);
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

    public int getCrimeItemPos(UUID id) {
        loadCrimeLab();
        return  mCrimeLab.getCrimeItemPos(id);
    }

    private void loadCrimeLab() {
        if (mCrimeLab == null) {
            mCrimeLab = CrimeLab.getCrimeLab();
        }
    }
}