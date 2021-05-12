package tech.rounak.invoice.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import tech.rounak.invoice.R;
import tech.rounak.invoice.models.InvoiceModel;
import tech.rounak.invoice.utils.InvoicePdfCreator;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceCardHolder> {

    private List<InvoiceModel> invoiceModels = new ArrayList<>();
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
//        holder.date.setText(invoiceModel.getTimestamp().toDate().toString());
        holder.currency.setText(invoiceModel.getCurrency() +".");
        holder.price.setText(invoiceModel.getTotal());
        holder.invNumber.setText("Invoice ID - " + invoiceModel.getInvoiceNumber());

        SimpleDateFormat pattern = new SimpleDateFormat("dd.MM.yyyy | hh:mm aaa", Locale.getDefault());
        String date = pattern.format(invoiceModel.getTimestamp().toDate());
        holder.date.setText(date);



    }


    public void setInvoiceModels(List<InvoiceModel> invoiceModels){

        this.invoiceModels=invoiceModels;
        notifyDataSetChanged();

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        InvoiceModel invoiceModel = invoiceModels.get(position);
                        String fileName = "Invoice" +invoiceModel.getInvoiceNumber() + "_" + invoiceModel.getTimestamp().toString();
                        createPdf(invoiceModel,fileName);
                        readPdf(fileName);
                    }

                }
            });
        }

    }



    public boolean createPdf(InvoiceModel invoiceModel, String filename){
        List<InvoiceModel> invoices = new ArrayList<>();
        invoices.add(invoiceModel);
        InvoicePdfCreator.createPdf(ctx, invoices,filename);
        return true;
    }

    public boolean readPdf(String fileName){

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/invoices/" + fileName +".pdf");
        Uri contentUri = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW, contentUri);
        intent.setDataAndType(contentUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ctx.startActivity(intent);

        return true;
    }






}


