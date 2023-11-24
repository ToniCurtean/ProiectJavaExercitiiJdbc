package console;

import dto.OrderDTO;
import dto.ProductDTO;
import dto.UserDTO;
import model.ProductCategory;
import model.ShoppingCart;
import model.UserRole;
import model.exception.ShoppingCartException;
import repository.exceptions.RepositoryException;
import service.Service;
import service.exceptions.ServiceException;
import service.exceptions.ValidationException;

import java.util.*;

public class Console {

    private final Service service;

    private UserDTO loggedUser;

    public Console(Service service) {
        this.service = service;
        this.loggedUser = null;
    }

    public void setLoggedUser(UserDTO userDTO) {
        this.loggedUser = userDTO;
    }

    public void menu() {
        System.out.println("MENIU:");
        System.out.println("1.Login as User");
        System.out.println("2.Register as User");
        System.out.println("3.Login as Admin");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            menu();
            System.out.print("Enter your choice (1-3, or 0 to exit): ");
            int choice;
            try{
                choice=scanner.nextInt();
            }catch(InputMismatchException ex){
                System.err.println("Please enter the number of an option");
                scanner.next();
                continue;
            }
            switch (choice) {
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    System.out.println("Enter last name: ");
                    String lastName = scanner.next();
                    System.out.println("Enter first name: ");
                    String firstName = scanner.next();
                    UserDTO userDTO;
                    try {
                        userDTO = this.service.getServiceUser().loginUser(lastName, firstName);
                    } catch (ServiceException ex) {
                        System.err.println(ex.getMessage());
                        break;
                    }
                    if (userDTO != null) {
                        this.setLoggedUser(userDTO);
                        Scanner userScanner = new Scanner(System.in);
                        boolean userLogged = true;
                        while (userLogged) {
                            userMenu();
                            int option;
                            try{
                                option=userScanner.nextInt();
                            }catch(InputMismatchException ex){
                                System.err.println("Please enter the number of an option");
                                userScanner.next();
                                continue;
                            }
                            switch (option) {
                                case 0:
                                    userLogged = false;
                                    break;
                                case 1:
                                    System.out.println("Enter the name of the product: ");
                                    String productName = userScanner.next();
                                    System.out.println("Enter the quantity: ");
                                    Integer productQuantity;
                                    try {
                                        productQuantity = userScanner.nextInt();
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Please enter a number for quantity!");
                                        break;
                                    }
                                    try {
                                        if (this.service.verifyProduct(productName, productQuantity)) {
                                            loggedUser.shoppingCart().addToShoppingCart(productName, productQuantity);
                                            System.out.println("Added product to the shopping cart!");
                                        }
                                    } catch (ValidationException ex) {
                                        System.err.println(ex.getMessage());
                                    }
                                    break;
                                case 2:
                                    System.out.println("Enter the name of the product you want to delete: ");
                                    productName = userScanner.next();
                                    try {
                                        loggedUser.shoppingCart().removeFromShoppingCart(productName);
                                        System.out.println("The product was deleted from the shopping cart!");
                                    } catch (ShoppingCartException ex) {
                                        System.err.println(ex.getMessage());
                                    }
                                    break;
                                case 3:
                                    System.out.println("Enter the name of the product you want to modify: ");
                                    productName = userScanner.next();
                                    System.out.println("Enter the new quantity: ");
                                    try {
                                        productQuantity = userScanner.nextInt();
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Please enter a number for quantity!");
                                        break;
                                    }
                                    try {
                                        this.service.verifyProduct(productName, productQuantity);
                                        loggedUser.shoppingCart().modifyQuantity(productName, productQuantity);
                                        System.out.println("The quantity was modified for the selected product!");
                                    } catch (ValidationException | ShoppingCartException ex) {
                                        System.err.println(ex.getMessage());
                                    }
                                    break;
                                case 4:
                                    if (loggedUser.shoppingCart().getProducts().isEmpty()) {
                                        System.out.println("Your shopping cart is empty");
                                        break;
                                    }
                                    System.out.println("Cart: ");
                                    loggedUser.shoppingCart().getProducts().forEach((name, quantity) -> System.out.println("Product: " + name + ", Quantity: " + quantity));
                                    Double total = this.service.getTotalForCart(loggedUser.shoppingCart());
                                    loggedUser.shoppingCart().setTotal(total);
                                    System.out.println("Total: " + total);
                                    break;
                                case 5:
                                    if (loggedUser.shoppingCart().getProducts().isEmpty()) {
                                        System.out.println("Your shopping cart is empty!");
                                        break;
                                    }
                                    System.out.println("Are you sure you want to place the order? Answer with YES or NO");
                                    String confirm = userScanner.next();
                                    if (confirm.equals("YES")) {
                                        try {
                                            this.service.addOrder(loggedUser);
                                            loggedUser.shoppingCart().emptyCart();
                                            System.out.println("Order confirmed!");
                                        } catch (RepositoryException ex) {
                                            System.err.println(ex.getMessage());
                                        }
                                    }
                                    break;
                                case 6:
                                    System.out.println("All your orders: ");
                                    List<OrderDTO> orders = this.service.getServiceOrder().findAllOrdersForUser(loggedUser.id());
                                    if (orders.isEmpty()) {
                                        System.out.println("You have no history of orders!");
                                    }
                                    printOrders(orders);
                                    break;
                                case 7:
                                    System.out.println("All products available: ");
                                    List<ProductDTO> products = this.service.getServiceProduct().findAll();
                                    products.stream().filter((product) -> product.quantity() > 0).forEach((product) -> System.out.println("Product name: " + product.name() + " , Price: " + product.price() + " , Available Quantity: " + product.quantity()+" , Categorie: "+product.productCategory().toString()));
                                    break;
                                case 8:
                                    loggedUser.shoppingCart().emptyCart();
                                    System.out.println("Shopping Cart is now empty!");
                                    break;
                                case 9:
                                    if (loggedUser.shoppingCart().getProducts().isEmpty()) {
                                        System.out.println("Your shopping cart is empty!");
                                        break;
                                    }
                                    System.out.println("Most expensive product in shopping cart: ");
                                    System.out.println(this.service.getMostExpensiveProductCart(loggedUser.shoppingCart()));
                                    break;
                                case 10:
                                    if (loggedUser.shoppingCart().getProducts().isEmpty()) {
                                        System.out.println("Your shopping cart is empty!");
                                        break;
                                    }
                                    System.out.println("Least expensive product in shopping cart: ");
                                    System.out.println(this.service.getLeastExpensiveProductCart(loggedUser.shoppingCart()));
                                    break;
                                case 11:
                                    System.out.println("Enter the maximum price: ");
                                    int maxPrice;
                                    try {
                                        maxPrice = userScanner.nextInt();
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Please enter a number for price!");
                                        break;
                                    }
                                    this.service.showProductsForMaxPrice(maxPrice);
                                    break;
                                case 12:
                                    System.out.println("Enter the minimum price: ");
                                    int minPrice;
                                    try {
                                        minPrice = userScanner.nextInt();
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Please enter a number for price!");
                                        break;
                                    }
                                    this.service.showProductsForMinPrice(minPrice);
                                    break;
                                case 13:
                                    System.out.println("Enter the category: ");
                                    String productCategory=userScanner.next().toUpperCase();
                                    if(Arrays.stream(ProductCategory.values()).noneMatch(value->value.toString().equals(productCategory))){
                                        System.out.println("For category please enter Food,Electronics or Cleaning.");
                                        break;
                                    }
                                    this.service.showProductsForCategory(productCategory);
                                    break;
                            }
                        }
                        break;
                    }
                    break;
                case 2:
                    System.out.println("Please enter last name: ");
                    lastName = scanner.next();
                    System.out.println("Please enter first name: ");
                    firstName = scanner.next();
                    try {
                        this.service.getServiceUser().add(new UserDTO(0, lastName, firstName, UserRole.REGULAR));
                        System.out.println("The user was registered!");
                    } catch (ValidationException | ServiceException ex) {
                        System.err.println(ex.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Enter admin last name: ");
                    lastName = scanner.next();
                    System.out.println("Enter admin first name: ");
                    firstName = scanner.next();
                    userDTO = null;
                    try {
                        userDTO = this.service.getServiceUser().loginAdmin(lastName, firstName);
                    } catch (ServiceException ex) {
                        System.err.println(ex.getMessage());
                    }
                    if (userDTO != null) {
                        this.setLoggedUser(userDTO);
                        Scanner adminScanner = new Scanner(System.in);
                        boolean adminLogged = true;
                        while (adminLogged) {
                            adminMenu();
                            int option;
                            try{
                                option=adminScanner.nextInt();
                            }catch(InputMismatchException ex){
                                System.err.println("Please enter the number of an option");
                                adminScanner.next();
                                continue;
                            }
                            switch (option) {
                                case 0:
                                    adminLogged = false;
                                    break;
                                case 1:
                                    System.out.println("Enter the name of the product: ");
                                    String productName = adminScanner.next();
                                    System.out.println("Enter the price: ");
                                    double productPrice;
                                    try {
                                        productPrice = adminScanner.nextDouble();
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Please enter a number for price");
                                        break;
                                    }
                                    System.out.println("Enter the quantity: ");
                                    int productQuantity;
                                    try {
                                        productQuantity = adminScanner.nextInt();
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Please enter a number for quantity");
                                        break;
                                    }
                                    System.out.println("Enter the product category: ");
                                    String productCategory=adminScanner.next().toUpperCase();
                                    if(Arrays.stream(ProductCategory.values()).noneMatch(value->value.toString().equals(productCategory))){
                                        System.out.println("For category please enter Food,Electronics or Cleaning.");
                                        break;
                                    }
                                    ProductDTO productDTO = new ProductDTO(0, productName, productPrice, productQuantity,ProductCategory.valueOf(productCategory));
                                    try{
                                        this.service.getServiceProduct().add(productDTO);
                                    }catch(ValidationException | ServiceException ex){
                                        System.err.println(ex.getMessage());
                                    }
                                    break;
                                case 2:
                                    System.out.println("Enter the name of the product you want to delete: ");
                                    productName = adminScanner.next();
                                    this.service.getServiceProduct().deleteByName(productName);
                                    break;
                                case 3:
                                    System.out.println("Enter the name of the product you want to update: ");
                                    productName = adminScanner.next();
                                    System.out.println("Enter a number for the new quantity: ");
                                    try {
                                        productQuantity = adminScanner.nextInt();
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Please enter a number for quantity!");
                                        break;
                                    }
                                    this.service.updateProductQuantity(productName, productQuantity);
                                case 4:
                                    System.out.println("Enter the name of the product you want to update: ");
                                    productName = adminScanner.next();
                                    System.out.println("Enter a number for the new price: ");
                                    try {
                                        productPrice = adminScanner.nextDouble();
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Please enter a number for quantity!");
                                        break;
                                    }
                                    this.service.updateProductPrice(productName, productPrice);
                                case 5:
                                    System.out.println("All products: ");
                                    List<ProductDTO> products = this.service.getServiceProduct().findAll();
                                    products.forEach((product) -> System.out.println("Product name: " + product.name() + " , Price: " + product.price() + " , Available Quantity: " + product.quantity() + " , Category: "+ product.productCategory()));
                                    break;
                                case 6:
                                    System.out.println("All orders: ");
                                    List<OrderDTO> allOrders = this.service.getServiceOrder().findAll();
                                    if (allOrders != null) {
                                        printOrders(allOrders);
                                        break;
                                    }
                                    System.out.println("No orders have been placed!");
                                    break;
                                case 7:
                                    System.out.println("Enter the category: ");
                                    productCategory=adminScanner.next().toUpperCase();
                                    if(Arrays.stream(ProductCategory.values()).noneMatch(value->value.toString().equals(productCategory))){
                                        System.out.println("For category please enter Food,Electronics or Cleaning.");
                                        break;
                                    }
                                    this.service.showProductsForCategory(productCategory);
                                    break;
                            }
                        }
                        break;
                    }
                    break;
            }

        }

    }

    private void printOrders(List<OrderDTO> orders) {
        orders.forEach((order) -> {
            List<ProductDTO> orderProducts = this.service.getServiceOrderProduct().findAllForOrder(order.id());
            System.out.println("Order id: " + order.id());
            orderProducts.forEach((product) -> System.out.println("Product name: " + product.name() + " , Price: " + product.price() + " , Quantity: " + product.quantity()+ " , Category: " +product.productCategory()));
            System.out.println();
        });
    }

    private void userMenu() {
        System.out.println("Meniu User: ");
        System.out.println("1.Add product to cart");
        System.out.println("2.Delete product from cart");
        System.out.println("3.Modify product from cart");
        System.out.println("4.View cart");
        System.out.println("5.Place order");
        System.out.println("6.View all orders");
        System.out.println("7.View available products");
        System.out.println("8.Empty cart");
        System.out.println("9.Most expensive product from cart");
        System.out.println("10.Least expensive product from cart");
        System.out.println("11.View products below price");
        System.out.println("12.View products over price");
        System.out.println("13.View products for category");
        System.out.println("0.Log out");
    }

    private void adminMenu() {
        System.out.println("Meniu Admin: ");
        System.out.println("1.Add product");
        System.out.println("2.Delete product");
        System.out.println("3.Update quantity for product");
        System.out.println("4.Update price for product");
        System.out.println("5.View all products");
        System.out.println("6.View all orders");
        System.out.println("7.View products for category");
        System.out.println("0.Log out");
    }
}

