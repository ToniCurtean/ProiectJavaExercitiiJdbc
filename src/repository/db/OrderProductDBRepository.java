package repository.db;

import model.OrderProduct;
import model.Product;
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
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("insert into orderproduct(cod_o,cod_p,cantitate_p) values (?,?,?)")){
            preparedStatement.setInt(1,entity.getOrderId());
            preparedStatement.setInt(2,entity.getProductId());
            preparedStatement.setInt(3,entity.getQuantity());
            int res=preparedStatement.executeUpdate();
            if(res==0){
                throw new RepositoryException("Couldn't insert into db the products for orders");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public void delete(Integer id) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("delete from orderproduct where cod_o=?")){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            int res=preparedStatement.executeUpdate();
            if(res==0)
                throw new RepositoryException("Couldn't delete all the products for the order");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void update(OrderProduct entity) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("update orderproduct set cantitate_p=? where cod_o=? and cod_p=?")){
            preparedStatement.setInt(1,entity.getQuantity());
            preparedStatement.setInt(2,entity.getOrderId());
            preparedStatement.setInt(3,entity.getProductId());
            int res=preparedStatement.executeUpdate();
            if(res==0){
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
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("delete from orderproduct where cod_o=? and cod_p=?")){
            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,productId);
            preparedStatement.executeUpdate();
            int res=preparedStatement.executeUpdate();
            if(res==0)
                throw new RepositoryException("Couldn't delete the product for the order");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
