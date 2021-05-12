package tech.rounak.invoice.ui.history;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.File;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
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
import tech.rounak.invoice.utils.InvoicePdfCreator;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    HistoryFragmentBinding binding;
    NavController navController;

    final int WRITE_PERMISSION_CODE = 102;
    final int READ_PERMISSION_CODE = 103;


    List<InvoiceModel> invoiceModelList;
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

        if(!isWriteStoragePermissionGranted()) {
            Toast.makeText(getContext(), "Please Grant Storage Write Access", Toast.LENGTH_SHORT).show();
        }

        if(!isReadStoragePermissionGranted()) {
            Toast.makeText(getContext(), "Please Grant Storage Read Access", Toast.LENGTH_SHORT).show();
        }

        setupBottomAppBar();
        setupRecyclerView();



        return binding.getRoot();
    }


    public void setupRecyclerView(){

        mViewModel.getInvoiceModelsLiveData().observe(getViewLifecycleOwner(), new Observer<List<InvoiceModel>>() {
            @Override
            public void onChanged(List<InvoiceModel> invoiceModels) {
                invoiceModelList=invoiceModels;
                InvoiceAdapter invoiceAdapter = new InvoiceAdapter(invoiceModels,getContext());
                binding.historyRecyclerview.setAdapter(invoiceAdapter);
                binding.historyRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

    }


    public void initDatePicker(){

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("Select Range");
        materialDateBuilder.setSelection(
                new  Pair(
                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
        ));

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.show(getParentFragmentManager(),"MATERIAL_DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

//                Get the selected DATE RANGE
                Pair<Long,Long> selectedDates = (Pair<Long, Long>) selection;
                final Pair<Date, Date> rangeDate = new Pair<>(new Date(selectedDates.first), new Date(selectedDates.second));
//              assigned variables
                Date startDate = rangeDate.first;
                Date endDate = rangeDate.second;

                mViewModel.fetchInvoices(startDate,endDate);
            }
        });

    }

    public void setupBottomAppBar(){

        binding.bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_date_pick:
                        initDatePicker();

                }

                return false;
            }
        });



        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(invoiceModelList.size()<1){
                    Toast.makeText(getContext(), "No Records Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fileName = "Report" + invoiceModelList.get(0).getTimestamp().toString()+"-"+invoiceModelList.get(invoiceModelList.size()-1).toString();
                InvoicePdfCreator.createPdf(getContext(),invoiceModelList, fileName);
                readPdf(fileName);
            }
        });



    }

    public boolean readPdf(String fileName){


        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/invoices/" + fileName +".pdf");
        Uri contentUri = FileProvider.getUriForFile(getContext(), requireActivity().getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW, contentUri);
        intent.setDataAndType(contentUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getContext().startActivity(intent);

        return true;
    }

    public  boolean isReadStoragePermissionGranted() {

        if (ContextCompat.checkSelfPermission( getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            return true;
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_PERMISSION_CODE);
            return false;
        }
    }
    public  boolean isWriteStoragePermissionGranted() {

        if (ContextCompat.checkSelfPermission( getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            return true;
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION_CODE);
            return false;
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION_CODE :
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                }  else {
                    navController.navigate(R.id.action_historyFragment_pop);
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                }

            case READ_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.

                    navController.navigate(R.id.action_historyFragment_pop);
                }
                return;

        }
    }


}