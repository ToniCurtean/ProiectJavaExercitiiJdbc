package repository.db;

import model.Order;
import org.mariadb.jdbc.Statement;
import repository.OrderRepository;
import repository.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDBRepository implements OrderRepository {

    private JdbcUtils jdbcUtils;

    public OrderDBRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public Order add(Order entity) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("insert into ordertable(cod_u) values (?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1,entity.getUserId());
            preparedStatement.executeUpdate();
            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            if(resultSet.next())
                entity.setId(resultSet.getInt(1));
            else
                throw new RepositoryException("Couldn't insert order in db!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public void delete(Integer id) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("delete from ordertable where cod_o=?")){
            preparedStatement.setInt(1,id);
            int result=preparedStatement.executeUpdate();
            if(result==0)
                throw new RepositoryException("Couldn't delete order in db");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Order entity) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("update ordertable set cod_u=? where cod_o=?")){
            preparedStatement.setInt(1,entity.getUserId());
            preparedStatement.setInt(2,entity.getId());
            int result=preparedStatement.executeUpdate();
            if(result==0)
                throw new RepositoryException("Couldn't update order in db");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Order findOne(Integer id) {
        Connection conn= jdbcUtils.getConnection();
        Order order=null;
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from ordertable where cod_o=?")){
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                order=new Order(resultSet.getInt("cod_o"),resultSet.getInt("cod_u"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        if(order==null)
            throw new RepositoryException("Couldn't find the order with the given id");
        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders=new ArrayList<>();
        Connection conn= jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from ordertable")){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                Order order=new Order(resultSet.getInt("cod_o"),resultSet.getInt("cod_u"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        if(orders.isEmpty())
            throw new RepositoryException("Couldn't find the orders in db");
        return orders;
    }

    @Override
    public List<Order> findAllOrdersForUser(Integer userId) {
        List<Order> orders=new ArrayList<>();
        Connection conn= jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from ordertable where cod_u=?")){
            preparedStatement.setInt(1,userId);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                Order order=new Order(resultSet.getInt("cod_o"),resultSet.getInt("cod_u"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        if(orders.isEmpty())
            throw new RepositoryException("Couldn't find the orders in db");
        return orders;
    }
}
