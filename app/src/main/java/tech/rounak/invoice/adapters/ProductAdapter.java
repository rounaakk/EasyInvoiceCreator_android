package tech.rounak.invoice.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tech.rounak.invoice.R;
import tech.rounak.invoice.models.ProductModel;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductCardHolder> {

    private List<ProductModel> productModels = new ArrayList<>();

    public ProductAdapter(List<ProductModel> productCardModels){
        this.productModels=productCardModels;

    }

    @NonNull
    @Override
    public ProductCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_list,parent,false);
        return new ProductCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardHolder holder, int position) {

        ProductModel productModel = productModels.get(position);
        holder.itemName.setText(productModel.getProductName());
//        String text = "Subtotal: Rs." + productModel.getSubTotal() + " | GST: Rs." + Math.round(Double.parseDouble(productModel.getProductTotal()) - Double.parseDouble(productModel.getSubTotal()));
        String text = "Subtotal: Rs." + productModel.getSubTotal();

        holder.itemSubtotalGst.setText(text);
        holder.itemTotal.setText(productModel.getProductTotal());


    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }


    class ProductCardHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        TextView itemSubtotalGst;
        TextView itemTotal;

        public ProductCardHolder(@NonNull View itemView) {
            super(itemView);

            itemName=itemView.findViewById(R.id.tv_item_name);
            itemSubtotalGst = itemView.findViewById(R.id.tv_subtotal_gst);
            itemTotal = itemView.findViewById(R.id.money);

        }
    }


}


