package service.validator;

import dto.ProductDTO;
import service.exceptions.ValidationException;

public class ValidatorProduct implements Validator<ProductDTO>{
    @Override
    public void validate(ProductDTO entity) {
        String message="";
        if(entity.name().isEmpty())
            message+="The product name can't be empty!\n";
        if(entity.name().matches("[a-zA-Z]+"))
            message+="The product name must contain only letters!\n";
        if(entity.price()<=0)
            message+="The price must be greater then zero!\n";
        if(entity.quantity()<=0)
            message+="The quantity must be greater then zero!\n";
        if(!message.isEmpty())
            throw new ValidationException(message);
    }
}
