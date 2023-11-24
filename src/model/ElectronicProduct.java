package model;

public class ElectronicProduct extends Product{


    public ElectronicProduct(Integer id, String name, Double price, Integer quantity) {
        super(id, name, price, quantity);
        super.setProductCategory(ProductCategory.ELECTRONICS);
    }

    public ElectronicProduct(String name, Double price, Integer quantity) {
        super(name, price, quantity);
        super.setProductCategory(ProductCategory.ELECTRONICS);
    }
}
