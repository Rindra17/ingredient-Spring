package hei.school.ingredient.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    private Integer id;
    private String name;
    private double price;
    private CategoryEnum category;
    private List<StockMovement> stockMovementList;

    public Ingredient() {
        this.stockMovementList =new ArrayList<>();
    }

    public Ingredient(Integer id, String name, double price, CategoryEnum category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockMovementList =new ArrayList<>();
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

    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList != null ? stockMovementList : new ArrayList<>();
    }

    public StockValue getStockValueAt (Instant t, UnitType unit) {
        double total = 0;

        for (StockMovement stockMovement : stockMovementList) {
            if (!stockMovement.getCreationDatetime().isAfter(t)) {
                if(stockMovement.getType() == MouvementType.IN
                        && stockMovement.getStockValue().getUnit().equals(unit)) {
                    total += stockMovement.getStockValue().getQuantity();
                }
                else if (stockMovement.getType() == MouvementType.OUT
                        && stockMovement.getStockValue().getUnit().equals(unit)) {
                   total -= stockMovement.getStockValue().getQuantity();
                }
            }
        }
        return new StockValue(total, unit);
    }
}
