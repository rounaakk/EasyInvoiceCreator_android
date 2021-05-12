package tech.rounak.invoice.ui.history;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.rounak.invoice.models.InvoiceModel;

public class HistoryViewModel extends ViewModel {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "HistoryFragment";

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    List<InvoiceModel> invoiceModels = new ArrayList<>();
    MutableLiveData<List<InvoiceModel>> invoiceModelsLiveData = new MutableLiveData<>();

    public LiveData<List<InvoiceModel>> getInvoiceModelsLiveData() {
        return invoiceModelsLiveData;
    }

    public void fetchInvoices(){

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        CollectionReference colRef = db.collection("users/"+currentUser.getUid()+"/bills");


        Query orderedInvoices = colRef
                .orderBy("timestamp", Query.Direction.DESCENDING);


        orderedInvoices.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    if (documents.size()>0) {
                        for(DocumentSnapshot document:documents){

                      Log.d(TAG, "DATA" + document.getData());
                            InvoiceModel invoiceModel = document.toObject(InvoiceModel.class);
                            invoiceModels.add(invoiceModel);
                        }
                        invoiceModelsLiveData.setValue(invoiceModels);
//                      Log.d(TAG, "onComplete: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });



    }

    public void fetchInvoices(Date startDate, Date endDate){

        invoiceModels.clear();
        invoiceModelsLiveData.setValue(invoiceModels);
        CollectionReference colRef = db.collection("users/"+currentUser.getUid()+"/bills");

        Query orderedInvoices = colRef.whereGreaterThanOrEqualTo("timestamp" , startDate)
                .whereLessThanOrEqualTo("timestamp", endDate)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        orderedInvoices.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    if (documents.size()>0) {
                        for(DocumentSnapshot document:documents){

                            Log.d(TAG, "DATA" + document.getData());
                            InvoiceModel invoiceModel = document.toObject(InvoiceModel.class);
                            invoiceModels.add(invoiceModel);
                        }
                        invoiceModelsLiveData.setValue(invoiceModels);
//                      Log.d(TAG, "onComplete: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });



    }



}