package tech.rounak.invoice.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tech.rounak.invoice.R;
import tech.rounak.invoice.models.InvoiceModel;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceCardHolder> {

    private List<InvoiceModel> invoiceModels ;
    private Context ctx;

    public InvoiceAdapter(List<InvoiceModel> invoiceModels, Context ctx){
        this.invoiceModels=invoiceModels;
        this.ctx=ctx;

    }

    @NonNull
    @Override
    public InvoiceCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_invoice,parent,false);
        return new InvoiceCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceCardHolder holder, int position) {

        InvoiceModel invoiceModel = invoiceModels.get(position);
        holder.name.setText(invoiceModel.getCustomerName());
        holder.date.setText(invoiceModel.getTimestamp().toDate().toString());
        holder.currency.setText(invoiceModel.getCurrency());
        holder.price.setText(invoiceModel.getTotal());
        holder.invNumber.setText(invoiceModel.getInvoiceNumber());



    }

    @Override
    public int getItemCount() {
        return invoiceModels.size();
    }

    class InvoiceCardHolder extends RecyclerView.ViewHolder{


        TextView name;
        TextView date;
        TextView invNumber;
        TextView price;
        TextView currency;

        public InvoiceCardHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.customer_name);
            date =itemView.findViewById(R.id.transaction_date);
            invNumber = itemView.findViewById(R.id.bill_id);
            price = itemView.findViewById(R.id.money);
            currency=itemView.findViewById(R.id.tv_currency);
        }


    }

}


