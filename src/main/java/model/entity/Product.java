package model.entity;

import java.util.Date;
import java.util.Map;

public class Product extends Entity{
    private String name;
    private double price;
    private Date creationDate;
    private Map<String, String> properties;

    public Product(){}

    public Product(String name, double price, Date creationDate, Map<String, String> properties) {
        this.name = name;
        this.price = price;
        this.creationDate = creationDate;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", properties=" + properties +
                '}';
    }
}
