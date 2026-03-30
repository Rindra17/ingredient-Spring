package hei.school.ingredient.entity;

import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    private int id;
    private String name;
    private double price;
    private CategoryEnum category;
    private List<StockMovement> stockMovementList;

    public Ingredient() {
        this.stockMovementList =new ArrayList<>();
    }

    public Ingredient(int id, String name, double price, CategoryEnum category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockMovementList =new ArrayList<>();
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public List<StockMovement> getStockMouvementList() {
        return stockMovementList;
    }

    public void setStockMouvementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList != null ? stockMovementList : new ArrayList<>();
    }
}
