package tech.rounak.invoice.ui.settingsEdit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import tech.rounak.invoice.R;
import tech.rounak.invoice.databinding.SettingsEditFragmentBinding;
import tech.rounak.invoice.utils.EventObserver;

public class SettingsEditFragment extends Fragment {

    private SettingsEditViewModel mViewModel;
    SettingsEditFragmentBinding binding;
    NavController navController;
    BottomSheetBehavior bottomSheetBehavior;

    public static SettingsEditFragment newInstance() {
        return new SettingsEditFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = SettingsEditFragmentBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);

        mViewModel = new ViewModelProvider(this).get(SettingsEditViewModel.class);
        binding.setViewModel(mViewModel);
        mViewModel.fetchUserDetails();
        navController = NavHostFragment.findNavController(this);
        setupBottomAppBar();


        mViewModel.toast.observe(getViewLifecycleOwner(), new EventObserver<String>(new EventObserver.EventUnhandledContent<String>() {
            @Override
            public void onEventUnhandledContent(String data) {
                Toast.makeText(SettingsEditFragment.this.getContext(), data, Toast.LENGTH_SHORT).show();
            }
        }));


        return binding.getRoot();
    }

    public void setupBottomAppBar(){

        bottomSheetBehavior = BottomSheetBehavior.from(binding.navView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //Managing BottomAppBarUi
//        binding.scrim.setOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
        binding.bottomAppBar.setNavigationOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.drawer_create_invoice:
                    Toast.makeText(getContext(), "Create Invoice", Toast.LENGTH_SHORT).show();
                    break;
            }

            return false;
        });



    }


}