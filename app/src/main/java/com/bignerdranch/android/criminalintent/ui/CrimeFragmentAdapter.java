package com.bignerdranch.android.criminalintent.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Created by Tom Buczynski on 21.11.2022.
 */
public class CrimeFragmentAdapter extends FragmentStateAdapter {
   private final CrimeViewModel mViewModel;

   public CrimeFragmentAdapter(@NonNull FragmentActivity fragmentActivity, CrimeViewModel viewModel) {
      super(fragmentActivity);

      mViewModel = viewModel;
   }

   @Override
   @NonNull
   public Fragment createFragment(int position) {
      return CrimeFragment.newInstance(mViewModel.getCrimeList().get(position).getId());
   }

   @Override
   public int getItemCount() {
      return mViewModel.getCrimeList().size();
   }
}
