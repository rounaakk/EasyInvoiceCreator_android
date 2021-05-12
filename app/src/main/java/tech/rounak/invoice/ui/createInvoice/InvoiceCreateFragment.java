package tech.rounak.invoice.ui.createInvoice;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import tech.rounak.invoice.R;
import tech.rounak.invoice.adapters.ProductAdapter;
import tech.rounak.invoice.databinding.InvoiceCreateFragmentBinding;
import tech.rounak.invoice.models.ProductModel;
import tech.rounak.invoice.utils.EventObserver;

public class InvoiceCreateFragment extends Fragment {

    private InvoiceCreateViewModel mViewModel;
    InvoiceCreateFragmentBinding binding;
    NavController navController;
    BottomSheetBehavior bottomSheetBehavior;
    ArrayList<ProductModel> productModels;
    ProductAdapter productAdapter;
    int serialNumber;


    public static InvoiceCreateFragment newInstance() {
        return new InvoiceCreateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = InvoiceCreateFragmentBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);

        mViewModel = new ViewModelProvider(this).get(InvoiceCreateViewModel.class);
        binding.setViewModel(mViewModel);
        navController = NavHostFragment.findNavController(this);
        productModels = new ArrayList<>();
        serialNumber=1;
        setupBottomAppBar();

        binding.btnAddProduct.setOnClickListener(view -> showProductDialog());

        mViewModel.setInvoiceNumber();

        mViewModel.toast.observe(getViewLifecycleOwner(), new EventObserver<String>(new EventObserver.EventUnhandledContent<String>() {
            @Override
            public void onEventUnhandledContent(String data) {
                Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
            }
        }));

        mViewModel.navigateBack.observe(getViewLifecycleOwner(),new EventObserver<Boolean>(new EventObserver.EventUnhandledContent<Boolean>() {
            @Override
            public void onEventUnhandledContent(Boolean aBoolean) {

                navController.navigate(R.id.action_invoiceCreateFragment_pop);
            }
        }));



        binding.productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.transactionRecyclerView.setHasFixedSize(true);
        productAdapter = new ProductAdapter(productModels);
        binding.productRecyclerView.setAdapter(productAdapter);
        binding.productRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));




        return binding.getRoot();
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
                mViewModel.saveInvoice(productModels);
            }
        });



    }

    private void showProductDialog(){

            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.card_product);
            dialog.setCancelable(true);


            (dialog.findViewById(R.id.btn_save_product)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextInputLayout name = dialog.findViewById(R.id.et_product_name);
                    String productName = name.getEditText().getText().toString();

                    TextInputLayout hsn = dialog.findViewById(R.id.et_product_hsn);
                    String productHsn = hsn.getEditText().getText().toString();

                    TextInputLayout quantity = dialog.findViewById(R.id.et_product_quantity);
                    String productQuantity = quantity.getEditText().getText().toString();

                    TextInputLayout price = dialog.findViewById(R.id.et_product_price);
                    String productPrice = price.getEditText().getText().toString();

                    TextInputLayout sgst = dialog.findViewById(R.id.et_product_sgst);
                    String productSgst = sgst.getEditText().getText().toString();

                    TextInputLayout cgst = dialog.findViewById(R.id.et_product_cgst);
                    String productCgst = cgst.getEditText().getText().toString();

                    TextInputLayout igst = dialog.findViewById(R.id.et_product_igst);
                    String productIgst = igst.getEditText().getText().toString();



                    productModels.add(new ProductModel(String.valueOf(serialNumber),productName,productHsn,productQuantity,productPrice,productSgst,productCgst,productIgst));

                    serialNumber++;
                    Toast.makeText(getContext(), "Product Added", Toast.LENGTH_SHORT).show();
                    productAdapter.notifyDataSetChanged();
                    dialog.hide();

                }
            });


            dialog.show();
        }




}