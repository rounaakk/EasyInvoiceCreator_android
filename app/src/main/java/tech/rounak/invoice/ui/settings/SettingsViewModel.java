package tech.rounak.invoice.ui.settings;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.rounak.invoice.models.ProfileCardModel;
import tech.rounak.invoice.models.UserModel;

public class SettingsViewModel extends ViewModel {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "SettingsFragment";

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    List<ProfileCardModel> profileCardModels = new ArrayList<>();
    MutableLiveData<List<ProfileCardModel>> profileCardModelsMutableLiveData = new MutableLiveData<>();

    public LiveData<List<ProfileCardModel>> getProfileCardModelsLiveData() {
        return profileCardModelsMutableLiveData;
    }


    public void fetchProfileDetails(){

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
                    convertToList(userModel);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }

    private void convertToList(UserModel um){

        profileCardModels.clear();

        if(um.getCompanyName()!=null ){
            profileCardModels.add(new ProfileCardModel(um.getCompanyName(), "Company Name"));
        }
        if(um.getMobileNumber()!=null){
            profileCardModels.add(new ProfileCardModel(um.getMobileNumber(), "Mobile Number"));
        }
        if(um.getEmailAddress()!=null){
            profileCardModels.add(new ProfileCardModel(um.getEmailAddress(), "Email Address"));
        }
        if(um.getGstin()!=null){
            profileCardModels.add(new ProfileCardModel(um.getGstin(), "GSTIN"));
        }
        if(um.getPan()!=null){
            profileCardModels.add(new ProfileCardModel(um.getPan(), "PAN"));
        }
        if(um.getAddress()!=null){
            profileCardModels.add(new ProfileCardModel(um.getAddress(), "Address"));
        }
        if(um.getCity()!=null){
            profileCardModels.add(new ProfileCardModel(um.getCity(), "City"));
        }
        if(um.getState()!=null){
            profileCardModels.add(new ProfileCardModel(um.getState(), "State"));
        }
        if(um.getPin()!=null){
            profileCardModels.add(new ProfileCardModel(um.getPin(), "Pin Code"));
        }
        if(um.getAccountName()!=null){
            profileCardModels.add(new ProfileCardModel(um.getAccountName(), "Account Name"));
        }
        if(um.getBankName()!=null){
            profileCardModels.add(new ProfileCardModel(um.getBankName(), "Bank Name"));
        }
        if(um.getBranchName()!=null){
            profileCardModels.add(new ProfileCardModel(um.getBranchName(), "Branch Name"));
        }
        if(um.getAccountNumber()!=null){
            profileCardModels.add(new ProfileCardModel(um.getAccountNumber(), "Account Number"));
        }
        if(um.getIfsc()!=null){
            profileCardModels.add(new ProfileCardModel(um.getIfsc(), "IFSC"));
        }

        profileCardModelsMutableLiveData.setValue(profileCardModels);


    }


}