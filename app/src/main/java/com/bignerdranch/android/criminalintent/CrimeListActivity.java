package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;

import com.bignerdranch.android.criminalintent.ui.CrimeListFragment;
import com.bignerdranch.android.criminalintent.ui.CrimeViewModel;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by Tom Buczynski on 20.02.2022.
 */
public class CrimeListActivity extends FragmentContainerActivity {
   private CrimeViewModel mViewModel;
   private ActivityResultLauncher<Intent> mCrimeActivity;
   private boolean mCrimeActivityEnabled;

   @Override
   protected Class<? extends Fragment> getFragmentClass() {
      return CrimeListFragment.class;
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      mCrimeActivityEnabled = false;

      mViewModel = new ViewModelProvider(this).get(CrimeViewModel.class);

      registerForCrimeActivityResult();

      mViewModel.getSelectedItemPos().observe(this, this::onSelItemChanged);
   }

   private void startCrimeActivity(int itemPos) {
      if (itemPos >= 0) {
         Intent intent = new Intent(this, CrimeActivity.class);
         intent.putExtra(CrimeActivity.EXTRA_CRIME_ID, mViewModel.getCrime(itemPos).getId());

         mCrimeActivity.launch(intent);
      }
   }

   private void onSelItemChanged(Integer itemPos) {
      if (mCrimeActivityEnabled) {
         startCrimeActivity(itemPos);
      } else {
         if (mCrimeActivity != null) {
            mCrimeActivityEnabled = true;
         }
      }
   }

   private void registerForCrimeActivityResult() {
      mCrimeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
         if (result.getResultCode() == RESULT_OK) {
            mViewModel.setLastChangedItemPos(mViewModel.getSelectedItemPos().getValue());
         }
      });
   }
}
