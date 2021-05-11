package tech.rounak.invoice.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import tech.rounak.invoice.R;
import tech.rounak.invoice.adapters.InvoiceAdapter;
import tech.rounak.invoice.databinding.HistoryFragmentBinding;
import tech.rounak.invoice.models.InvoiceModel;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    HistoryFragmentBinding binding;
    NavController navController;
    BottomSheetBehavior bottomSheetBehavior;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = HistoryFragmentBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);

        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        binding.setViewModel(mViewModel);
        mViewModel.fetchInvoices();
        navController = NavHostFragment.findNavController(this);

        setupBottomAppBar();
        setupRecyclerView();


        return binding.getRoot();
    }


    public void setupRecyclerView(){




        mViewModel.getInvoiceModelsLiveData().observe(getViewLifecycleOwner(), new Observer<List<InvoiceModel>>() {
            @Override
            public void onChanged(List<InvoiceModel> invoiceModels) {
                InvoiceAdapter invoiceAdapter = new InvoiceAdapter(invoiceModels,getContext());
                binding.historyRecyclerview.setAdapter(invoiceAdapter);
                binding.historyRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
//                invoiceAdapter.setInvoiceModels(invoiceModels);
            }
        });

    }


    public void setupBottomAppBar(){

        bottomSheetBehavior = BottomSheetBehavior.from(binding.navView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //Managing BottomAppBarUi
        binding.bottomAppBar.setNavigationOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));



        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.drawer_create_invoice:
                    Toast.makeText(getContext(), "Create Invoice", Toast.LENGTH_SHORT).show();
                    break;
            }

            return false;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }


}