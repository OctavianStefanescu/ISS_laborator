package domain;

import javafx.scene.control.CheckBox;

public class GameUI {
    private Integer id;
    private String name;
    private Float price;
    private CheckBox cart;
    private CheckBox wishlist;

    public GameUI(Integer id, String name, Float price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cart = new CheckBox();
        this.wishlist = new CheckBox();
    }

    public CheckBox getCart() {
        return cart;
    }

    public void setCart(CheckBox cart) {
        this.cart = cart;
    }

    public CheckBox getWishlist() {
        return wishlist;
    }

    public void setWishlist(CheckBox wishlist) {
        this.wishlist = wishlist;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
