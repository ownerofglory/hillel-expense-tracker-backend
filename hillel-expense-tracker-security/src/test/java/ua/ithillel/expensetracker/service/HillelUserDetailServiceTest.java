package ua.ithillel.expensetracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.UserRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class HillelUserDetailServiceTest {
    private HillelUserDetailService hillelUserDetailService;

    @Mock
    private UserRepo userRepoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hillelUserDetailService = new HillelUserDetailService(userRepoMock);
    }

    @Test
    void loadUserByUsernameTest() throws ExpenseTrackerPersistingException {
        when(userRepoMock.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        UserDetails userDetails = hillelUserDetailService.loadUserByUsername("test");

        assertNotNull(userDetails);
    }
}
