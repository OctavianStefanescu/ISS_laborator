package domain;

public class User {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Float currency;

    public User(Integer id,  String username, String password, String name, Float currency) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.currency = currency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Float getCurrency() {
        return currency;
    }

    public void setCurrency(Float currency) {
        this.currency = currency;
    }
}
