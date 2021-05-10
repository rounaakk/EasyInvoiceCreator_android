package tech.rounak.invoice.models;

/**
 * Created by Rounak
 * For more info visit https://rounak.tech
 **/
public class UserModel {

    String companyName;
    String mobileNumber;
    String emailAddress;
    String gstin;
    String pan;
    String address;
    String city;
    String state;
    String pin;
    String accountName;
    String bankName;
    String branchName;
    String accountNumber;
    String ifsc;
    String currency= "Rs";
    int invoiceNumber=0;

    public UserModel() {
    }

    public UserModel(String companyName, String mobileNumber, String emailAddress, String gstin, String pan, String address, String city, String state, String pin, String accountName, String bankName, String branchName, String accountNumber, String ifsc) {
        this.companyName = companyName;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.gstin = gstin;
        this.pan = pan;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.accountName = accountName;
        this.bankName = bankName;
        this.branchName = branchName;
        this.accountNumber = accountNumber;
        this.ifsc = ifsc;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
}
