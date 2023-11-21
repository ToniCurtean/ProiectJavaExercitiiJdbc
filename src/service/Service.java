package service;

import dto.*;
import model.Order;
import model.Product;
import model.ShoppingCart;
import model.User;
import repository.*;
import repository.exceptions.RepositoryException;
import service.ServiceOrder;
import service.ServiceOrderProduct;
import service.ServiceProduct;
import service.ServiceUser;
import service.exceptions.ValidationException;

import java.util.List;
import java.util.Map;

public class Service {
    private final ServiceUser serviceUser;
    private final ServiceProduct serviceProduct;
    private final ServiceOrder serviceOrder;
    private final ServiceOrderProduct serviceOrderProduct;

    public Service(ServiceUser serviceUser, ServiceProduct serviceProduct, ServiceOrder serviceOrder, ServiceOrderProduct serviceOrderProduct) {
        this.serviceUser = serviceUser;
        this.serviceProduct = serviceProduct;
        this.serviceOrder = serviceOrder;
        this.serviceOrderProduct = serviceOrderProduct;
    }

    public ServiceUser getServiceUser() {
        return serviceUser;
    }

    public ServiceProduct getServiceProduct() {
        return serviceProduct;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public ServiceOrderProduct getServiceOrderProduct() {
        return serviceOrderProduct;
    }

    public void addOrder(UserDTO userDTO) {
        OrderDTO orderDTO=this.serviceOrder.add(new OrderDTO(0,userDTO.id()));
        userDTO.shoppingCart().getProducts().forEach((prodName,prodQuant)->{
                ProductDTO productDTO=this.serviceProduct.findProductByName(prodName);
                ProductDTO updated=productDTO.withQuantity(productDTO.quantity()-prodQuant);
                this.serviceProduct.update(updated);
                OrderProductDTO orderProductDTO=new OrderProductDTO(orderDTO.id(),productDTO.id(),prodQuant);
                this.serviceOrderProduct.add(orderProductDTO);
            });
    }

    public boolean verifyProduct(String productName,Integer quantity){
        ProductDTO productDTO=null;
        try{
                productDTO=this.serviceProduct.findProductByName(productName);
        }catch (RepositoryException e){
            System.err.println(e.getMessage());
            return false;
        }
        if(productDTO.quantity()<quantity)
            throw new ValidationException("We don't have that much in stock!");
        return true;
    }

    public Double getTotalForCart(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts().entrySet().stream().mapToDouble(entry->{
            String productName=entry.getKey();
            Integer quantity=entry.getValue();
            ProductDTO productDTO= this.serviceProduct.findProductByName(productName);
            return quantity*productDTO.price();
        }).sum();
    }

    public String getMostExpensiveProductCart(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts().entrySet().stream().map(entry -> {
            ProductDTO productDTO = this.serviceProduct.findProductByName(entry.getKey());
            Double totalPrice = productDTO.price() * entry.getValue();
            return Map.entry(entry.getKey(),totalPrice);
        }).max(Map.Entry.comparingByValue()).map(maxEntry-> "Product: "+maxEntry.getKey()+" , Price: "+maxEntry.getValue()).orElse("Shopping cart is empty");
    }

    public String getLeastExpensiveProductCart(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts().entrySet().stream().map(entry -> {
            ProductDTO productDTO = this.serviceProduct.findProductByName(entry.getKey());
            Double totalPrice = productDTO.price() * entry.getValue();
            return Map.entry(entry.getKey(),totalPrice);
        }).min(Map.Entry.comparingByValue()).map(minEntry-> "Product: "+minEntry.getKey()+" , Price: "+minEntry.getValue()).orElse("Shopping cart is empty");
    }

    public void showProductsForMinPrice(Integer minPrice){
        List<ProductDTO> products=this.serviceProduct.findAll();
        if(products.stream().noneMatch(product->product.price()>=minPrice)){
            System.out.println("No products over the price of "+minPrice);
        }else{
            products.stream().filter((product)->product.price()>=minPrice && product.quantity()>0).forEach((product)->System.out.println("Product name: " + product.name()+" , Price: "+product.price()+" , Available Quantity: " +product.quantity()));
        }
    }


    public void showProductsForMaxPrice(Integer maxPrice) {
        List<ProductDTO> products=this.serviceProduct.findAll();
        if(products.stream().noneMatch(product->product.price()<=maxPrice))
            System.out.println("No products below the price of "+ maxPrice);
        else{
            products.stream().filter((product)->product.price()<=maxPrice && product.quantity()>0).forEach((product)->System.out.println("Product name: " + product.name()+" , Price: "+product.price()+" , Available Quantity: " +product.quantity()));
        }
    }

    public void updateProductQuantity(String productName, Integer productQuantity) {
        ProductDTO productDTO=null;
        try{
            productDTO=this.serviceProduct.findProductByName(productName);
            ProductDTO modified=productDTO.withQuantity(productQuantity);
            this.serviceProduct.update(modified);
            System.out.println("Updated the product quantity successfully!");
        }catch (RepositoryException ex){
            System.err.println(ex.getMessage());
        }
    }

    public void updateProductPrice(String productName, Double productPrice){
        ProductDTO productDTO=null;
        try{
            productDTO=this.serviceProduct.findProductByName(productName);
            ProductDTO modified=productDTO.withPrice(productPrice);
            this.serviceProduct.update(modified);
            System.out.println("Updated the product price successfully!");
        }catch (RepositoryException ex){
            System.err.println(ex.getMessage());
        }
    }
}
