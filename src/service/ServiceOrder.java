package service;

import dto.DTOUtils;
import dto.OrderDTO;
import dto.ProductDTO;
import dto.UserDTO;
import model.Order;
import model.Product;
import model.User;
import repository.OrderRepository;
import repository.exceptions.RepositoryException;

import java.util.List;

public class ServiceOrder {
    private final OrderRepository orderRepository;

    public ServiceOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public OrderDTO add(OrderDTO dto){
        Order order= DTOUtils.getOrderFromDTO(dto);
        try{
            return DTOUtils.getOrderDTO(this.orderRepository.add(order));
        }catch(RepositoryException exception){
            System.err.println(exception.getMessage());
            return null;
        }
    }

    public void delete(Integer id){
        try{
            this.orderRepository.delete(id);
        }catch(RepositoryException e){
            System.err.println(e.getMessage());
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

    public OrderDTO findOne(Integer id){
        try{
            return DTOUtils.getOrderDTO(this.orderRepository.findOne(id));
        }catch (RepositoryException e){
            System.out.println();
            return null;
        }
    }

    public List<OrderDTO> findAll(){
        try{
            List<Order> orders=this.orderRepository.findAll();
            return orders.stream().map(DTOUtils::getOrderDTO).toList();
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<OrderDTO> findAllOrdersForUser(Integer userId){
        try{
            List<Order> orders=this.orderRepository.findAllOrdersForUser(userId);
            return orders.stream().map(DTOUtils::getOrderDTO).toList();
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
