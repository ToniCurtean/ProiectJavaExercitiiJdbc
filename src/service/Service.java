package service;

import dto.DTOUtils;
import dto.ProductDTO;
import dto.UserDTO;
import model.Order;
import model.Product;
import model.User;
import repository.*;
import repository.exceptions.RepositoryException;
import service.ServiceOrder;
import service.ServiceOrderProduct;
import service.ServiceProduct;
import service.ServiceUser;

public class Service {
    private ServiceUser serviceUser;
    private ServiceProduct serviceProduct;
    private ServiceOrder serviceOrder;
    private ServiceOrderProduct serviceOrderProduct;

    public Service(ServiceUser serviceUser, ServiceProduct serviceProduct, ServiceOrder serviceOrder, ServiceOrderProduct serviceOrderProduct) {
        this.serviceUser = serviceUser;
        this.serviceProduct = serviceProduct;
        this.serviceOrder = serviceOrder;
        this.serviceOrderProduct = serviceOrderProduct;
    }

    public ServiceUser getServiceUser() {
        return serviceUser;
    }

    public ServiceProduct getServiceProduct() {
        return serviceProduct;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public ServiceOrderProduct getServiceOrderProduct() {
        return serviceOrderProduct;
    }
}
