package dto;

import model.ProductCategory;

public record ProductDTO(Integer id, String name, Double price, Integer quantity, ProductCategory productCategory) {
    public ProductDTO withQuantity(Integer quantity){
        return new ProductDTO(id(),name(),price(),quantity,productCategory());
    }

    public ProductDTO withPrice(Double price){
        return new ProductDTO(id(),name(),price,quantity(),productCategory());
    }
}
