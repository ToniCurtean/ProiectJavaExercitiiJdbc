package repository.db;

import model.*;
import org.mariadb.jdbc.Statement;
import repository.ProductRepository;
import repository.Repository;
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
        try(PreparedStatement preparedStatement=conn.prepareStatement("insert into product(nume_p,pret,cantitate,categorie) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setBigDecimal(2, BigDecimal.valueOf(entity.getPrice()));
            preparedStatement.setInt(3,entity.getQuantity());
            preparedStatement.setString(4,entity.getProductCategory().toString());
            preparedStatement.executeUpdate();
            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            if(resultSet.next())
                entity.setId(resultSet.getInt(1));
            else
                throw new RepositoryException("Couldn't insert product in db");
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
        try(PreparedStatement preparedStatement=conn.prepareStatement("update product set nume_p=?,pret=?,cantitate=?,categorie=? where cod_p=?")){
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setBigDecimal(2, BigDecimal.valueOf(entity.getPrice()));
            preparedStatement.setInt(3,entity.getQuantity());
            preparedStatement.setString(4,entity.getProductCategory().toString());
            preparedStatement.setInt(5,entity.getId());
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
                product = switch (ProductCategory.valueOf(resultSet.getString("categorie"))) {
                    case ELECTRONICS ->
                            new ElectronicProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))), resultSet.getInt("cantitate"));
                    case FOOD ->
                            new FoodProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))), resultSet.getInt("cantitate"));
                    case CLEANING ->
                            new CleaningProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))),resultSet.getInt("cantitate"));
                };
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
                Product product = switch (ProductCategory.valueOf(resultSet.getString("categorie"))) {
                    case ELECTRONICS ->
                            new ElectronicProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))), resultSet.getInt("cantitate"));
                    case FOOD ->
                            new FoodProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))), resultSet.getInt("cantitate"));
                    case CLEANING ->
                            new CleaningProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))),resultSet.getInt("cantitate"));
                };
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
    public Product findProductByName(String prodName) {
        Product product=null;
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from product where nume_p=?")){
            preparedStatement.setString(1,prodName);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                product = switch (ProductCategory.valueOf(resultSet.getString("categorie"))) {
                    case ELECTRONICS ->
                            new ElectronicProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))), resultSet.getInt("cantitate"));
                    case FOOD ->
                            new FoodProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))), resultSet.getInt("cantitate"));
                    case CLEANING ->
                            new CleaningProduct(resultSet.getInt("cod_p"), resultSet.getString("nume_p"), Double.valueOf(String.valueOf(resultSet.getBigDecimal("pret"))),resultSet.getInt("cantitate"));
                };
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(product==null)
            throw new RepositoryException("Couldn't find the product by the given name");
        return product;
    }

    @Override
    public void deleteByName(String prodName) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("delete from product where nume_p=?")){
            preparedStatement.setString(1,prodName);
            int result=preparedStatement.executeUpdate();
            if(result==0)
                throw new RepositoryException("Couldn't delete product in db");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}
