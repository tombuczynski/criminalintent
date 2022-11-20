package com.bignerdranch.android.criminalintent.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.criminalintent.CrimeActivity;
import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.data.CrimeLab;

import java.text.DateFormat;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Tom Buczynski on 27.04.2022.
 */
public class CrimeRecViewAdapter extends RecyclerView.Adapter<CrimeRecViewAdapter.ViewHolderCrime> {

   public class ViewHolderCrime extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener {

      protected final TextView mTextViewTitle;
      protected final TextView mtextViewDateTime;
      protected final ImageView mImageViewSolved;

      public ViewHolderCrime(@NonNull View itemView) {
         super(itemView);

         mTextViewTitle = itemView.findViewById(R.id.textViewTitle);
         mtextViewDateTime = itemView.findViewById(R.id.textViewDateTime);
         mImageViewSolved = itemView.findViewById(R.id.imageViewSolved);

         itemView.setOnClickListener(this);
         //itemView.setOnLongClickListener(this);
      }

      public void bind(Crime crime) {
         mTextViewTitle.setText(crime.getTitle());
         DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT);
         mtextViewDateTime.setText(df.format(crime.getDate()));
         mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.INVISIBLE);
      }

      protected void showToast(CharSequence msg) {
         Toast.makeText(itemView.getContext(), msg, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onClick(View itemView) {
         //int pos = getAdapterPosition();

         //showToast("Wybrane: " + mTextViewTitle.getText());

         mViewModel.setSelectedItemPos(getAdapterPosition());
      }

      @Override
      public boolean onLongClick(View v) {
         showToast("Edycja: " + mTextViewTitle.getText());
         return true;
      }
   }

   public class ViewHolderCrimePolice extends ViewHolderCrime {

      public ViewHolderCrimePolice(@NonNull View itemView) {
         super(itemView);

         Button button = itemView.findViewById(R.id.buttonCallPolice);
         button.setOnClickListener(v -> showToast("Policja wezwana dla: " + mTextViewTitle.getText()));
      }
   }


   private final CrimeViewModel mViewModel;

   public CrimeRecViewAdapter(CrimeViewModel viewModel) {
      mViewModel = viewModel;
   }


   /**
    * Called when RecyclerView needs a new {@link ViewHolderCrime} of the given type to represent
    * an item.
    * <p>
    * This new ViewHolder should be constructed with a new View that can represent the items
    * of the given type. You can either create a new View manually or inflate it from an XML
    * layout file.
    * <p>
    * The new ViewHolder will be used to display items of the adapter using
    * onBindViewHolder(ViewHolder, int, List). Since it will be re-used to display
    * different items in the data set, it is a good idea to cache references to sub views of
    * the View to avoid unnecessary {@link View#findViewById(int)} calls.
    *
    * @param parent   The ViewGroup into which the new View will be added after it is bound to
    *                 an adapter position.
    * @param viewType The view type of the new View.
    * @return A new ViewHolder that holds a View of the given view type.
    * @see #getItemViewType(int)
    * @see #onBindViewHolder(ViewHolderCrime, int)
    */
   @NonNull
   @Override
   public ViewHolderCrime onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

      return viewType == R.layout.list_item_crime_police ? new ViewHolderCrimePolice(itemView) : new ViewHolderCrime(itemView);
   }

   /**
    * Called by RecyclerView to display the data at the specified position. This method should
    * update the contents of the {@link ViewHolderCrime#itemView} to reflect the item at the given
    * position.
    * <p>
    * Note that unlike {@link ListView}, RecyclerView will not call this method
    * again if the position of the item changes in the data set unless the item itself is
    * invalidated or the new position cannot be determined. For this reason, you should only
    * use the <code>position</code> parameter while acquiring the related data item inside
    * this method and should not keep a copy of it. If you need the position of an item later
    * on (e.g. in a click listener), use {@link ViewHolderCrime#getAdapterPosition()} which will
    * have the updated adapter position.
    * <p>
    * Override onBindViewHolder(ViewHolder, int, List) instead if Adapter can
    * handle efficient partial bind.
    *
    * @param holder   The ViewHolder which should be updated to represent the contents of the
    *                 item at the given position in the data set.
    * @param position The position of the item within the adapter's data set.
    */
   @Override
   public void onBindViewHolder(@NonNull ViewHolderCrime holder, int position) {
      holder.bind(mViewModel.getCrimeList().get(position));
   }

   /**
    * Return the view type of the item at <code>position</code> for the purposes
    * of view recycling.
    *
    * <p>The default implementation of this method returns 0, making the assumption of
    * a single view type for the adapter. Unlike ListView adapters, types need not
    * be contiguous. Consider using id resources to uniquely identify item view types.
    *
    * @param position position to query
    * @return integer value identifying the type of the view needed to represent the item at
    * <code>position</code>. Type codes need not be contiguous.
    */
   @Override
   public int getItemViewType(int position) {
      return mViewModel.getCrimeList().get(position).isRequiresPolice() ? R.layout.list_item_crime_police : R.layout.list_item_crime;
   }

   /**
    * Returns the total number of items in the data set held by the adapter.
    *
    * @return The total number of items in this adapter.
    */
   @Override
   public int getItemCount() {
      return mViewModel.getCrimeList().size();
   }
}
