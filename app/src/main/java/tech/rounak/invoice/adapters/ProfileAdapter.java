package tech.rounak.invoice.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import tech.rounak.invoice.BR;
import tech.rounak.invoice.R;
import tech.rounak.invoice.databinding.CardSettingsBinding;
import tech.rounak.invoice.models.ProfileCardModel;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileCardHolder> {

    private List<ProfileCardModel> profileCardModels;
    private Context ctx;

    public ProfileAdapter(List<ProfileCardModel> profileCardModels, Context ctx){
        this.profileCardModels=profileCardModels;
        this.ctx=ctx;

    }

    @NonNull
    @Override
    public ProfileCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardSettingsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_settings, parent, false);
        return new ProfileCardHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ProfileCardHolder holder, int position) {

        ProfileCardModel profileCardModel = profileCardModels.get(position);
        holder.bind(profileCardModel);
    }

    @Override
    public int getItemCount() {
        return profileCardModels.size();
    }

    class ProfileCardHolder extends RecyclerView.ViewHolder{


        CardSettingsBinding cardSettingsBinding;
        TextView item;
        TextView itemDesc;

        public ProfileCardHolder(CardSettingsBinding cardSettingsBinding) {
            super(cardSettingsBinding.getRoot());
            this.cardSettingsBinding=cardSettingsBinding;

            item = itemView.findViewById(R.id.tv_item_name);
            itemDesc = itemView.findViewById(R.id.tv_profile_desc);
        }

        public void bind(Object obj){
            cardSettingsBinding.setVariable(BR.profileItem, obj);
            cardSettingsBinding.executePendingBindings();
        }

    }

}


