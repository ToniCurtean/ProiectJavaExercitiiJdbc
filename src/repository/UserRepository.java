package repository;

import model.User;

public interface UserRepository extends Repository<User>{

    User findUserByLastAndFirstName(String lastName,String firstName);
}
