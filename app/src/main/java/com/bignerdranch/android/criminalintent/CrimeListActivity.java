package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.criminalintent.thutils.ActivityPrefs;
import com.bignerdranch.android.criminalintent.ui.CrimeListFragment;
import com.bignerdranch.android.criminalintent.ui.CrimeViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by Tom Buczynski on 20.02.2022.
 */
public class CrimeListActivity extends FragmentContainerActivity {
    private CrimeViewModel mViewModel;
    private ActivityResultLauncher<Intent> mCrimeActivity;

    private static final String KEY_PREF_SHOW_QUANTITY = "showQuantity";

    private boolean mShowQuantity;

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return CrimeListFragment.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(CrimeViewModel.class);

        registerForCrimeActivityResult();

        mViewModel.getSelectedItemPos().observe(this, this::onSelItemChanged);
        mViewModel.getModifiedItems().observe(this, this::handleModifiedItems);

        mShowQuantity = ActivityPrefs.getBoolean(this,KEY_PREF_SHOW_QUANTITY, false);
        updateCrimeQuantity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crime_list_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mi = menu.findItem(R.id.menu_show_hide_quantity);

        if (mi != null) {
            mi.setTitle(mShowQuantity ? R.string.hide_quantity : R.string.show_quantity);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();

        if (menuItemId == R.id.menu_show_hide_quantity) {
            toggleShowQuantity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSelItemChanged(Integer itemPos) {
        if (itemPos >= 0)
        {


            if (itemPos == mViewModel.getActivatedItemPos()) {
                startCrimeActivity(itemPos);
            }
        }
    }

    private void toggleShowQuantity() {
        mShowQuantity = !mShowQuantity;
        ActivityPrefs.putBoolean(this, KEY_PREF_SHOW_QUANTITY, mShowQuantity);
        updateCrimeQuantity();
        invalidateOptionsMenu();
    }

    private void startCrimeActivity(int itemPos) {
        if (itemPos >= 0) {
//            Intent intent = new Intent(this, CrimeActivity.class);
//            intent.putExtra(CrimeActivity.EXTRA_CRIME_ID, mViewModel.getCrime(itemPos).getId());
            Intent intent = new Intent(this, CrimePagerActivity.class);
            intent.putExtra(CrimePagerActivity.EXTRA_CRIME_ID, mViewModel.getCrime(itemPos).getId());

            mCrimeActivity.launch(intent);
        }
    }

    private void registerForCrimeActivityResult() {
        mCrimeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (result.getResultCode() == RESULT_OK && data != null) {
                @SuppressWarnings("unchecked")
                HashMap<UUID, CrimeViewModel.ItemModifyKind> modifiedItems = (HashMap<UUID, CrimeViewModel.ItemModifyKind>) data.getSerializableExtra(CrimeActivity.EXTRA_MODIFIED_CRIMES_SET);

                handleModifiedItems(modifiedItems);
                //mViewModel.setLastChangedItemPos(mViewModel.getSelectedItemPos().getValue());
            }
        });
    }

    private void handleModifiedItems(@NonNull HashMap<UUID, CrimeViewModel.ItemModifyKind> modifiedItems) {
        CrimeListFragment f = (CrimeListFragment) getFragment();
        for (Map.Entry<UUID, CrimeViewModel.ItemModifyKind> item: modifiedItems.entrySet()) {
            int itemPos = mViewModel.getCrimeItemPos(item.getKey());
            if (itemPos >= 0) {
                switch (item.getValue()) {
                    case CHANGED:
                        f.notifyItemChanged(itemPos);
                        mViewModel.getCrimeList().get(itemPos).clearModified();
                        break;
                    case INSERTED:
                        f.notifyItemInserted(itemPos);
                        updateCrimeQuantity();
                        break;
                    case REMOVED:
                        mViewModel.removeCrime(itemPos);
                        f.notifyItemRemoved(itemPos);
                        updateCrimeQuantity();
                        break;
                }
            }
        }

        modifiedItems.clear();
    }

    private void updateCrimeQuantity() {
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            return;
        }

        if(!mShowQuantity) {
            bar.setSubtitle(null);
        } else {
            int quantity = mViewModel.getCrimeList().size();
            bar.setSubtitle(getResources().getQuantityString(R.plurals.crime_quantity, quantity,quantity));
        }
    }
}
