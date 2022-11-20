package com.bignerdranch.android.criminalintent.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.criminalintent.R;

import org.jetbrains.annotations.Contract;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeListFragment extends Fragment {

    public CrimeRecViewAdapter mRecViewAdapter;
    private CrimeViewModel mViewModel;
    private RecyclerView mRecyclerViewCrimes;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrimeListFragment.
     */
    @NonNull
    @Contract(" -> new")
    public static CrimeListFragment newInstance() {
        return new CrimeListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(CrimeViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crime_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerViewCrimes = view.findViewById(R.id.crimeListRecView);

        mRecyclerViewCrimes.setLayoutManager(new LinearLayoutManager(requireActivity()));

        mRecViewAdapter = new CrimeRecViewAdapter(mViewModel);
        mRecyclerViewCrimes.setAdapter(mRecViewAdapter);
    }

    public void notifyItemChanged(int itemPos) {
        mRecViewAdapter.notifyItemChanged(itemPos);
    }
}