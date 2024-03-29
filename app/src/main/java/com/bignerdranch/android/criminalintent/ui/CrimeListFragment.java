package com.bignerdranch.android.criminalintent.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.bignerdranch.android.criminalintent.data.Crime;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeListFragment extends Fragment {

    private static final String ARG_USE_SELECTION = "UseSelection";

    public CrimeRecyclerViewAdapter mRecViewAdapter;
    private int mContextMenuItemPos;
    private CrimeViewModel mViewModel;
    private RecyclerView mRecyclerViewCrimes;
    private Button mButtonNewCrime;

    private boolean mUseSelection;
    private UUID mLastSelectedItemId;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CrimeListFragment.
     */
    @NonNull
    public static CrimeListFragment newInstance(boolean useSelection) {
        CrimeListFragment f = new CrimeListFragment();
        f.setArguments(prepareArgs(useSelection));
        return f;
    }

    @NonNull
    public static Bundle prepareArgs(boolean useSelection) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_USE_SELECTION, useSelection);

        return bundle;
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

        mUseSelection = requireArguments().getBoolean(ARG_USE_SELECTION);

        mRecyclerViewCrimes = view.findViewById(R.id.recyclerViewCrimeList);

        mRecyclerViewCrimes.setLayoutManager(new LinearLayoutManager(requireActivity()));

        mRecViewAdapter = new CrimeRecyclerViewAdapter(mViewModel, mUseSelection);
        mRecyclerViewCrimes.setAdapter(mRecViewAdapter);

//        SelectionTracker<String> selectionTracker = new SelectionTracker.Builder<String>(
//                "crimeSelection",
//                mRecyclerViewCrimes,
//                new CrimeIdKeyProvider(mViewModel),
//                new CrimeDetailsLookup(mRecyclerViewCrimes),
//                StorageStrategy.createStringStorage())
//                .withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
//                //.withOnItemActivatedListener((item, e) -> false)
//                //.withOnContextClickListener(e -> false)
//                .build();
//
//        mRecViewAdapter.setSelectionTracker(selectionTracker);
//
//        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<String>() {
//            @Override
//            public void onItemStateChanged(@NonNull String key, boolean selected) {
//                super.onItemStateChanged(key, selected);
//            }
//
//
//            @Override
//            public void onSelectionChanged() {
//                super.onSelectionChanged();
//            }
//
//        });

        registerForContextMenu(mRecyclerViewCrimes);

        mButtonNewCrime = view.findViewById(R.id.buttonNewCrime);
        updateButtonNewCrime();
        mButtonNewCrime.setOnClickListener(v -> newCrime());

        if (mUseSelection)
            trackSelection();

        handleItemSwipe();
    }

    private void handleItemSwipe() {
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int itemPos = viewHolder.getAdapterPosition();

                removeItem(itemPos);
            }
        });

        touchHelper.attachToRecyclerView(mRecyclerViewCrimes);
    }

    private void removeItem(int itemPos) {
        mViewModel.addModifiedItem(mViewModel.getCrime(itemPos).getId(), CrimeViewModel.ItemModifyKind.REMOVED);
    }

    private void trackSelection() {
        mLastSelectedItemId = null;
        mViewModel.getSelectedItemPos().observe(getViewLifecycleOwner(), pos -> {
            if (mLastSelectedItemId != null) {
                int lastPos = mViewModel.getCrimeItemPos(mLastSelectedItemId);

                if (lastPos >= 0) {
                    mRecViewAdapter.notifyItemChanged(lastPos);
                }
            }

            if (pos >= 0) {
                mRecViewAdapter.notifyItemChanged(pos);
                Crime crime = mViewModel.getCrime(pos);
                mLastSelectedItemId = crime.getId();
            } else {
                mLastSelectedItemId = null;
            }
        });
    }

    private void updateButtonNewCrime() {
        mButtonNewCrime.setVisibility(mViewModel.getCrimesCount() == 0 ? View.VISIBLE : View.GONE);
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

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_crime_list, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();

        if (menuItemId == R.id.menu_new_crime) {
            newCrime();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newCrime() {
        //int newCrimeNumber = mViewModel.getCrimeList().size() + 1;

        int itemPos = mViewModel.newCrime(getResources().getString(R.string.new_crime));
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
            removeItem(mContextMenuItemPos);

            return true;
        }

        return super.onContextItemSelected(item);
    }
}