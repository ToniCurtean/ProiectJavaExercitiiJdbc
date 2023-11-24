package service;

import dto.DTOUtils;
import dto.ProductDTO;
import model.Product;
import repository.ProductRepository;
import repository.exceptions.RepositoryException;
import service.exceptions.ServiceException;
import service.validator.Validator;

import java.util.List;

public class ServiceProduct {
    private final ProductRepository productRepository;
    private final Validator<ProductDTO> validator;

    public ServiceProduct(ProductRepository productRepository, Validator<ProductDTO> validatorProduct) {
        this.productRepository = productRepository;
        this.validator=validatorProduct;
    }

    public void add(ProductDTO dto) {
        validator.validate(dto);
        Product product;
        try{
            this.productRepository.findProductByName(dto.name());
            throw new ServiceException("The product already exists!");
        }catch(RepositoryException ex){
            product = DTOUtils.getProductFromDTO(dto);
            try {
                this.productRepository.add(product);
            } catch (RepositoryException exception) {
                System.err.println(exception.getMessage());
            }
        }
    }

    public void delete(Integer id) {
        try {
            this.productRepository.delete(id);
        } catch (RepositoryException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteByName(String prodName) {
        try {
            this.productRepository.deleteByName(prodName);
        } catch (RepositoryException e) {
            System.err.println(e.getMessage());
        }
    }

    public void update(ProductDTO dto) {
        Product product = DTOUtils.getProductFromDTO(dto);
        try {
            this.productRepository.update(product);
        } catch (RepositoryException e) {
            System.err.println(e.getMessage());
        }
    }

    public ProductDTO findOne(Integer id) {
        try {
            return DTOUtils.getProductDTO(this.productRepository.findOne(id));
        } catch (RepositoryException e) {
            System.err.println();
            return null;
        }
    }

    public List<ProductDTO> findAll() {
        try {
            List<Product> products = this.productRepository.findAll();
            return products.stream().map(DTOUtils::getProductDTO).toList();
        } catch (RepositoryException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    public ProductDTO findProductByName(String prodName) {
        return DTOUtils.getProductDTO(this.productRepository.findProductByName(prodName));
    }


}
