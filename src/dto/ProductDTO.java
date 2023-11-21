package dto;

public record ProductDTO(Integer id,String name,Double price,Integer quantity) {
    public ProductDTO withQuantity(Integer quantity){
        return new ProductDTO(id(),name(),price(),quantity);
    }

    public ProductDTO withPrice(Double price){
        return new ProductDTO(id(),name(),price,quantity());
    }
}
