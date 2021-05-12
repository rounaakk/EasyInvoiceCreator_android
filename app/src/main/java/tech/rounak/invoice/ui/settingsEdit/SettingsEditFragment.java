package tech.rounak.invoice.ui.settingsEdit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import tech.rounak.invoice.databinding.SettingsEditFragmentBinding;
import tech.rounak.invoice.utils.EventObserver;

public class SettingsEditFragment extends Fragment {

    private SettingsEditViewModel mViewModel;
    SettingsEditFragmentBinding binding;
    NavController navController;

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



        mViewModel.toast.observe(getViewLifecycleOwner(), new EventObserver<String>(new EventObserver.EventUnhandledContent<String>() {
            @Override
            public void onEventUnhandledContent(String data) {
                Toast.makeText(SettingsEditFragment.this.getContext(), data, Toast.LENGTH_SHORT).show();
            }
        }));


        return binding.getRoot();
    }




}