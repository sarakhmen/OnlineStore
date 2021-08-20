package entitytest;

import model.entity.Product;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ProductTest {
    @Test
    public void shouldReturnCorrectValuesOfProductFields(){
        Product product = new Product();
        Date creationDate = new Date();
        Map<String, String> properties = new HashMap<>();
        properties.put("prop1", "value1");
        properties.put("prop2", "value2");

        product.setId(1);
        product.setCreationDate(creationDate);
        product.setName("product");
        product.setPrice(1000);
        product.setProperties(properties);

        assertEquals(1, product.getId());
        assertEquals("product", product.getName());
        assertEquals(1000, product.getPrice(), 0.001);
        assertEquals(creationDate, product.getCreationDate());
        assertEquals(properties, product.getProperties());
    }
}
