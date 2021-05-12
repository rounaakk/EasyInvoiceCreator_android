package tech.rounak.invoice.models;

/**
 * Created by Rounak
 * For more info visit https://rounak.tech
 **/
public class ProductModel {

    private String serialNumber;
    private String productName="Product";
    private String productHSN="N/A";
    private String productQuantity="0";
    private String productPrice = "0";
    private String subTotal;
    private String sgstPercent;
    private String sgstAmt;
    private String cgstPercent;
    private String cgstAmt;
    private String igstPercent;
    private String igstAmt;
    private String productTotal;

    public ProductModel() {
    }

    public ProductModel(String serialNumber, String productName, String productHSN, String productQuantity, String productPrice, String sgstPercent, String cgstPercent, String igstPercent) {
        this.serialNumber = serialNumber;
        this.productName = productName.length()==0?"Product " + serialNumber :productName;
        this.productHSN = productHSN.length()==0?"N/A":productHSN;
        this.productQuantity = productQuantity.length()==0?"0":productQuantity ;
        this.productPrice = productPrice.length()==0?"0":productPrice;
        this.sgstPercent = sgstPercent.length()==0?"0":sgstPercent;
        this.cgstPercent = cgstPercent.length()==0?"0":cgstPercent;
        this.igstPercent = igstPercent.length()==0?"0":igstPercent;

        double subTotal=Double.parseDouble(this.productPrice)*Double.parseDouble(this.productQuantity);
        double sgstAmt = (subTotal*Double.parseDouble(this.sgstPercent))/100;
        double cgstAmt = (subTotal*Double.parseDouble(this.cgstPercent))/100;
        double igstAmt = (subTotal*Double.parseDouble(this.igstPercent))/100;

        double productTotal = subTotal+sgstAmt+cgstAmt+igstAmt;

        this.subTotal= String.valueOf(Math.round(subTotal));
        this.cgstAmt=String.valueOf(Math.round(cgstAmt));
        this.sgstAmt=String.valueOf(Math.round(sgstAmt));
        this.igstAmt=String.valueOf(Math.round(igstAmt));
        this.productTotal=String.valueOf(Math.round(productTotal));

    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductHSN() {
        return productHSN;
    }

    public void setProductHSN(String productHSN) {
        this.productHSN = productHSN;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getSgstPercent() {
        return sgstPercent;
    }

    public void setSgstPercent(String sgstPercent) {
        this.sgstPercent = sgstPercent;
    }

    public String getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(String sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public String getCgstPercent() {
        return cgstPercent;
    }

    public void setCgstPercent(String cgstPercent) {
        this.cgstPercent = cgstPercent;
    }

    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public String getIgstPercent() {
        return igstPercent;
    }

    public void setIgstPercent(String igstPercent) {
        this.igstPercent = igstPercent;
    }

    public String getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(String igstAmt) {
        this.igstAmt = igstAmt;
    }

    public String getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(String productTotal) {
        this.productTotal = productTotal;
    }
}
