package tech.rounak.invoice.ui.settingsEdit;

import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.rounak.invoice.utils.Event;

public class SettingsEditViewModel extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user = new HashMap<>();
    final String TAG = "SettingsEditFragment";

//    String companyName="";
//    String mobileNumber="";
//    String emailAddress="";
//    String gstin="";
//    String pan="";
//    String address="";
//    String city="";
//    String state=;
//    String pin="";
//    String accountName="";
//    String bankName;
//    String branchName;
//    String accountNumber;
//    String ifsc="";
//
    private MutableLiveData<Event<String>> _toast = new MutableLiveData<>();
    LiveData<Event<String>> toast = _toast;

    public void test(View view){
        _toast.setValue(new Event<>("This is Toast"));
    }

    public void saveUserDetails(){

        user.put("companyName", "New Minerva Sports");
        user.put("gstin", "4545413");
        user.put("pin", 700009);

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });

    }



}