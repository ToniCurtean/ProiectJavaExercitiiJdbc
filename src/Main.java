import console.Console;
import model.User;
import repository.OrderProductRepository;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.UserRepository;
import repository.db.*;
import service.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties=new Properties();
        try{
            properties.load(new FileInputStream("bd.properties"));
        }catch(IOException e){
            System.err.println("Couldn't find properties");
            return;
        }
        JdbcUtils jdbcUtils=new JdbcUtils(properties);
        UserRepository userRepository=new UserDBRepository(jdbcUtils);
        ProductRepository productRepository=new ProductDBRepository(jdbcUtils);
        OrderRepository orderRepository=new OrderDBRepository(jdbcUtils);
        OrderProductRepository orderProductRepository=new OrderProductDBRepository(jdbcUtils);

        ServiceUser serviceUser=new ServiceUser(userRepository);
        ServiceProduct serviceProduct=new ServiceProduct(productRepository);
        ServiceOrder serviceOrder=new ServiceOrder(orderRepository);
        ServiceOrderProduct serviceOrderProduct=new ServiceOrderProduct(orderProductRepository);

        Service service=new Service(serviceUser,serviceProduct,serviceOrder,serviceOrderProduct);

        Console console=new Console(service);
        console.run();
    }

}