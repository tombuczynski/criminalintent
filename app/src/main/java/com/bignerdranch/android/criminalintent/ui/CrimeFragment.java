package com.bignerdranch.android.criminalintent.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeBinding;

import org.jetbrains.annotations.Contract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";
    private CrimeViewModel mViewModel;
    private FragmentCrimeBinding mBinding;

    @NonNull
    @Contract(" -> new")
    public static CrimeFragment newInstance() {
        return new CrimeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(CrimeViewModel.class);

        mBinding = FragmentCrimeBinding.inflate(inflater, container, false);
        mBinding.setCrime(mViewModel.getCrimes().get(0));

        //mBinding.setLifecycleOwner(this);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonDateTime.setEnabled(false);

        handleViewEvents();
    }

    private void handleViewEvents() {
        mBinding.buttonDateTime.setOnClickListener(v -> {
//            Log.d(TAG, "title = " + mCrime.getTitle());
//            Log.d(TAG, "solved = " + mCrime.isSolved());
//
//            mCrime = new Crime("Klipa", true);
//            mBinding.setCrime(mCrime);

        });
    }

}