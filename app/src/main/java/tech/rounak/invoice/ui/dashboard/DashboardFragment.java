package tech.rounak.invoice.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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

    int RC_SIGN_IN = 100;
    int RESULT_OK = 101;
    final int WRITE_PERMISSION_CODE = 102;
    final int READ_PERMISSION_CODE = 103;

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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION_CODE :
            case READ_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                }
                return;

        }
    }



    private File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), albumName);
        if (!file.mkdirs()) {
            Log.e("TAG", "Directory not created");
        }
        return file;
    }

    public void textPaintConfig(Paint textPaint, int size, int alignInt,  int fontInt, int typeInt){

        Paint.Align align;
        Typeface font;

        switch (alignInt){
            case 1:
                align = Paint.Align.CENTER;
            case 2:
                align= Paint.Align.RIGHT;
            default:
                align = Paint.Align.LEFT;

        }

        switch (fontInt){
            case 1:
                font = Typeface.SERIF;
            case 2:
                font= Typeface.SANS_SERIF;
            case 3:
                font= Typeface.MONOSPACE;
            default:
                font = Typeface.DEFAULT;

        }

        textPaint.setTextAlign(align);
        textPaint.setTypeface(Typeface.create(font, typeInt));
        textPaint.setTextSize(size);
    }


    public void linePaintConfig(Paint shapePaint, int strokeWidth){
        shapePaint.setStyle(Paint.Style.STROKE);
        shapePaint.setStrokeWidth(2);
    }

//    public boolean createPdf(UserModel userModel, ArrayList<InvoiceModel> invoiceModels){

    public boolean createPdf(){

        if(!isWriteStoragePermissionGranted()) {
            Toast.makeText(getContext(), "Please Grant Storage Write Access", Toast.LENGTH_SHORT).show();
            return false;
        }

//        private String customerName;
//        private String customerGstin;
//        private String mobile;
//        private String billingAddress;
//        private String shippingAddress;
//        private String total;
//        private String totalInWords;
//        private Timestamp timestamp;
//        private String invoiceNumber;
//        private String currency;

//        int PAGES = invoiceModels.size();
        int PAGES =1;


        int PAGE_WIDTH = 600;
        int PAGE_HEIGHT = 800;
        int MARGIN = 20;

        final int LEFT = 0;
        final int CENTRE = 1;
        final int RIGHT = 2;

        final int NORMAL = 0;
        final int BOLD = 1;
        final int ITALIC = 2;
        final int BOLD_ITALIC = 3;

        final int DEFAULT = 0;
        final int SERIF = 1;
        final int SANS_SERIF = 2;
        final int MONOSPACE = 3;

        final int sizeH1 = 30;
        final int sizeH2 = 24;
        final int sizeM = 9;
        final int sizeS = 7;
        final int sizeXS = 6;


        PdfDocument pdfDocument = new PdfDocument();
        Paint textPaint = new Paint();
        Paint shapePaint = new Paint();

        for(int i = 1; i<=PAGES; i++){
//            InvoiceModel invoiceModel= invoiceModels.get(i);

            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH,PAGE_HEIGHT,i).create();
            PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
            Canvas canvas = page.getCanvas();

            //Header
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTypeface(  Typeface.create(ResourcesCompat.getFont(getContext(), R.font.raleway_bold), Typeface.BOLD));
            textPaint.setTextSize(sizeH1);
            canvas.drawText("New Minerva Sports",(float)MARGIN , 50f, textPaint);


            textPaint.setTextAlign(Paint.Align.RIGHT);
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            textPaint.setTextSize(sizeM);

            canvas.drawText("invoiceModel.getInvoiceNumber()",PAGE_WIDTH-MARGIN , 35, textPaint);
            canvas.drawText("invoiceModel.getTimestamp().toDate().toString()",PAGE_WIDTH-MARGIN , 50, textPaint);

            shapePaint.setStyle(Paint.Style.STROKE);
            shapePaint.setStrokeWidth(1);
            canvas.drawLine(MARGIN,70,PAGE_WIDTH-MARGIN,70,shapePaint);



            //Bank Details
            canvas.drawText("sddsdsd",PAGE_WIDTH-MARGIN , 100, textPaint);
            canvas.drawText("AAAAAAA",PAGE_WIDTH-MARGIN , 115, textPaint);
            canvas.drawText("SDDSDS",PAGE_WIDTH-MARGIN , 130, textPaint);
            canvas.drawText("AsdasDasda",PAGE_WIDTH-MARGIN , 145, textPaint);
            canvas.drawText("dsadsdsdsfsdfsd",PAGE_WIDTH-MARGIN , 160, textPaint);


            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Bank Name",PAGE_WIDTH-MARGIN , 85, textPaint);

            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("Bank Name",MARGIN , 85, textPaint);
            canvas.drawText("sddsdsd",MARGIN , 100, textPaint);
            canvas.drawText("AAAAAAA",MARGIN , 115, textPaint);
            canvas.drawText("SDDSDS",MARGIN , 130, textPaint);
            canvas.drawText("AsdasDasda",MARGIN , 145, textPaint);
            canvas.drawText("dsadsdsdsfsdfsd",MARGIN , 160, textPaint);


            canvas.drawText("dsadsdsdsfsdfsd",MARGIN , 205, textPaint);
            canvas.drawText("dsadsdsdsfsdfsd",MARGIN , 220, textPaint);
            canvas.drawText("dsadsdsdsfsdfsd",MARGIN , 235, textPaint);
            canvas.drawText("dsadsdsdsfsdfsd",MARGIN , 250, textPaint);


            canvas.drawLine(MARGIN,175,220,175,shapePaint);
            canvas.drawLine(380,175,PAGE_WIDTH-MARGIN,175,shapePaint);

            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
            textPaint.setTextSize(sizeH2);
            canvas.drawText("INVOICE",(float)PAGE_WIDTH/2, 187, textPaint);



            canvas.drawLine(45,270,45,715,shapePaint);
            canvas.drawLine(180,270,180,715,shapePaint);
            canvas.drawLine(240,270,240,715,shapePaint);
            canvas.drawLine(280,270,280,715,shapePaint);
            canvas.drawLine(320,270,320,715,shapePaint);
            canvas.drawLine(370,270,370,715,shapePaint);
            canvas.drawLine(420,270,420,750,shapePaint);
            canvas.drawLine(470,270,470,715,shapePaint);
            canvas.drawLine(520,270,520,750,shapePaint);

            shapePaint.setStrokeWidth(0.75f);
            canvas.drawLine(392,290,392,715,shapePaint);
            canvas.drawLine(442,290,442,715,shapePaint);
            canvas.drawLine(492,290,492,715,shapePaint);

            canvas.drawLine(280,290,PAGE_WIDTH-MARGIN,290,shapePaint);

            shapePaint.setStrokeWidth(2);
            canvas.drawLine(MARGIN,270,PAGE_WIDTH-MARGIN,270,shapePaint);
            canvas.drawLine(MARGIN,300,PAGE_WIDTH-MARGIN,300,shapePaint);
            canvas.drawLine(MARGIN,715,PAGE_WIDTH-MARGIN,715,shapePaint);
            canvas.drawLine(MARGIN,750,PAGE_WIDTH-MARGIN,750,shapePaint);




            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
            textPaint.setTextSize(sizeM);

            canvas.drawText("S/N",32.5f , 288, textPaint);
            canvas.drawText("Description of Item",112.5f , 288, textPaint);
            canvas.drawText("HSN",210f , 288, textPaint);
            canvas.drawText("Qty.",260f , 288, textPaint);

            canvas.drawText("Rate",300f , 283, textPaint);
            canvas.drawText("Subtotal",345f , 283, textPaint);
            canvas.drawText("SGST",395f , 283, textPaint);
            canvas.drawText("CGST",445f , 283, textPaint);
            canvas.drawText("IGST",495f , 283, textPaint);
            canvas.drawText("Total",550f , 283, textPaint);

            canvas.drawText("Grand Total:",470f , 735.5f, textPaint);

            textPaint.setTextSize(sizeS);
            canvas.drawText("Rs",300f , 297, textPaint);
            canvas.drawText("Rs",345f , 297, textPaint);
            canvas.drawText("%",381f , 297, textPaint);
            canvas.drawText("Rs",406f , 297, textPaint);
            canvas.drawText("%",431f , 297, textPaint);
            canvas.drawText("Rs",456f , 297, textPaint);
            canvas.drawText("%",481f , 297, textPaint);
            canvas.drawText("Rs",506f , 297, textPaint);
            canvas.drawText("Rs",550f , 297, textPaint);


            textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTextSize(sizeM);
            canvas.drawText("Amount in Words: ",MARGIN+10 , 735.5f, textPaint);
            textPaint.setTextSize(sizeS);
            canvas.drawText("This is computer generated invoice, No signature is required",(float)PAGE_WIDTH/2 , 770, textPaint);





            pdfDocument.finishPage(page);

        }


//        title.setTextAlign(Paint.Align.CENTER);
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        title.setTextSize(70);
//        canvas.drawText(companyName, (float)PAGE_WIDTH/2, 270, title );
//
//        paint2.setColor(Color.rgb(0,113,108));
//        paint2.setTextSize(30f);
//        paint2.setTextAlign(Paint.Align.RIGHT);
//        canvas.drawText(mobNo,1160,40,paint2);
//        canvas.drawText(mobNo,1160,80,paint2);
//
//        //invoice
//        title.setTextAlign(Paint.Align.CENTER);
//        title.setTypeface(Typeface.create(Typeface.create(), Typeface.BOLD));
//        title.setTextSize(70);
//        canvas.drawText("Invoice", (float)PAGE_WIDTH/2, 500, title );
//
//        //cust name
//        paint2.setColor(Color.BLACK);
//        paint2.setTextSize(30f);
//        paint2.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText("Customer Name",20,590,paint2);
//        canvas.drawText("Contact No:",20,640,paint2);
//
//        //inv no
//        paint2.setColor(Color.BLACK);
//        paint2.setTextSize(30f);
//        paint2.setTextAlign(Paint.Align.RIGHT);
//        canvas.drawText("Invoice No: 55598",PAGE_WIDTH-20,590,paint2);
//        canvas.drawText("DATE: 8465454",PAGE_WIDTH-20,690,paint2);
//
//
////        Rectangle
//
//        paint2.setStyle(Paint.Style.STROKE);
//        paint2.setStrokeWidth(2);
//        canvas.drawRect(20,780,PAGE_WIDTH-20, 860, paint2);
//
//        paint2.setStyle(Paint.Style.FILL);
//        paint2.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText("sl No.",40,830,paint2);
//
//        //deviderLine
//        canvas.drawLine(180,790,180,840,paint2);



        //Save


        File file = new File(Environment.getExternalStorageDirectory(), "/testpdf.pdf");

        try{
            pdfDocument.writeTo(new FileOutputStream(file));
        }catch(IOException e){
            e.printStackTrace();
        }

        pdfDocument.close();


        return true;
    }


    public void setupBottomAppBar(){

        bottomSheetBehavior = BottomSheetBehavior.from(binding.navView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //Managing BottomAppBarUi
        binding.scrim.setOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
        binding.bottomAppBar.setNavigationOnClickListener(view -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));


        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.drawer_create_invoice:
//                    navController.navigate(R.id.action_dashboardFragment_to_invoiceCreateFragment);
                    createPdf();
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