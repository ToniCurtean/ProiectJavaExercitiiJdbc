package service;

import dto.DTOUtils;
import dto.ProductDTO;
import dto.UserDTO;
import model.Product;
import model.User;
import repository.ProductRepository;
import repository.exceptions.RepositoryException;

import java.util.List;

public class ServiceProduct {
    private ProductRepository productRepository;

    public ServiceProduct(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void add(ProductDTO dto){
        Product product= DTOUtils.getProductFromDTO(dto);
        try{
            this.productRepository.add(product);
        }catch(RepositoryException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void delete(Integer id){
        try{
            this.productRepository.delete(id);
        }catch(RepositoryException e){
            System.out.println(e.getMessage());
        }
    }


    public void update(ProductDTO dto){
        Product product=DTOUtils.getProductFromDTO(dto);
        try{
            this.productRepository.update(product);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    public Product findOne(Integer id){
        try{
            return this.productRepository.findOne(id);
        }catch (RepositoryException e){
            System.out.println();
            return null;
        }
    }

    public List<ProductDTO> findAll(){
        try{
            List<Product> products=this.productRepository.findAll();
            List<ProductDTO> productsDTO=products.stream().map(DTOUtils::getProductDTO).toList();
            return productsDTO;
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return null;
        }
    }


}
