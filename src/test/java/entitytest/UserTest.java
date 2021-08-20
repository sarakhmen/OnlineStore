package entitytest;

import model.entity.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserTest {
    @Test
    public void shouldReturnCorrectValuesOfUserFields(){
        User user = new User();
        user.setId(1);
        user.setLogin("user@gmail.com");
        user.setName("test user");
        user.setPassword("qwer");
        user.setBlocked(false);
        user.setRole("USER");

        assertEquals(1, user.getId());
        assertEquals("user@gmail.com", user.getLogin());
        assertEquals("test user", user.getName());
        assertEquals("qwer", user.getPassword());
        assertFalse(user.isBlocked());
        assertEquals("USER", user.getRole());
    }
}
