package entitytest;

import model.entity.ExtendedProduct;
import model.entity.Product;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ExtendedProductTest {
    @Test
    public void shouldReturnCorrectValuesOfExProductFields(){
        ExtendedProduct product = new ExtendedProduct();
        Date creationDate = new Date();
        Map<String, String> propertiesEn = new HashMap<>();
        propertiesEn.put("prop1", "value1");
        propertiesEn.put("prop2", "value2");
        Map<String, String> propertiesUk = new HashMap<>();
        propertiesEn.put("парам1", "знач1");
        propertiesEn.put("парам2", "знач2");

        product.setId(1);
        product.setCreationDate(creationDate);
        product.setName("product");
        product.setNameUk("продукт");
        product.setPrice(1000);
        product.setProperties(propertiesEn);
        product.setPropertiesUk(propertiesUk);

        assertEquals(1, product.getId());
        assertEquals("product", product.getName());
        assertEquals("продукт", product.getNameUk());
        assertEquals(1000, product.getPrice(), 0.001);
        assertEquals(creationDate, product.getCreationDate());
        assertEquals(propertiesEn, product.getProperties());
        assertEquals(propertiesUk, product.getPropertiesUk());
    }
}
