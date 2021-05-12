package tech.rounak.invoice.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import tech.rounak.invoice.R;
import tech.rounak.invoice.databinding.DashboardFragmentBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;
    DashboardFragmentBinding binding;
    NavController navController;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    BottomAppBar bottomAppBar;
    BottomSheetBehavior bottomSheetBehavior;
//    BarChart chart;
    LineChart chart;
    private static final String[] DAYS = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    int RC_SIGN_IN = 100;
    int RESULT_OK = 101;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DashboardFragmentBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        navController = NavHostFragment.findNavController(this);
        ViewModelStoreOwner storeOwner = navController.getViewModelStoreOwner(R.id.navigation);
        mViewModel = new ViewModelProvider(storeOwner).get(DashboardViewModel.class);
        binding.setViewModel(mViewModel);
        setupBottomAppBar();
        mViewModel.fetchInvoices();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateDashboard();
        }else{
            signIn();
        }



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
                    navController.navigate(R.id.action_dashboardFragment_to_invoiceCreateFragment);
                    break;
            }

            return false;
        });

        binding.bottomAppBar.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_settings:
                    navController.navigate(R.id.action_dashboardFragment_to_settingsFragment);
                    break;
                case R.id.menu_list:
                    navController.navigate(R.id.action_dashboardFragment_to_historyFragment);
                    break;

            }
            return false;
        });

        binding.fab.setOnClickListener(view -> navController.navigate(R.id.action_dashboardFragment_to_invoiceCreateFragment));

    }

    public void updateDashboard(){

        chart =binding.barChart;

//        BarData data = new BarData(getDataSet());
        LineData data = new LineData(getDataSet());

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color = typedValue.data;

        data.setValueTextColor(color);
        data.setValueTextSize(9f);

//        data.setValueTextSize(12f);
        configureChartAppearance();
//        configureLineChart();
        chart.setData(data);
        chart.invalidate();

    }



    private LineDataSet getDataSet(){

        List<Entry> dataset = new ArrayList<>();

        dataset.add(new Entry(0, 0f));
        dataset.add(new Entry(1, 26586f));
        dataset.add(new Entry(2,12365f));
        dataset.add(new Entry(3,43568f ));
        dataset.add(new Entry(4, 36594f));
        dataset.add(new Entry(5, 57000));
        dataset.add(new Entry(6, 75465f));

        LineDataSet set = new LineDataSet(dataset, "Test set");

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

//        MaterialColors.getColor(getContext(), R.attr.colorAccent,getContext().getResources().getColor());
        set.setColor(color);
        set.setCircleColor(color);
        set.setLineWidth(3f);
        set.setFillAlpha(10);
        set.setCircleHoleRadius(3f);
        set.setCircleRadius(6f);

        set.setDrawCircles(true);

        return set;


    }



    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);
        chart.animateXY(2000, 2000);
        chart.getDescription().setEnabled(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });

        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.disableGridDashedLine();

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);

    }


    public void signIn(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_invoice)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateDashboard();
        }else{
            signIn();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = mAuth.getCurrentUser();
                updateDashboard();
                // ...
            } else {
                Toast.makeText(getContext(), "Sign In Failed, Please Sign In again.", Toast.LENGTH_SHORT).show();
                signIn();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

}