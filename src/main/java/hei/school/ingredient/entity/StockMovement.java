package hei.school.ingredient.entity;

import java.time.Instant;

public class StockMovement {
    private int id;
    private StockValue stockValue;
    private MouvementType type;
    private Instant creationDatetime;

    public StockMovement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StockValue getStockValue() {
        return stockValue;
    }

    public void setStockValue(StockValue value) {
        this.stockValue = value;
    }


    public MouvementType getType() {
        return type;
    }

    public void setType(MouvementType type) {
        this.type = type;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }
}
