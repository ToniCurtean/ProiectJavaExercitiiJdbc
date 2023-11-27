package teste;

import console.Console;
import dto.ProductDTO;
import dto.UserDTO;
import model.Order;
import model.ProductCategory;
import model.ShoppingCart;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.*;
import repository.db.*;
import service.*;
import service.validator.Validator;
import service.validator.ValidatorProduct;
import service.validator.ValidatorUser;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Provider;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShoppingCartTest {

    static Service service;

    @BeforeAll
    public static void setUp(){
        ServiceProduct serviceProductMock = mock(ServiceProduct.class);
        ServiceUser serviceUserMock=mock(ServiceUser.class);
        ServiceOrder serviceOrderMock=mock(ServiceOrder.class);
        ServiceOrderProduct serviceOrderProductMock=mock(ServiceOrderProduct.class);

        service=new Service(serviceUserMock,serviceProductMock,serviceOrderMock,serviceOrderProductMock);

    }

    @Test
    public void testShoppingCartTotal(){
        ShoppingCart shoppingCart1=new ShoppingCart();
        shoppingCart1.addToShoppingCart("casti",2);
        shoppingCart1.addToShoppingCart("boxe",1);

        ProductDTO productCasti=new ProductDTO(1,"casti",25.00,10, ProductCategory.ELECTRONICS);
        ProductDTO productBoxe=new ProductDTO(2,"boxe",50.00,10,ProductCategory.ELECTRONICS);

        when(service.getServiceProduct().findProductByName("casti")).thenReturn(productCasti);
        when(service.getServiceProduct().findProductByName("boxe")).thenReturn(productBoxe);

        Double rez=service.getTotalForCart(shoppingCart1);
        Double expectedRez=100.0;

        assertEquals(expectedRez,rez);
    }

    @Test
    public void testMostExpensiveProduct(){
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.addToShoppingCart("casti",2);
        shoppingCart.addToShoppingCart("boxe",3);

        ProductDTO productCasti=new ProductDTO(1,"casti",25.00,10, ProductCategory.ELECTRONICS);
        ProductDTO productBoxe=new ProductDTO(2,"boxe",50.00,10,ProductCategory.ELECTRONICS);

        when(service.getServiceProduct().findProductByName("casti")).thenReturn(productCasti);
        when(service.getServiceProduct().findProductByName("boxe")).thenReturn(productBoxe);

        String expectedRez="Product: boxe , Price: 150.0";
        String rez=service.getMostExpensiveProductCart(shoppingCart);

        assertEquals(expectedRez,rez);
    }

    @Test
    public void testLeastExpensiveProduct(){
        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.addToShoppingCart("casti",2);
        shoppingCart.addToShoppingCart("boxe",3);

        ProductDTO productCasti=new ProductDTO(1,"casti",25.00,10, ProductCategory.ELECTRONICS);
        ProductDTO productBoxe=new ProductDTO(2,"boxe",50.00,10,ProductCategory.ELECTRONICS);

        when(service.getServiceProduct().findProductByName("casti")).thenReturn(productCasti);
        when(service.getServiceProduct().findProductByName("boxe")).thenReturn(productBoxe);

        String expectedRez="Product: casti , Price: 50.0";
        String rez=service.getLeastExpensiveProductCart(shoppingCart);

        assertEquals(expectedRez,rez);
    }



}
