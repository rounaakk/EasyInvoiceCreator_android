package tech.rounak.invoice.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;

import tech.rounak.invoice.utils.NumberToWord;

/**
 * Created by Rounak
 * For more info visit https://rounak.tech
 **/
public class InvoiceModel {

    private String customerName="Cash";
    private String customerGstin="N/A";
    private String mobile="N/A";
    private String billingAddress="N/A";
    private String shippingAddress="N/A";
    private String total;
    private String totalInWords;
    @ServerTimestamp
    private Timestamp timestamp;
    private String invoiceNumber;
    private String currency;


    private ArrayList<ProductModel> products;

    public InvoiceModel() {
    }

    public InvoiceModel(String customerName, String customerGstin, String mobile, String billingAddress, String shippingAddress, ArrayList<ProductModel> products, int invoiceNumber, String currency) {
        this.customerName = customerName==null?"Cash":customerName;
        this.customerGstin = customerGstin==null?"N/A":customerGstin;
        this.mobile = mobile==null?"N/A":mobile;
        this.billingAddress = billingAddress==null?"N/A":billingAddress;
        this.shippingAddress = shippingAddress==null?"N/A":shippingAddress;
        this.products = products;
        this.invoiceNumber=String.valueOf(invoiceNumber);
        this.currency=currency;


        double total = (double)0;

        for(ProductModel p : products){
            total+=Double.parseDouble(p.getProductTotal());
        }

        this.total = String.valueOf(Math.round(total));
        this.totalInWords = NumberToWord.convertNumber(Math.round(total));

    }

//    @ServerTimestamp
//    public FieldValue getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(FieldValue timestamp) {
//        this.timestamp = timestamp;
//    }


    @ServerTimestamp
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }


    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerGstin() {
        return customerGstin;
    }

    public void setCustomerGstin(String customerGstin) {
        this.customerGstin = customerGstin;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public ArrayList<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductModel> products) {
        this.products = products;
    }


    public String getTotalInWords() {
        return totalInWords;
    }

    public void setTotalInWords(String totalInWords) {
        this.totalInWords = totalInWords;
    }
}
