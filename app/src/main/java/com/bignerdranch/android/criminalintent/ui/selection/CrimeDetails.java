package com.bignerdranch.android.criminalintent.ui.selection;

import android.view.MotionEvent;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

/**
 * Created by Tom Buczynski on 25.12.2022.
 */
public class CrimeDetails extends ItemDetailsLookup.ItemDetails<String> {
   private final UUID mId;
   private final int mPos;

   public CrimeDetails(int pos, UUID id) {
      mId = id;
      mPos = pos;
   }

   @Override
   public int getPosition() {
      return mPos;
   }

   @Override
   public boolean hasSelectionKey() {
      return true;
   }

   @Nullable
   @Override
   public String getSelectionKey() {
      return mId.toString();
   }

   @Override
   public boolean inSelectionHotspot(@NonNull MotionEvent e) {
      return true;
   }
}
