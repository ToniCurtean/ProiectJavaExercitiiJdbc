package service;

import dto.DTOUtils;
import dto.UserDTO;
import model.User;
import model.UserRole;
import repository.UserRepository;
import repository.exceptions.RepositoryException;
import service.exceptions.ServiceException;
import service.validator.Validator;

import java.util.List;

public class ServiceUser {

    private final UserRepository userRepository;

    private final Validator<UserDTO> validator;

    public ServiceUser(UserRepository userRepository, Validator<UserDTO> validatorUser) {
        this.userRepository = userRepository;
        this.validator=validatorUser;
    }

    public void add(UserDTO userDTO){
        validator.validate(userDTO);
        User user;
        try{
            this.userRepository.findUserByLastAndFirstName(userDTO.lastName(), userDTO.firstName());
            throw new ServiceException("The user is already registered!");
        }catch(RepositoryException ex){
            user= DTOUtils.getUserFromDTO(userDTO);
            try{
                this.userRepository.add(user);
            }catch(RepositoryException e){
                System.err.println(e.getMessage());
            }
        }
    }

    public void delete(Integer id){
        try{
            this.userRepository.delete(id);
        }catch(RepositoryException e){
            System.err.println(e.getMessage());
        }
    }

    public void update(UserDTO dto){
        User user=DTOUtils.getUserFromDTO(dto);
        try{
            this.userRepository.update(user);
        }catch (RepositoryException e){
            System.err.println(e.getMessage());
        }
    }

    public UserDTO findOne(Integer id){
        try{
            return DTOUtils.getUserDTO(this.userRepository.findOne(id));
        }catch (RepositoryException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<UserDTO> findAll(){
        try{
            List<User> users=this.userRepository.findAll();
            return users.stream().map(DTOUtils::getUserDTO).toList();
        }catch (RepositoryException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    public UserDTO findByLastAndFirstName(String lastName,String firstName){
        try{
            return DTOUtils.getUserDTO(this.userRepository.findUserByLastAndFirstName(lastName,firstName));
        }catch(RepositoryException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    public UserDTO loginUser(String lastName,String firstName) {
        try{
            UserDTO userDto=DTOUtils.getUserDTO(this.userRepository.findUserByLastAndFirstName(lastName,firstName));
            if(userDto.role()== UserRole.ADMIN)
                throw new ServiceException("Can't login with these credentials!");
            return userDto;
        }catch(RepositoryException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    public UserDTO loginAdmin(String lastName, String firstName) {
        try{
            UserDTO userDto=DTOUtils.getUserDTO(this.userRepository.findUserByLastAndFirstName(lastName,firstName));
            if(userDto.role()== UserRole.REGULAR)
                throw new ServiceException("Can't login with these credentials!");
            return userDto;
        }catch(RepositoryException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
}
