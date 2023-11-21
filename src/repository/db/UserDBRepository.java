package repository.db;

import model.User;
import model.UserRole;
import org.mariadb.jdbc.Statement;
import org.mariadb.jdbc.export.Prepare;
import repository.UserRepository;
import repository.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDBRepository implements UserRepository {

    private JdbcUtils jdbcUtils;

    public UserDBRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }


    @Override
    public User add(User entity) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("insert into user(nume_u,prenume_u,rol) values (?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,entity.getLastName());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setString(3,entity.getRole().toString());
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
        try(PreparedStatement preparedStatement=conn.prepareStatement("delete from user where cod_u=?")) {
            preparedStatement.setInt(1,id);
            int result=preparedStatement.executeUpdate();
            if(result==0){
                throw new RepositoryException("Couldn't delete user with the given id");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("update user set nume_u=?,prenume_u=? where cod_u=?")){
            preparedStatement.setString(1,entity.getLastName());
            preparedStatement.setString(2,entity.getFirstName());
            preparedStatement.setInt(3,entity.getId());
            int result=preparedStatement.executeUpdate();
            if(result==0){
                throw new RepositoryException("Couldn't update the user with the given id");
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public User findOne(Integer id) {
        Connection conn=jdbcUtils.getConnection();
        User user=null;
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from user where cod_u=?")) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                user=new User(resultSet.getInt("cod_u"),resultSet.getString("nume_u"),resultSet.getString("prenume_u"), UserRole.valueOf(resultSet.getString("rol")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(user==null)
            throw new RepositoryException("Couldn't find the user with the given id!");
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users=new ArrayList<>();
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from user")){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                User user=new User(resultSet.getInt("cod_u"),resultSet.getString("nume_u"),resultSet.getString("prenume_u"),UserRole.valueOf(resultSet.getString("rol")));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(users.isEmpty())
            throw new RepositoryException("Couldn't find users");
        return users;
    }

    @Override
    public User findUserByLastAndFirstName(String lastName, String firstName) {
        User user=null;
        Connection conn=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=conn.prepareStatement("select * from user where nume_u=? and prenume_u=?")){
            preparedStatement.setString(1,lastName);
            preparedStatement.setString(2,firstName);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                user=new User(resultSet.getInt("cod_u"),resultSet.getString("nume_u"),resultSet.getString("prenume_u"), UserRole.valueOf(resultSet.getString("rol")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
