package repository.db;

import model.*;
import repository.OrderProductRepository;
import repository.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderProductDBRepository implements OrderProductRepository {

    private JdbcUtils jdbcUtils;

    public OrderProductDBRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public OrderProduct add(OrderProduct entity) {
        Connection conn = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("insert into orderproduct(cod_o,cod_p,cantitate_p) values (?,?,?)")) {
            preparedStatement.setInt(1, entity.getOrderId());
            preparedStatement.setInt(2, entity.getProductId());
            preparedStatement.setInt(3, entity.getQuantity());
            int res = preparedStatement.executeUpdate();
            if (res == 0) {
                throw new RepositoryException("Couldn't insert into db the products for orders");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public void delete(Integer id) {
        Connection conn = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("delete from orderproduct where cod_o=?")) {
            preparedStatement.setInt(1, id);
            int res = preparedStatement.executeUpdate();
            if (res == 0)
                throw new RepositoryException("Couldn't delete all the products for the order");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(OrderProduct entity) {
        Connection conn = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("update orderproduct set cantitate_p=? where cod_o=? and cod_p=?")) {
            preparedStatement.setInt(1, entity.getQuantity());
            preparedStatement.setInt(2, entity.getOrderId());
            preparedStatement.setInt(3, entity.getProductId());
            int res = preparedStatement.executeUpdate();
            if (res == 0) {
                throw new RepositoryException("Couldn't update");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public OrderProduct findOne(Integer id) {
        return null;
    }

    @Override
    public List<OrderProduct> findAll() {
        return null;
    }

    @Override
    public void deleteProductsForOrder(Integer orderId, Integer productId) {
        Connection conn = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("delete from orderproduct where cod_o=? and cod_p=?")) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();
            int res = preparedStatement.executeUpdate();
            if (res == 0)
                throw new RepositoryException("Couldn't delete the product for the order");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteOrderForProduct(Integer productId) {
        Connection conn = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("delete from orderproduct where cod_p=?")) {
            preparedStatement.setInt(1, productId);
            preparedStatement.executeUpdate();
            int res = preparedStatement.executeUpdate();
            if (res == 0)
                throw new RepositoryException("Couldn't delete the order for the product");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Product> getProductsForOrder(Integer orderId){
        List<Product> products=new ArrayList<>();
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("select p.cod_p,p.nume_p,p.pret,op.cantitate_p,p.categorie from product as p inner join orderproduct as op on p.cod_p=op.cod_p where op.cod_o=?")){
            preparedStatement.setInt(1,orderId);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product = switch (ProductCategory.valueOf(resultSet.getString("p.categorie"))) {
                    case ELECTRONICS ->
                            new ElectronicProduct(resultSet.getInt("p.cod_p"), resultSet.getString("p.nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("p.pret"))), resultSet.getInt("op.cantitate_p"));
                    case FOOD ->
                            new FoodProduct(resultSet.getInt("p.cod_p"), resultSet.getString("p.nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("p.pret"))), resultSet.getInt("op.cantitate_p"));
                    case CLEANING ->
                            new CleaningProduct(resultSet.getInt("p.cod_p"), resultSet.getString("p.nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("p.pret"))),resultSet.getInt("op.cantitate_p"));
                };
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(products.isEmpty())
            throw new RepositoryException("Couldn't get products for order");
        return products;
    }

}
