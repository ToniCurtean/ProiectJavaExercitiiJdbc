package console;

import dto.ProductDTO;
import dto.UserDTO;
import service.Service;

import java.util.List;
import java.util.Scanner;

public class Console {

    private Service service;

    public Console(Service service) {
        this.service=service;
    }

    public void menu(){
        System.out.println("MENIU:");
        System.out.println("1.Add user: ");
        System.out.println("2.Delete user: ");
        System.out.println("3.Get users: ");
        System.out.println("4.Modify user: ");
        System.out.println("5.Add product: ");
        System.out.println("6.Delete product: ");
        System.out.println("7.Get products: ");
        System.out.println("8.Modify product: ");
        System.out.println("9.Add order: ");
        System.out.println("10.Delete order: ");
        System.out.println("11.Get order products: ");
        System.out.println("12.Delete order products: ");
        System.out.println("13.Delete products for order: ");

    }

    public void run(){
        Scanner scanner=new Scanner(System.in);

        while(true){
            menu();

            System.out.print("Enter your choice (1-13, or 0 to exit): ");
            int choice = scanner.nextInt();

            switch(choice){
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    System.out.println("Enter last name: ");
                    String lastName=scanner.next();
                    System.out.println("Enter first name");
                    String firstName=scanner.next();
                    UserDTO userDTO=new UserDTO(0,lastName,firstName);
                    this.service.getServiceUser().add(userDTO);
                    break;
                case 2:
                    System.out.println("Enter the id of the user you want to delete: ");
                    int id=scanner.nextInt();
                    this.service.getServiceUser().delete(id);
                    break;
                case 3:
                    List<UserDTO> users=this.service.getServiceUser().findAll();
                    for(UserDTO user:users){
                        System.out.println(user.toString());
                    }
                    break;
                case 4:
                    System.out.println("Enter user id of the user you want to modify:");
                    id=scanner.nextInt();
                    System.out.println("Enter new last name: ");
                    lastName=scanner.next();
                    System.out.println("Enter new first name: ");
                    firstName=scanner.next();
                    UserDTO userDTO1=new UserDTO(id,lastName,firstName);
                    this.service.getServiceUser().update(userDTO1);
                    break;
                case 5:
                    System.out.println("Enter the name of the product: ");
                    String productName=scanner.next();
                    System.out.println("Enter the price: ");
                    Double productPrice= scanner.nextDouble();
                    System.out.println("Enter the quantity: ");
                    Integer productQuantity=scanner.nextInt();
                    ProductDTO productDTO=new ProductDTO(0,productName,productPrice,productQuantity);
                    this.service.getServiceProduct().add(productDTO);
                    break;
                case 6:
                    System.out.println("Enter the id of the product you want to delete: ");
                    id=scanner.nextInt();
                    this.service.getServiceProduct().delete(id);
                    break;
                case 7:
                    List<ProductDTO> products=this.service.getServiceProduct().findAll();
                    for(ProductDTO prod:products){
                        System.out.println(prod.toString());
                    }
                    break;
                case 8:
                    System.out.println("Enter product id of the product you want to modify:");
                    id=scanner.nextInt();
                    System.out.println("Enter new name: ");
                    productName=scanner.next();
                    System.out.println("Enter the price: ");
                    productPrice= scanner.nextDouble();
                    System.out.println("Enter the quantity: ");
                    productQuantity=scanner.nextInt();
                    ProductDTO productDTO1=new ProductDTO(id,productName,productPrice,productQuantity);
                    this.service.getServiceProduct().update(productDTO1);
                    break;
            }
        }
    }

}

