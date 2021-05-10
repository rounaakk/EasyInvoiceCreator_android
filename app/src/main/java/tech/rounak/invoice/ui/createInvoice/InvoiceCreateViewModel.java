package tech.rounak.invoice.ui.createInvoice;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.rounak.invoice.models.InvoiceModel;
import tech.rounak.invoice.models.ProductModel;
import tech.rounak.invoice.utils.Event;

public class InvoiceCreateViewModel extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "Invoice Create Fragment";
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public final String INVOICE_NUMBER = "invoiceNumber";
    public final String CURRENCY = "currency";

    public String name;
    public String mobile;
    public String gst;
    public String billingAddress;
    public String shippingAddress;
    private int currentInvoiceNumber=0;
    private String currentCurrency=" ";

    private MutableLiveData<Event<String>> _toast = new MutableLiveData<>();
    LiveData<Event<String>> toast = _toast;

    private MutableLiveData<Event<Boolean>> _navigateBack = new MutableLiveData<>();
    LiveData<Event<Boolean>> navigateBack = _navigateBack;


    public void setInvoiceNumber(){

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String,Object> userData = document.getData();
                                if(userData.containsKey(INVOICE_NUMBER)){
                                    currentInvoiceNumber = Integer.parseInt(userData.get(INVOICE_NUMBER).toString());
                                }
                                if(userData.containsKey(CURRENCY)){
                                    currentCurrency = userData.get(CURRENCY).toString();
                                }
//                                Log.d(TAG, "onComplete: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


    }


    public void saveInvoice(ArrayList<ProductModel> productModels){

        InvoiceModel invoiceModel = new InvoiceModel(name,gst,mobile,billingAddress,shippingAddress,productModels, currentInvoiceNumber, currentCurrency);
        db.collection("users").document(currentUser.getUid()).update(INVOICE_NUMBER, FieldValue.increment(1));

        // Add a new document with a generated ID
        db.collection("users/"+currentUser.getUid()+"/bills").add(invoiceModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                _toast.setValue(new Event<>("Invoice Created Successfully"));
                _navigateBack.setValue(new Event<>(true));

            }else{
                _toast.setValue(new Event<>("Invoice Creation Unsuccessful"));
            }
        });


    }


}