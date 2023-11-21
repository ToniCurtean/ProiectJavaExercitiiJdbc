package service;

import dto.DTOUtils;
import dto.UserDTO;
import model.User;
import repository.Repository;
import repository.UserRepository;
import repository.exceptions.RepositoryException;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceUser {

    private UserRepository userRepository;

    public ServiceUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void add(UserDTO userDTO){
        User user= DTOUtils.getUserFromDTO(userDTO);
        try{
            this.userRepository.add(user);
        }catch(RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    public void delete(Integer id){
        try{
            this.userRepository.delete(id);
        }catch(RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    public void update(UserDTO dto){
        User user=DTOUtils.getUserFromDTO(dto);
        try{
            this.userRepository.update(user);
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    public UserDTO findOne(Integer id){
        try{
            return DTOUtils.getUserDTO(this.userRepository.findOne(id));
        }catch (RepositoryException e){
            System.out.println();
            return null;
        }
    }

    public List<UserDTO> findAll(){
        try{
            List<User> users=this.userRepository.findAll();
            List<UserDTO> usersDto= users.stream().map(DTOUtils::getUserDTO).toList();
            return usersDto;
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            return null;
        }
    }


}
