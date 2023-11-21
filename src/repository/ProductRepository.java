package repository;

import model.Product;

import java.util.List;

public interface ProductRepository extends Repository<Product>{
    public List<Product> getProductsForOrder(Integer orderId);
}
