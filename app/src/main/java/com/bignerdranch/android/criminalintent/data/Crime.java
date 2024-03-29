package com.bignerdranch.android.criminalintent.data;

/*
 * Created by Tom Buczynski on 14.01.2022.
 */

import java.util.Date;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by Tom Buczynski on 13.01.2022.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private boolean mRequiresPolice;

    private final MutableLiveData<Boolean> mIsModified;

    public Crime(String title, boolean solved, boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
        mId = UUID.randomUUID();
        mDate = new Date();

        mTitle = title;
        mSolved = solved;
        mSuspect = null;

        mIsModified = new MutableLiveData<>(false);
    }

    public Crime(UUID id, String title, boolean solved, Date date, boolean requiresPolice, String suspect) {
        this(title, solved, requiresPolice);

        mDate = date;
        mId = id;
        mSuspect = suspect;
    }

    public Crime(String title) {
        this(title, false , false);
    }

    public UUID getId() {
        return mId;
    }

    public String getPhotoFileName() {
        return getId().toString() + ".jpg";
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
        mIsModified.setValue(true);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        mIsModified.setValue(true);
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
        mIsModified.setValue(true);
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
        mIsModified.setValue(true);
    }

    public boolean isRequiresPolice() {
        return mRequiresPolice;
    }

    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
        mIsModified.setValue(true);
    }

    public LiveData<Boolean> isModified() {
        return mIsModified;
    }

    public void clearModified() {
        mIsModified.setValue(false);
    }

}
