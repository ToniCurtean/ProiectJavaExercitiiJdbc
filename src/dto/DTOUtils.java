package dto;

import model.Order;
import model.OrderProduct;
import model.Product;
import model.User;

public class DTOUtils {
    public static UserDTO getUserDTO(User user){
        return new UserDTO(user.getId(),user.getLastName(),user.getFirstName());
    }

    public static User getUserFromDTO(UserDTO userDTO){
        return new User(userDTO.id(), userDTO.lastName(), userDTO.firstName());
    }

    public static ProductDTO getProductDTO(Product product){
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    public static Product getProductFromDTO(ProductDTO productDTO){
        return new Product(productDTO.id(), productDTO.name(), productDTO.price(), productDTO.quantity());
    }

    public static OrderDTO getOrderDTO(Order order){
        return new OrderDTO(order.getId(), order.getUserId());
    }

    public static Order getOrderFromDTO(OrderDTO orderDTO){
        return new Order(orderDTO.id(), orderDTO.userId());
    }

    public static OrderProductDTO getOrderProductDTO(OrderProduct orderProduct){
        return new OrderProductDTO(orderProduct.getOrderId(), orderProduct.getProductId(), orderProduct.getQuantity());
    }

    public static OrderProduct getOrderProductFromDTO(OrderProductDTO dto){
        return new OrderProduct(dto.orderId(),dto.productId(),dto.quantity());
    }


}
