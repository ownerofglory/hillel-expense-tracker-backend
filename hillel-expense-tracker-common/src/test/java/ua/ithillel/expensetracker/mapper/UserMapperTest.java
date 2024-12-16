package ua.ithillel.expensetracker.mapper;

import org.junit.jupiter.api.Test;
import ua.ithillel.expensetracker.dto.UserDTO;
import ua.ithillel.expensetracker.model.User;

import static org.junit.jupiter.api.Assertions.*;
class UserMapperTest {

    @Test
    void userToUserDTO() {
        UserDTO testUserDTO = UserMapper.INSTANCE.userToUserDTO(TestData.USER);

        assertNotNull(testUserDTO);
        assertEquals(TestData.FIRST_NAME, testUserDTO.getFirstname());
        assertEquals(TestData.LAST_NAME, testUserDTO.getLastname());
        assertEquals(TestData.EMAIL, testUserDTO.getEmail());
        assertEquals(TestData.TAGS, testUserDTO.getTags());
    }

    @Test
    void userDTOToUser() {
        User testUser = UserMapper.INSTANCE.userDTOToUser(TestData.USER_DTO);

        assertNotNull(testUser);
        assertEquals(TestData.FIRST_NAME, testUser.getFirstname());
        assertEquals(TestData.LAST_NAME, testUser.getLastname());
        assertEquals(TestData.EMAIL, testUser.getEmail());
        assertEquals(TestData.TAGS, testUser.getTags());
    }
}