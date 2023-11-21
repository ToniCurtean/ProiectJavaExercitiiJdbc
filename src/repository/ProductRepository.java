package repository;

import model.Product;

import java.util.List;

public interface ProductRepository extends Repository<Product>{
    Product findProductByName(String prodName);

    void deleteByName(String prodName);
}
