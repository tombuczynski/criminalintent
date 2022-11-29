package com.bignerdranch.android.criminalintent.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bignerdranch.android.criminalintent.R;

import org.jetbrains.annotations.Contract;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeListFragment extends Fragment {

    public CrimeRecyclerViewAdapter mRecViewAdapter;
    private int mContextMenuItemPos;
    private CrimeViewModel mViewModel;
    private RecyclerView mRecyclerViewCrimes;
    private Button mButtonNewCrime;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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

        mRecyclerViewCrimes = view.findViewById(R.id.recyclerViewCrimeList);

        mRecyclerViewCrimes.setLayoutManager(new LinearLayoutManager(requireActivity()));

        mRecViewAdapter = new CrimeRecyclerViewAdapter(mViewModel);
        mRecyclerViewCrimes.setAdapter(mRecViewAdapter);

        registerForContextMenu(mRecyclerViewCrimes);

        mButtonNewCrime = view.findViewById(R.id.buttonNewCrime);
        updateButtonNewCrime();
        mButtonNewCrime.setOnClickListener(v -> newCrime());
    }

    private void updateButtonNewCrime() {
        mButtonNewCrime.setVisibility(mViewModel.getCrimeList().size() == 0 ? View.VISIBLE : View.GONE);
    }

    public void notifyItemChanged(int itemPos) {
        mRecViewAdapter.notifyItemChanged(itemPos);
        //mRecyclerViewCrimes.scrollToPosition(itemPos);
    }

    public void notifyItemInserted(int itemPos) {
        mRecViewAdapter.notifyItemInserted(itemPos);
        mRecyclerViewCrimes.scrollToPosition(itemPos);
        updateButtonNewCrime();
    }

    public void notifyItemRemoved(int itemPos) {
        mRecViewAdapter.notifyItemRemoved(itemPos);
        updateButtonNewCrime();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();

        if (menuItemId == R.id.menu_new_crime) {
            newCrime();

            //mRecViewAdapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newCrime() {
        int newCrimeNumber = mViewModel.getCrimeList().size() + 1;

        int itemPos = mViewModel.newCrime(getResources().getString(R.string.new_crime_number, newCrimeNumber));
        mViewModel.setActivatedItemPos(itemPos);
        mViewModel.addModifiedItem(mViewModel.getCrime(itemPos).getId(), CrimeViewModel.ItemModifyKind.INSERTED);
        mViewModel.setSelectedItemPos(itemPos);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        mContextMenuItemPos = mViewModel.getActivatedItemPos();

        requireActivity().getMenuInflater().inflate(R.menu.contextmenu_crime_list, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();

        if (menuItemId == R.id.menu_remove_crime) {
            mViewModel.addModifiedItem(mViewModel.getCrime(mContextMenuItemPos).getId(), CrimeViewModel.ItemModifyKind.REMOVED);

            return true;
        }

        return super.onContextItemSelected(item);
    }
}