package com.nbrk.rates;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 16.08.12
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyRates {
    private long id;
    private String date;
    private String code;
    private String price;
    private String quantity;

    public CurrencyRates() {}

    public CurrencyRates(String date, String code, String price, String quantity) {
        this.date = date;
        this.code = code;
        this.price = price;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
