package com.bignerdranch.android.criminalintent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bignerdranch.android.criminalintent.data.CrimeLab;
import com.bignerdranch.android.criminalintent.ui.CrimeFragmentAdapter;
import com.bignerdranch.android.criminalintent.ui.CrimeViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crimeid";
    public static final String EXTRA_MODIFIED_CRIMES_SET = "com.bignerdranch.android.criminalintent.crimeset";
    public ImageButton mButtonArrowStart;
    public ImageButton mButtonArrowEnd;

    private CrimeViewModel mViewModel;
    private ViewPager2 mViewPagerCrimes;

    private class OnPageChangeCallback extends ViewPager2.OnPageChangeCallback {

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            enableButtons(position);
        }
    }

    private final OnPageChangeCallback pagerCallback = new OnPageChangeCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar =  Objects.requireNonNull(getSupportActionBar());
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        mViewPagerCrimes = findViewById(R.id.viewPagerCrimes);

        mViewModel = new ViewModelProvider(this).get(CrimeViewModel.class);
        mViewModel.setCrimeLab(CrimeLab.getCrimeLab(this));

        configureButtons();
        configureViewPager();

        mViewModel.getModifiedItems().observe(this, modifiedItems -> {
            if (modifiedItems.size() > 0) {

                Intent data = new Intent();
                data.putExtra(EXTRA_MODIFIED_CRIMES_SET, modifiedItems);
                setResult(RESULT_OK, data);

                if (modifiedItems.containsValue(CrimeViewModel.ItemModifyKind.REMOVED))
                    finish();
            }
        });

    }

    private void configureViewPager() {
        mViewPagerCrimes.setAdapter(new CrimeFragmentAdapter(this, mViewModel));

        Intent intent = getIntent();
        UUID id = (UUID) intent.getSerializableExtra(EXTRA_CRIME_ID);

        int itemPos = mViewModel.getCrimeItemPos(id);
        mViewPagerCrimes.setCurrentItem(itemPos, false);

        enableButtons(itemPos);
    }

    private void configureButtons() {
        mButtonArrowStart = findViewById(R.id.imageButtonStart);
        mButtonArrowEnd = findViewById(R.id.imageButtonEnd);

        mButtonArrowStart.setOnClickListener(v -> mViewPagerCrimes.setCurrentItem(0, false));
        mButtonArrowEnd.setOnClickListener(v -> mViewPagerCrimes.setCurrentItem(mViewModel.getCrimeList().size() - 1,false));
    }

    private void enableButtons(int itemPos) {
        mButtonArrowStart.setEnabled(itemPos > 0);
        mButtonArrowEnd.setEnabled(itemPos < mViewModel.getCrimeList().size() - 1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mViewPagerCrimes.registerOnPageChangeCallback(pagerCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();

        HashMap<UUID, CrimeViewModel.ItemModifyKind> modifiedItems = mViewModel.getModifiedItems().getValue();

        if (modifiedItems != null) {
            for (Map.Entry<UUID, CrimeViewModel.ItemModifyKind> item : modifiedItems.entrySet()) {

                if (item.getValue() == CrimeViewModel.ItemModifyKind.CHANGED) {
                    mViewModel.updateCrime(item.getKey());
                }
            }
        }

        mViewPagerCrimes.unregisterOnPageChangeCallback(pagerCallback);
    }
}