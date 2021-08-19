package model.entity;

import java.util.Date;
import java.util.Map;

public class ExtendedProduct extends Product{
    private String nameUk;
    private Map<String, String> propertiesUk;

    public ExtendedProduct() {
    }

    public ExtendedProduct(String name, String nameUk, double price, Date creationDate, Map<String, String> properties,
                           Map<String, String> propertiesUk) {
        super(name, price, creationDate, properties);
        this.nameUk = nameUk;
        this.propertiesUk = propertiesUk;
    }

    public String getNameUk() {
        return nameUk;
    }

    public void setNameUk(String nameUk) {
        this.nameUk = nameUk;
    }

    public Map<String, String> getPropertiesUk() {
        return propertiesUk;
    }

    public void setPropertiesUk(Map<String, String> propertiesUk) {
        this.propertiesUk = propertiesUk;
    }

    @Override
    public String toString() {
        return "ExtendedProduct{" +
                super.toString() +
                ", nameUk='" + nameUk + '\'' +
                ", propertiesUk=" + propertiesUk +
                '}';
    }
}
