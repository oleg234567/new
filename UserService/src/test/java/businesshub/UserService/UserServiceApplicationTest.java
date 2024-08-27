package businesshub.UserService;

import business.hub.userservice.DTO.UserDTO;
import business.hub.userservice.model.ConfirmationCode;
import business.hub.userservice.model.Ticket;
import business.hub.userservice.model.TicketStatus;
import business.hub.userservice.model.User;
import business.hub.userservice.repositories.ConfirmationCodeRepositories;
import business.hub.userservice.repositories.RoleRepositories;
import business.hub.userservice.repositories.TicketRepositories;
import business.hub.userservice.repositories.UserRepositories;
import business.hub.userservice.service.TicketServicesImpl;
import business.hub.userservice.service.UserServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserServicesImpl.class)
class UserServiceApplicationTest {

    @MockBean
    private UserRepositories userRepositories;

    @MockBean
    private TicketRepositories ticketRepositories;

    @MockBean
    private TicketServicesImpl ticketServices;

    @MockBean
    private ConfirmationCodeRepositories confirmationCodeRepositories;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockBean
    private RoleRepositories roleRepositories;

    @Autowired
    private UserServicesImpl userServices;

    @MockBean
    private StreamBridge streamBridge;
    private User user;
    private Ticket ticket;

    private ConfirmationCode confirmationCode;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1);
        user.setEmail("kek@Kekov");
        user.setProfileStatus("200");
        user.setPassword("777");
        user.setRoles(new ArrayList<>());
        user.setFirstName("Utka");
        user.setLastName("Guse");
        user.setUsername("Utkativ");

        ticket = new Ticket();
        ticket.setDescription("trylala");
        ticket.setId(1L);
        ticket.setTicketStatus(TicketStatus.CREATED);
        ticketServices = new TicketServicesImpl(ticketRepositories, kafkaTemplate);

        confirmationCode = new ConfirmationCode();
        confirmationCode.setUser(user);
        confirmationCode.setCode("GGGGGG");
        confirmationCode.setExpiryDate(LocalDateTime.now().plusHours(1));
    }


    @Test
    void getAllUsersTest() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("userTest1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("userTest2");
        List<User> userList = new ArrayList<>(List.of(user1, user2));
        when(userRepositories.findAll()).thenReturn(userList);

        List<User> resList = userServices.getAllUsers();

        assertEquals(userList, resList);
        assertThat(resList, containsInAnyOrder(user1, user2));
        verify(userRepositories).findAll();
    }

    @Test
    void getUserTest() {

        when(userRepositories.findById(user.getId())).thenReturn(Optional.of(user));

        User newUser = userServices.getUser(user.getId());

        assertEquals(user, newUser);

    }

    @Test
    void saveUserTest() {
        userServices.saveUser(user);
        verify(userRepositories, times(1)).save(user);

    }

    @Test
    void deleteUserTest() {
        when(userRepositories.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepositories.existsById(user.getId())).thenReturn(true);

        userServices.deleteUser(user.getId());

        verify(userRepositories, times(1)).deleteById(user.getId());
    }

    @Test
    void updateProfileStatusTest() {

        when(userRepositories.findById(1)).thenReturn(Optional.of(user));

        userServices.updateProfileStatus(1, "CREATED");

        verify(userRepositories).findById(1);
        verify(userRepositories).save(any(User.class));
    }

    @Test
    void handleProfileCreationEventTest() {

        when(userRepositories.findById(1)).thenReturn(Optional.of(user));

        userServices.handleProfileCreationEvent(1, true);

        verify(userRepositories).findById(1);
    }


    @Test
    void getTicketTest() {

        when(ticketRepositories.findById(Math.toIntExact(ticket.getId()))).thenReturn(Optional.of(ticket));

        Ticket newTicket = ticketServices.getTicket(Math.toIntExact(ticket.getId()));

        assertEquals(ticket, newTicket);

    }

    @Test
    void getAllTicketsTest() {
        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setDescription("Test1");

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setDescription("Test2");
        List<Ticket> ticketList = new ArrayList<>(List.of(ticket1, ticket2));
        when(ticketRepositories.findAll()).thenReturn(ticketList);

        List<Ticket> resList = ticketServices.getAllTickets();

        assertEquals(ticketList, resList);
        assertThat(resList, containsInAnyOrder(ticket1, ticket2));
        verify(ticketRepositories).findAll();
    }

    @Test
    void saveTicketTest() {
        when(ticketRepositories.save(ticket)).thenReturn(ticket);
        ticketServices.saveTicket(ticket);
        verify(ticketRepositories, times(1)).save(ticket);
    }

    @Test
    void deleteTicketTest() {
        when(ticketRepositories.findById(Math.toIntExact(ticket.getId()))).thenReturn(Optional.of(ticket));
        when(ticketRepositories.existsById(Math.toIntExact(ticket.getId()))).thenReturn(true);

        ticketServices.deleteTicket(Math.toIntExact(ticket.getId()));

        verify(ticketRepositories, times(1)).deleteById(Math.toIntExact(ticket.getId()));
    }

    @Test
    void updateTicketStatusTest() {

        when(ticketRepositories.findById(1)).thenReturn(Optional.of(ticket));

        ticketServices.updateTicketStatus(1, TicketStatus.valueOf("CREATED"));

        verify(ticketRepositories).findById(1);
        verify(ticketRepositories).save(any(Ticket.class));
    }

    @Test
    void testFindByEmail_UserFound() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepositories.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userServices.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testFindByEmail_UserNotFound() {
        String email = "nonexistent@example.com";

        when(userRepositories.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userServices.findByEmail(email);

        assertFalse(result.isPresent());
    }

    @Test
    void testRegisterUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail(userDTO.getEmail());
        savedUser.setEmailVerified(false);

        when(userRepositories.save(any(User.class))).thenReturn(savedUser);
        when(confirmationCodeRepositories.save(any(ConfirmationCode.class))).thenReturn(new ConfirmationCode());

        UserDTO result = userServices.registerUser(userDTO);

        assertEquals(1, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertFalse(result.isEmailVerified());

        verify(kafkaTemplate).send(anyString(), anyString());
    }

    @Test
    void testVerifyUser_CodeValid() {
        String code = "valid_code";
        User user = new User();
        user.setEmailVerified(false);

        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setUser(user);
        confirmationCode.setExpiryDate(LocalDateTime.now().plusHours(1));

        when(confirmationCodeRepositories.findByCode(code)).thenReturn(Optional.of(confirmationCode));

        boolean result = userServices.verifyUser(code);

        assertTrue(result);
        assertTrue(user.isEmailVerified());
        verify(userRepositories).save(user);
    }

    @Test
    void testVerifyUser_CodeExpired() {
        String code = "expired_code";
        ConfirmationCode expiredCode = new ConfirmationCode();
        expiredCode.setExpiryDate(LocalDateTime.now().minusHours(1));

        when(confirmationCodeRepositories.findByCode(code)).thenReturn(Optional.of(expiredCode));

        boolean result = userServices.verifyUser(code);

        assertFalse(result);
        verify(confirmationCodeRepositories).delete(expiredCode);
    }

    @Test
    public void testRenewConfirmationCode_UserExists_ExistingCodeNotExpired() {
        when(userRepositories.findById(1)).thenReturn(Optional.of(user));
        when(confirmationCodeRepositories.findByUser(user)).thenReturn(confirmationCode);

        userServices.renewConfirmationCode(1);

        verify(confirmationCodeRepositories, never()).delete(any());
        verify(confirmationCodeRepositories, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    public void testRenewConfirmationCode_UserExists_ExistingCodeExpired() {
        confirmationCode.setExpiryDate(LocalDateTime.now().minusHours(1));
        when(userRepositories.findById(1)).thenReturn(Optional.of(user));
        when(confirmationCodeRepositories.findByUser(user)).thenReturn(confirmationCode);

        userServices.renewConfirmationCode(1);

        verify(confirmationCodeRepositories).delete(confirmationCode);
        verify(confirmationCodeRepositories).save(any());
        verify(kafkaTemplate).send(anyString(), anyString());
    }

    @Test
    public void testRenewConfirmationCode_UserDoesNotExist() {
        when(userRepositories.findById(1)).thenReturn(Optional.empty());

        userServices.renewConfirmationCode(1);

        verify(confirmationCodeRepositories, never()).delete(any());
        verify(confirmationCodeRepositories, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    public void testRenewConfirmationCode_NoExistingCode() {
        when(userRepositories.findById(1)).thenReturn(Optional.of(user));
        when(confirmationCodeRepositories.findByUser(user)).thenReturn(null);

        userServices.renewConfirmationCode(1);

        verify(confirmationCodeRepositories).save(any());
        verify(kafkaTemplate).send(anyString(), anyString());
    }
    @Test
    public void contextLoads() {
        assertNotNull(userServices);
    }


}
