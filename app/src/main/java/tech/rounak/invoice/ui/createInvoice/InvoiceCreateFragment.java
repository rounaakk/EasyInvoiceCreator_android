package tech.rounak.invoice.ui.createInvoice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import tech.rounak.invoice.R;

public class InvoiceCreateFragment extends Fragment {

    private InvoiceCreateViewModel mViewModel;

    public static InvoiceCreateFragment newInstance() {
        return new InvoiceCreateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.invoice_create_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InvoiceCreateViewModel.class);
        // TODO: Use the ViewModel
    }

}