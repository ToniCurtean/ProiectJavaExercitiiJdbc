package repository;

import model.OrderProduct;
import model.Product;

import java.util.List;

public interface OrderProductRepository extends Repository<OrderProduct>{
    void deleteProductsForOrder(Integer orderId,Integer productId);

}
