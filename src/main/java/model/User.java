package model;

public class User extends Entity{
    private String login;
    private String password;
    private String name;
    private String role;
    private boolean blocked;

    public User(String login, String password, String name, String role, boolean blocked) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
        this.blocked = blocked;
    }

    public User(){}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", blocked=" + blocked +
                '}';
    }
}
