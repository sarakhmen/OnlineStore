package model.entity;

import java.util.Date;
import java.util.Map;

public class Product {
    private int id;
    private String name;
    private int price;
    private Date creationDate;
    private Map<String, String> properties;

    public Product(){}

    public Product(int id, String name, int price, Date creationDate, Map<String, String> properties) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationDate = creationDate;
        this.properties = properties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", properties=" + properties +
                '}';
    }
}
