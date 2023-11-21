package repository;

import model.Order;

import java.util.List;

public interface OrderRepository extends Repository<Order>{

    List<Order> findAllOrdersForUser(Integer userId);
}
