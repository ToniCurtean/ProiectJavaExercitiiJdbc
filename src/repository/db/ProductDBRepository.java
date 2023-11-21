package repository.db;

import model.Product;
import model.User;
import repository.ProductRepository;
import repository.exceptions.RepositoryException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDBRepository implements ProductRepository {

    private JdbcUtils jdbcUtils;

    public ProductDBRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public Product add(Product entity) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("insert into product(nume_p,pret,cantitate) values (?,?,?)")){
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setBigDecimal(2, BigDecimal.valueOf(entity.getPrice()));
            preparedStatement.setInt(3,entity.getQuantity());
            preparedStatement.executeUpdate();
            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            if(resultSet.next())
                entity.setId(resultSet.getInt(1));
            else
                throw new RepositoryException("Couldn't insert user in db");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public void delete(Integer id) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("delete from product where cod_p=?")){
            preparedStatement.setInt(1,id);
            int result=preparedStatement.executeUpdate();
            if(result==0)
                throw new RepositoryException("Couldn't delete product in db");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(Product entity) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("update product set nume_p=?,pret=?,cantiate=? where cod_p=?")){
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setBigDecimal(2, BigDecimal.valueOf(entity.getPrice()));
            preparedStatement.setInt(3,entity.getQuantity());
            preparedStatement.setInt(4,entity.getId());
            int result=preparedStatement.executeUpdate();
            if(result==0)
                throw new RepositoryException("Couldn't update product in db");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Product findOne(Integer id) {
        Connection conn=jdbcUtils.getConnection();
        Product product=null;
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from product where cod_p=?")) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                product=new Product(resultSet.getInt("cod_p"),resultSet.getString("nume_p"),Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))),resultSet.getInt("cantitate"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(product==null)
            throw new RepositoryException("Couldn't find the product with the given id!");
        return product;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products=new ArrayList<>();
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from product")){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product=new Product(resultSet.getInt("cod_p"),resultSet.getString("nume_p"),Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))),resultSet.getInt("cantitate"));
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(products.isEmpty())
            throw new RepositoryException("Couldn't find products");
        return products;
    }

    @Override
    public List<Product> getProductsForOrder(Integer orderId) {
        List<Product> products=new ArrayList<>();
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from product as p inner join orderproduct as o on p.cod_p=o.cod_p where o.cod_o=?")){
            preparedStatement.setInt(1,orderId);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product=new Product(resultSet.getInt("cod_p"),resultSet.getString("nume_p"),Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))),resultSet.getInt("cantitate"));
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(products.isEmpty())
            throw new RepositoryException("Coudln't get products for order");
        return products;
    }

}
