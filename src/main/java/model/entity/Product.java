package model.entity;

import java.util.Date;
import java.util.Map;

public class Product {
    private int id;
    private String name;
    private int price;
    private Date creationDate;
    private String status;
    private Map<String, String> properties;

    public Product(){}

    public Product(int id, String name, int price, Date creationDate, String status, Map<String, String> parameters) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationDate = creationDate;
        this.status = status;
        this.properties = parameters;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> parameters) {
        this.properties = parameters;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", status='" + status + '\'' +
                ", parameters=" + properties +
                '}';
    }
}
