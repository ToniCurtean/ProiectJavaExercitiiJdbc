package service.validator;

import dto.UserDTO;
import service.exceptions.ValidationException;


public class ValidatorUser implements Validator<UserDTO>{
    @Override
    public void validate(UserDTO entity) {
        String message="";
        if(entity.lastName().isEmpty())
            message+="Last name can't be left empty!\n";
        if(entity.firstName().isEmpty())
            message+="First name can't be left empty!\n";
        if(!entity.lastName().matches("[a-zA-Z]+"))
            message+="Last name must be made only out of letters\n";
        if(!entity.firstName().matches("[a-zA-Z]+"))
            message+="First name must be made only out of letters\n";
        if(!message.isEmpty())
            throw new ValidationException(message);
    }
}
