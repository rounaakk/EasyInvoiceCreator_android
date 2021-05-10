package tech.rounak.invoice.ui.settingsEdit;

import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.rounak.invoice.models.UserModel;
import tech.rounak.invoice.utils.Event;

public class SettingsEditViewModel extends ViewModel {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "SettingsEditFragment";
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public ObservableField<String> companyName = new ObservableField<>();
    public ObservableField<String> mobileNumber = new ObservableField<>();
    public ObservableField<String> emailAddress = new ObservableField<>();
    public ObservableField<String> gstin = new ObservableField<>();
    public ObservableField<String> pan = new ObservableField<>();
    public ObservableField<String> address = new ObservableField<>();
    public ObservableField<String> city = new ObservableField<>();
    public ObservableField<String> state = new ObservableField<>();
    public ObservableField<String> pin = new ObservableField<>();
    public ObservableField<String> accountName = new ObservableField<>();
    public ObservableField<String> bankName = new ObservableField<>();
    public ObservableField<String> branchName = new ObservableField<>();
    public ObservableField<String> accountNumber = new ObservableField<>();
    public ObservableField<String> ifsc = new ObservableField<>();


    private MutableLiveData<Event<String>> _toast = new MutableLiveData<>();
    LiveData<Event<String>> toast = _toast;



    public void fetchUserDetails(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        db.collection("users").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        UserModel userModel = document.toObject(UserModel.class);
                        setDataInEditText(userModel);

                    } else {

                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }


    public void setDataInEditText(UserModel um){

        if(um.getCompanyName()!=null){
            companyName.set(um.getCompanyName());
        }
        if(um.getMobileNumber()!=null){
            mobileNumber.set(um.getMobileNumber());
        }
        if(um.getEmailAddress()!=null){
            emailAddress.set(um.getEmailAddress());
        }
        if(um.getGstin()!=null){
            gstin.set(um.getGstin());
        }
        if(um.getPan()!=null){
            pan.set(um.getPan());
        }
        if(um.getAddress()!=null){
            address.set(um.getAddress());
        }
        if(um.getCity()!=null){
            city.set(um.getCity());
        }
        if(um.getState()!=null){
            state.set(um.getState());
        }
        if(um.getPin()!=null){
            pin.set(um.getPin());
        }
        if(um.getAccountName()!=null){
            accountName.set(um.getAccountName());
        }
        if(um.getBankName()!=null){
            bankName.set(um.getBankName());
        }
        if(um.getBranchName()!=null){
            branchName.set(um.getBranchName());
        }
        if(um.getAccountNumber()!=null){
            accountNumber.set(um.getAccountNumber());
        }
        if(um.getIfsc()!=null){
            ifsc.set(um.getIfsc());
        }

    }
    public void saveUserDetails(View view){

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        UserModel userModel = new UserModel(companyName.get(),mobileNumber.get(),emailAddress.get(),
                gstin.get(),pan.get(),address.get(),city.get(),state.get(),pin.get(),
                accountName.get(),bankName.get(),branchName.get(),accountNumber.get(),ifsc.get());

        // Add a new document with a generated ID
        db.collection("users").document(currentUser.getUid()).set(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    _toast.setValue(new Event<>("Profile Updated Successfully"));
                }else{
                    _toast.setValue(new Event<>("Profile Update Unsuccessful"));
                }
            }
        });

    }



}