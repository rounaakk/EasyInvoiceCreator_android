package tech.rounak.invoice.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import tech.rounak.invoice.adapters.ProfileAdapter;
import tech.rounak.invoice.databinding.SettingsFragmentBinding;
import tech.rounak.invoice.models.ProfileCardModel;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    SettingsFragmentBinding binding;
    NavController navController;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = SettingsFragmentBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);

        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding.setViewModel(mViewModel);
        mViewModel.fetchProfileDetails();
        navController = NavHostFragment.findNavController(this);
        setupRecyclerView();


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        return binding.getRoot();
    }

    public void setupRecyclerView(){

        mViewModel.getProfileCardModelsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ProfileCardModel>>() {
            @Override
            public void onChanged(List<ProfileCardModel> profileCardModels) {
                ProfileAdapter profileAdapter = new ProfileAdapter(profileCardModels,getContext());
                binding.setSettingsAdapter(profileAdapter);
                binding.settingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.settingsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            }
        });

    }



}