package com.bignerdranch.android.criminalintent.ui;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.data.CrimeLab;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrimeViewModel extends ViewModel {

    private CrimeLab mCrimeLab;

    private MutableLiveData<Integer> mSelectedItemPos;


    public enum ItemModifyKind {CHANGED, INSERTED, REMOVED;};
    private MutableLiveData<HashMap<UUID, ItemModifyKind>> mModifiedItems;

    private int mActivatedItemPos;

    public int getActivatedItemPos() {
        int id = mActivatedItemPos;
        mActivatedItemPos = -1;

        return id;
    }

    public void setCrimeLab(CrimeLab crimeLab) {
        mCrimeLab = crimeLab;
    }

    public void setActivatedItemPos(int itemPos) {
        mActivatedItemPos = itemPos;
    }

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

    public LiveData<HashMap<UUID, ItemModifyKind>> getModifiedItems() {
        if (mModifiedItems == null) {
            mModifiedItems = new MutableLiveData<>(new HashMap<>());
        }
        return mModifiedItems;
    }

    public void setModifiedItems(HashMap<UUID, ItemModifyKind> modifiedItemsId) {
        if (mModifiedItems == null) {
            mModifiedItems = new MutableLiveData<>(modifiedItemsId);
        } else {
            mModifiedItems.setValue(modifiedItemsId);
        }
    }

    public void addModifiedItem(UUID modifiedItemId, ItemModifyKind kind) {
        HashMap<UUID, ItemModifyKind> modifiedItems = getModifiedItems().getValue();
        if (modifiedItems !=null) {
            modifiedItems.put(modifiedItemId, kind);
            setModifiedItems(modifiedItems);
        }
    }

    public List<Crime> getCrimeList() {
        return mCrimeLab.getCrimeList();
    }

    public Crime getCrime(UUID id) {
        return  mCrimeLab.getCrime(id);
    }

    public Crime getCrime(int itemPos) {
        return  mCrimeLab.getCrimeList().get(itemPos);
    }

    public void removeCrime(UUID id) {
        mCrimeLab.removeCrime(id);
    }

    public void removeCrime(int itemPos) {
        UUID id = getCrime(itemPos).getId();

        mCrimeLab.removeCrime(id);
    }

    public void updateCrime(UUID id) {
        Crime crime = mCrimeLab.getCrime(id);

        mCrimeLab.update(crime);
    }

    public void updateCrime(int itemPos) {
        Crime crime = getCrime(itemPos);

        mCrimeLab.update(crime);
    }

    public int getCrimeItemPos(UUID id) {
        return  mCrimeLab.getCrimeItemPos(id);
    }

    public int newCrime(String title) {
        Crime crime = new Crime(title);
        return mCrimeLab.insert(crime);
    }

}