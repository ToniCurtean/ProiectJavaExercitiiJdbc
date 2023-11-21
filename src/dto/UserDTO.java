package dto;

import model.ShoppingCart;
import model.UserRole;

public record UserDTO(Integer id, String lastName, String firstName, ShoppingCart shoppingCart, UserRole role) {
    public UserDTO(Integer id,String lastName,String firstName,UserRole role){
        this(id,lastName,firstName,new ShoppingCart(),role);
    }


}
