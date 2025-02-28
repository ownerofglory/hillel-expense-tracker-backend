package ua.ithillel.expensetracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.expensetracker.dto.UserDTO;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.exception.ServiceException;
import ua.ithillel.expensetracker.mapper.UserMapper;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceDefault implements UserService {
    private final UserMapper userMapper;
    private final UserRepo userRepo;

    @Override
    public List<UserDTO> getAllUser() {
        return List.of();
    }

    @Override
    public UserDTO getUserById(Long id) {
        try {
            Optional<User> user = userRepo.find(id);

            User exisitingUser = user.orElseThrow(() -> new ServiceException("User not found"));

            return userMapper.userToUserDTO(exisitingUser);
        } catch (ExpenseTrackerPersistingException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
