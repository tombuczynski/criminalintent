package com.bignerdranch.android.criminalintent.ui;

import android.content.Context;
import android.util.Log;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.data.CrimeLab;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrimeViewModel extends ViewModel {
    private static final String TAG = "CrimeViewModel";
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

    public int getCrimesCount() {
        return mCrimeLab.getCrimeList().size();
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

    public void removeCrimeAndPhoto(Context context, int itemPos) {
        Crime crime = getCrime(itemPos);
        UUID id = crime.getId();

        removeCrime(id);

        boolean succ = getPhotoFile(context, crime).delete();
        Log.d(TAG, "removeCrimeAndPhoto: photo for crime ID " + id.toString() + " " + (succ ? "deleted" : "not exists"));
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

    @NonNull
    public File getPhotoFile(@NonNull Context context, @NonNull Crime crime) {
        File dir = new File(context.getFilesDir(), "photos");
        //File dir = context.getFilesDir();

        if (! dir.exists()) {
            boolean created = dir.mkdir();
        }

        return new File(dir, crime.getPhotoFileName());
    }
}