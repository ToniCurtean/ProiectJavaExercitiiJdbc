package model;

public class CleaningProduct extends Product{
    public CleaningProduct(Integer id, String name, Double price, Integer quantity) {
        super(id, name, price, quantity);
        super.setProductCategory(ProductCategory.CLEANING);
    }

    public CleaningProduct(String name, Double price, Integer quantity) {
        super(name, price, quantity);
        super.setProductCategory(ProductCategory.CLEANING);
    }
}
