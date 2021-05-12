package tech.rounak.invoice.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import tech.rounak.invoice.R;
import tech.rounak.invoice.models.InvoiceModel;
import tech.rounak.invoice.models.ProductModel;
import tech.rounak.invoice.models.UserModel;

/**
 * Created by Rounak
 * For more info visit https://rounak.tech
 **/
public class InvoicePdfCreator {

    private static FirebaseAuth mAuth;
    static FirebaseUser currentUser;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static final String TAG = "PDF CREATOR";

    static String fileName;
//    static InvoiceModel invoiceModel;
    static Context ctx;

    static List<InvoiceModel> invoiceList;

    static int creationType;
    static final int SINGLE=0;
    static final int LIST=1;


//    public static void createPdf(Context context, InvoiceModel invoice){
//
//        ctx = context;
//        invoiceModel=invoice;
//        creationType=SINGLE;
//        fetchProfileDetails();
//
//    }

    public static void createPdf(Context context, List<InvoiceModel> invoices, String fileName){

        ctx = context;
        InvoicePdfCreator.invoiceList=invoices;
        InvoicePdfCreator.fileName = fileName;
//        creationType=LIST;
        fetchProfileDetails();

    }


    private static File getFileStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory(), "Invoices");
        if (!file.mkdirs()) {
            Log.e("TAG", "Directory not created");
        }
        return file;
    }


    public static void fetchProfileDetails(){

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        DocumentReference docRef = db.collection("users").document(currentUser.getUid());

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
//                    Log.d(TAG, "Current data: " + snapshot.getData());

                    UserModel userModel = snapshot.toObject(UserModel.class);
                    createPdfLayoutAndFillStats(userModel);

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }


    public static void createPdfLayoutAndFillStats(UserModel userModel){

//        int PAGES;
//        if(creationType==SINGLE){
//            PAGES =1;
//        }else{
        int PAGES=invoiceList.size();
//        }


        int PAGE_WIDTH = 600;
        int PAGE_HEIGHT = 800;
        int MARGIN = 20;

        final int sizeH1 = 30;
        final int sizeH2 = 24;
        final int sizeM = 9;
        final int sizeS = 7;


//        if(PAGES==1){
//            fileName= invoiceList.get(0).getInvoiceNumber() + "_" + invoiceList.get(0).getTimestamp().toDate().toString();
//        }else{
//            fileName = invoiceList.get(0).getTimestamp().toDate().toString();
//        }

        PdfDocument pdfDocument = new PdfDocument();
        Paint textPaint = new Paint();
        Paint shapePaint = new Paint();

        for(int i = 1; i<=PAGES; i++){
            InvoiceModel invoiceModel= invoiceList.get(i-1);

            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH,PAGE_HEIGHT,i).create();
            PdfDocument.Page page = pdfDocument.startPage(myPageInfo);
            Canvas canvas = page.getCanvas();

            //Header
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTypeface(  Typeface.create(ResourcesCompat.getFont(ctx, R.font.raleway_bold), Typeface.BOLD));
            textPaint.setTextSize(sizeH1);
            canvas.drawText(userModel.getCompanyName(),(float)MARGIN , 50f, textPaint);


            textPaint.setTextAlign(Paint.Align.RIGHT);
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            textPaint.setTextSize(sizeM);

            canvas.drawText("Invoice Number: " +invoiceModel.getInvoiceNumber(),PAGE_WIDTH-MARGIN , 35, textPaint);

            SimpleDateFormat pattern = new SimpleDateFormat("dd.MM.yyyy | hh:mm aaa", Locale.getDefault());
            String date = pattern.format(invoiceModel.getTimestamp().toDate());
            canvas.drawText(date,PAGE_WIDTH-MARGIN , 50, textPaint);

            shapePaint.setStyle(Paint.Style.STROKE);
            shapePaint.setStrokeWidth(1);
            canvas.drawLine(MARGIN,70,PAGE_WIDTH-MARGIN,70,shapePaint);



            //Bank Details
            canvas.drawText(userModel.getAccountName(),PAGE_WIDTH-MARGIN , 100, textPaint);
            canvas.drawText(userModel.getBankName() + ", " + userModel.getBranchName(),PAGE_WIDTH-MARGIN , 115, textPaint);
            canvas.drawText("A/C No. :  "+userModel.getAccountNumber(),PAGE_WIDTH-MARGIN , 130, textPaint);
            canvas.drawText("IFSC Code :  " +userModel.getIfsc() ,PAGE_WIDTH-MARGIN , 145, textPaint);
//            canvas.drawText("dsadsdsdsfsdfsd",PAGE_WIDTH-MARGIN , 160, textPaint);


            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Bank Details    ",PAGE_WIDTH-MARGIN , 85, textPaint);

            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("GSTIN :  " + userModel.getGstin(),MARGIN , 85, textPaint);
            canvas.drawText("PAN :  " + userModel.getPan(),MARGIN , 100, textPaint);
            canvas.drawText("Mob. :  " + userModel.getMobileNumber(),MARGIN , 115, textPaint);
            canvas.drawText("Email :  " + userModel.getEmailAddress(),MARGIN , 130, textPaint);
            canvas.drawText("Address :  "+userModel.getAddress(),MARGIN , 145, textPaint);
            canvas.drawText("                      " + userModel.getCity() + ", " + userModel.getState() + " " + userModel.getPin(),MARGIN , 160, textPaint);


            //Customer Details
            canvas.drawText("Customer Name :  " + invoiceModel.getCustomerName(),MARGIN , 200, textPaint);
            canvas.drawText("Mobile No. :  " + invoiceModel.getMobile(),MARGIN , 215, textPaint);
            canvas.drawText("Billing Address :  " + invoiceModel.getBillingAddress(),MARGIN , 230, textPaint);
            canvas.drawText("Shipping Address :  " + invoiceModel.getShippingAddress() ,MARGIN , 245, textPaint);
            canvas.drawText("Customer GSTIN :  " + invoiceModel.getCustomerGstin(),MARGIN , 260, textPaint);


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
            canvas.drawText("Amount in Words :  Rupees " + invoiceModel.getTotalInWords(),MARGIN+10 , 735.5f, textPaint);
            textPaint.setTextSize(sizeS);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("This is computer generated invoice, No signature is required",(float)PAGE_WIDTH/2 , 770, textPaint);


            //Products

            textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTextSize(sizeM);

            float yPos = 325f;
            for(ProductModel product : invoiceModel.getProducts()){

                canvas.drawText(product.getSerialNumber(),22 , yPos, textPaint);
                canvas.drawText(product.getProductName(),47 , yPos, textPaint);
                canvas.drawText(product.getProductHSN(),182 , yPos, textPaint);
                canvas.drawText(product.getProductQuantity(),242 , yPos, textPaint);
                canvas.drawText(product.getProductPrice(),282 , yPos, textPaint);
                canvas.drawText(product.getSubTotal(),322 , yPos, textPaint);
                canvas.drawText(product.getSgstPercent(),372 , yPos, textPaint);
                canvas.drawText(product.getCgstPercent(),422 , yPos, textPaint);
                canvas.drawText(product.getIgstPercent(),472 , yPos, textPaint);
                canvas.drawText(product.getProductTotal(),522 , yPos, textPaint);

//            %
                canvas.drawText(product.getSgstAmt(),394 , yPos, textPaint);
                canvas.drawText(product.getCgstAmt(),444 , yPos, textPaint);
                canvas.drawText(product.getIgstAmt(),494 , yPos, textPaint);

                yPos+=15f;
            }



            //Grand Total
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(invoiceModel.getTotal(),550f , 735.5f, textPaint);
            pdfDocument.finishPage(page);

        }

        //Save

        File file = new File(getFileStorageDir(),  fileName + ".pdf");

        try{
            pdfDocument.writeTo(new FileOutputStream(file));
        }catch(IOException e){
            e.printStackTrace();
        }

        pdfDocument.close();

//        readPdf(fileName);

    }






}
