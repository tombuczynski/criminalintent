package com.bignerdranch.android.criminalintent.data;

/*
 * Created by Tom Buczynski on 14.01.2022.
 */

import java.util.Date;
import java.util.UUID;

/**
 * Created by Tom Buczynski on 13.01.2022.
 */
public class Crime {
    private final UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiresPolice;

    public Crime(String title, boolean solved, boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
        mId = UUID.randomUUID();
        mDate = new Date();

        mTitle = title;
        mSolved = solved;
    }

    public Crime(String title, boolean solved, boolean requiresPolice, Date date) {
        this(title, solved, requiresPolice);

        mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }
    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mSolved;
    }
    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isRequiresPolice() {
        return mRequiresPolice;
    }
    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
    }

}
