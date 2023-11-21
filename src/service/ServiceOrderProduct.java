package service;

import dto.DTOUtils;
import dto.OrderProductDTO;
import model.Order;
import model.OrderProduct;
import repository.OrderProductRepository;
import repository.exceptions.RepositoryException;

public class ServiceOrderProduct {
    private OrderProductRepository orderProductRepository;

    public ServiceOrderProduct(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public void add(OrderProductDTO dto){
        OrderProduct orderProduct= DTOUtils.getOrderProductFromDTO(dto);
        try{
            this.orderProductRepository.add(orderProduct);
        }catch(RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllProductsForOrder(Integer id){
        try{
            this.orderProductRepository.delete(id);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    public void update(OrderProductDTO dto){
        OrderProduct orderProduct=DTOUtils.getOrderProductFromDTO(dto);
        try{
            this.orderProductRepository.update(orderProduct);
        }catch(RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteProductForOrder(Integer orderId,Integer productId){
        try{
            this.orderProductRepository.deleteProductsForOrder(orderId,productId);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
        }
    }
}
