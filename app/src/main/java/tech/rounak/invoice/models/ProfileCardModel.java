package tech.rounak.invoice.models;

/**
 * Created by Rounak
 * For more info visit https://rounak.tech
 **/
public class ProfileCardModel {

    String item;
    String itemDesc;

    public ProfileCardModel(String item, String itemDesc) {
        this.item = item;
        this.itemDesc = itemDesc;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
}
