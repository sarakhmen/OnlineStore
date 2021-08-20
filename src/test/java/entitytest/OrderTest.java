package entitytest;

import model.entity.Order;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OrderTest {
    @Test
    public void shouldReturnCorrectValuesOfUserFields(){
        Order order = new Order();
        order.setId(1);
        order.setName("prod1");
        order.setPrice(1000);
        order.setStatus("Unregistered");

        assertEquals(1, order.getId());
        assertEquals("prod1", order.getName());
        assertEquals(1000, order.getPrice(), 0.001);
        assertEquals("Unregistered", order.getStatus());
    }
}
