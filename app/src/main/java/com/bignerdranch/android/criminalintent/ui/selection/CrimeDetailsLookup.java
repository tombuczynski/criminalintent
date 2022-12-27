package com.bignerdranch.android.criminalintent.ui.selection;

import android.view.MotionEvent;
import android.view.View;

import com.bignerdranch.android.criminalintent.ui.CrimeRecyclerViewAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Tom Buczynski on 25.12.2022.
 */
public class CrimeDetailsLookup extends ItemDetailsLookup<String> {

   private final RecyclerView mRecyclerView;

   public CrimeDetailsLookup(RecyclerView recyclerView) {
      mRecyclerView = recyclerView;
   }

   @Nullable
   @Override
   public ItemDetails<String> getItemDetails(@NonNull MotionEvent e) {

      View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
      if (view != null) {
         RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(view);
         if (viewHolder instanceof CrimeRecyclerViewAdapter.ViewHolderCrime) {
            return ((CrimeRecyclerViewAdapter.ViewHolderCrime) viewHolder).getItemDetails();
         }
      }

      return null;
   }
}
