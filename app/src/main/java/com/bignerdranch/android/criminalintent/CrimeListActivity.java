package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;

import com.bignerdranch.android.criminalintent.ui.CrimeListFragment;
import com.bignerdranch.android.criminalintent.ui.CrimeViewModel;

import java.util.HashSet;
import java.util.UUID;

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
    private boolean mCrimeItemPreselection;

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return CrimeListFragment.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrimeItemPreselection = true;

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
        if (mCrimeItemPreselection) {
            if (itemPos >= 0) {

            }
            mCrimeItemPreselection = false;
        } else {
            startCrimeActivity(itemPos);
        }
    }

    private void registerForCrimeActivityResult() {
        mCrimeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (result.getResultCode() == RESULT_OK && data != null) {
                @SuppressWarnings("unchecked")
                HashSet<UUID> modifiedItems = (HashSet<UUID>) data.getSerializableExtra(CrimeActivity.EXTRA_MODIFIED_CRIMES_SET);

                CrimeListFragment f = (CrimeListFragment) getFragment();
                for (UUID itemId : modifiedItems) {
                    int itemPos = mViewModel.getCrimeItemPos(itemId);
                    if (itemPos >= 0) {
                        f.notifyItemChanged(itemPos);
                        mViewModel.getCrimeList().get(itemPos).clearModified();
                    }
                }
                //mViewModel.setLastChangedItemPos(mViewModel.getSelectedItemPos().getValue());
            }
        });
    }
}
