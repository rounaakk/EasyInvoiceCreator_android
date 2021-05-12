package tech.rounak.invoice.ui.dashboard;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.rounak.invoice.models.InvoiceModel;
import tech.rounak.invoice.models.UserModel;

public class DashboardViewModel extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "HistoryFragment";

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Calendar cal;

    MutableLiveData<Float[]> weeklySalesMutableLiveData = new MutableLiveData<>();

    public ObservableField<String> monthSales = new ObservableField<>();
    public ObservableField<String> monthMoney = new ObservableField<>();

    public ObservableField<String> todaySales = new ObservableField<>();
    public ObservableField<String> todayMoney = new ObservableField<>();


    public ObservableField<String> companyName = new ObservableField<>();

    MutableLiveData<String> companyNameMutableLiveData = new MutableLiveData<>();

    public LiveData<Float[]> getWeeklySalesLiveData() {
        return weeklySalesMutableLiveData;
    }

    public LiveData<String> getCompanyNameMutableLiveData() {
        return companyNameMutableLiveData;
    }

    //    public LiveData<List<InvoiceModel>> getInvoiceModelsLiveData() {
//        return invoiceModelsLiveData;
//    }

    public void fetchData(){

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
                    companyName.set(userModel.getCompanyName());
                    companyNameMutableLiveData.setValue(userModel.getCompanyName());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });




        cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        Date todayStart = new Date(cal.getTimeInMillis());
        int todayWeekDay = cal.get(Calendar.DAY_OF_WEEK);

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


        weekQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    if (documents.size()>0) {

//                        List<Float> dayIncomes = new ArrayList<>(7);
                        Float[] dayIncomes = new Float[7];
                        Arrays.fill(dayIncomes,0f);
                        String currency="";
                        int todaySalesCount=0;

                        Calendar cal = Calendar.getInstance();
                        for(DocumentSnapshot document:documents){
                            Log.d(TAG, "DATA" + document.getData());

                            InvoiceModel invoiceModel = document.toObject(InvoiceModel.class);
                            currency =invoiceModel.getCurrency();
                            cal.setTime(invoiceModel.getTimestamp().toDate());

                            int day = cal.get(Calendar.DAY_OF_WEEK);
                            
//                            if(invoiceModel.getTotal()==null){
//                                Log.d(TAG, "onComplete: getTotal is null");
//                            }else{
//                                Log.d(TAG, "onComplete: getTotal is NOT null" + dayIncomes[day]);
//
//                            }
                            dayIncomes[day-1]+=Float.parseFloat(invoiceModel.getTotal());
                            if(day+1==todayWeekDay){
                                todaySalesCount++;
                            }
                        }
                        weeklySalesMutableLiveData.setValue(dayIncomes);
                        todayMoney.set(currency + ". " + dayIncomes[todayWeekDay-1]);
                        todaySales.set(todaySalesCount + " Customers");
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