package model;

public class FoodProduct extends Product{

    public FoodProduct(Integer id, String name, Double price, Integer quantity) {
        super(id, name, price, quantity);
        super.setProductCategory(ProductCategory.FOOD);
    }

    public FoodProduct(String name, Double price, Integer quantity) {
        super(name, price, quantity);
        super.setProductCategory(ProductCategory.FOOD);
    }
}
