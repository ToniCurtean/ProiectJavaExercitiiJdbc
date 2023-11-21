package service;

import dto.DTOUtils;
import dto.OrderDTO;
import dto.ProductDTO;
import model.Order;
import model.Product;
import repository.OrderRepository;
import repository.exceptions.RepositoryException;

import java.util.List;

public class ServiceOrder {
    private OrderRepository orderRepository;

    public ServiceOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public void add(OrderDTO dto){
        Order order= DTOUtils.getOrderFromDTO(dto);
        try{
            this.orderRepository.add(order);
        }catch(RepositoryException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void delete(Integer id){
        try{
            this.orderRepository.delete(id);
        }catch(RepositoryException e){
            System.out.println(e.getMessage());
        }
    }


    public void update(OrderDTO dto){
        Order order=DTOUtils.getOrderFromDTO(dto);
        try{
            this.orderRepository.update(order);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    public Order findOne(Integer id){
        try{
            return this.orderRepository.findOne(id);
        }catch (RepositoryException e){
            System.out.println();
            return null;
        }
    }

    public List<Order> findAll(){
        try{
            return this.orderRepository.findAll();
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
