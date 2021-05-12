package tech.rounak.invoice.ui.dashboard;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.rounak.invoice.models.InvoiceModel;

public class DashboardViewModel extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "HistoryFragment";

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Calendar cal;

    List<Integer> weeklySalesList = new ArrayList<>();
    MutableLiveData<List<Integer>> weeklySalesMutableLiveData = new MutableLiveData<>();

    public ObservableField<String> monthSales = new ObservableField<>();
    public ObservableField<String> monthMoney = new ObservableField<>();

    public ObservableField<String> todaySales = new ObservableField<>();
    public ObservableField<String> todayMoney = new ObservableField<>();


//    public LiveData<List<InvoiceModel>> getInvoiceModelsLiveData() {
//        return invoiceModelsLiveData;
//    }

    public void fetchInvoices(){

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        Date todayStart = new Date(cal.getTimeInMillis());

        cal.clear();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date weekStart = new Date(cal.getTimeInMillis());

        cal.clear();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = new Date(cal.getTimeInMillis());


        CollectionReference billsCollectionRef = db.collection("users/"+currentUser.getUid()+"/bills");

        Query todayQuery = billsCollectionRef
                .whereGreaterThanOrEqualTo("timestamp", todayStart);

        Query weekQuery = billsCollectionRef
                .whereGreaterThanOrEqualTo("timestamp", weekStart);

        Query monthQuery = billsCollectionRef
                .whereGreaterThanOrEqualTo("timestamp", monthStart);


        monthQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    if (documents.size()>0) {

//                        List<InvoiceModel> invoiceModels = new ArrayList<>();
                        int money = 0;
                        int sales = 0;
                        String currency="";
                        for(DocumentSnapshot document:documents){
//                            Log.d(TAG, "DATA" + document.getData());

                            InvoiceModel invoiceModel = document.toObject(InvoiceModel.class);
                            currency =invoiceModel.getCurrency();
                            money+= Integer.parseInt(invoiceModel.getTotal());
                            sales+=1;
                        }
                        monthMoney.set(currency + ". " +String.valueOf(money));
                        monthSales.set(String.valueOf(sales) + " Sales");
//                      Log.d(TAG, "onComplete: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });



//        colRef.where('startTime', '>=', start).where('startTime', '<=', end).get().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
//                    if (documents.size()>0) {
//                        for(DocumentSnapshot document:documents){
//
//                            Log.d(TAG, "DATA" + document.getData());
//                            InvoiceModel invoiceModel = document.toObject(InvoiceModel.class);
//                            invoiceModels.add(invoiceModel);
//                        }
//                        invoiceModelsLiveData.setValue(invoiceModels);
////                      Log.d(TAG, "onComplete: " + document.getData());
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//
//            }
//        });




    }


}