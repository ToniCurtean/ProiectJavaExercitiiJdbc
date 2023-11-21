package service;

import dto.DTOUtils;
import dto.OrderProductDTO;
import dto.ProductDTO;
import model.Order;
import model.OrderProduct;
import model.Product;
import repository.OrderProductRepository;
import repository.exceptions.RepositoryException;

import java.util.List;

public class ServiceOrderProduct {
    private final OrderProductRepository orderProductRepository;

    public ServiceOrderProduct(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public void add(OrderProductDTO dto){
        OrderProduct orderProduct= DTOUtils.getOrderProductFromDTO(dto);
        try{
            this.orderProductRepository.add(orderProduct);
        }catch(RepositoryException e){
            System.err.println(e.getMessage());
        }
    }

    public void update(OrderProductDTO dto){
        OrderProduct orderProduct=DTOUtils.getOrderProductFromDTO(dto);
        try{
            this.orderProductRepository.update(orderProduct);
        }catch(RepositoryException e){
            System.err.println(e.getMessage());
        }
    }

    public List<ProductDTO> findAllForOrder(Integer orderId){
        try{
            List<Product> products=this.orderProductRepository.getProductsForOrder(orderId);
            return products.stream().map(DTOUtils::getProductDTO).toList();
        }catch (RepositoryException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
}
