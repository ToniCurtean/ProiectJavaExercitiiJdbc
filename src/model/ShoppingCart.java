package model;

import model.exception.ShoppingCartException;
import repository.exceptions.RepositoryException;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private Map<String,Integer> products;

    private Double total;

    public ShoppingCart() {
        this.total=0.0;
        this.products=new HashMap<>();
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public void emptyCart(){
        this.products.clear();
    }

    public void addToShoppingCart(String productName, Integer quantity){
        if(this.products.containsKey(productName))
            throw new ShoppingCartException("The product is already in the shopping cart!");
        this.products.put(productName,quantity);
    }

    public void removeFromShoppingCart(String productName){
        if(!this.products.containsKey(productName))
            throw new ShoppingCartException("The product you want to delete is not in the shopping cart!");
        this.products.remove(productName);
    }

    public void modifyQuantity(String productName,Integer newQuantity){
        if(!this.products.containsKey(productName))
            throw new ShoppingCartException("The product you want to modify the quantity for is not in the shopping cart!");
        this.products.put(productName,newQuantity);
    }


}
