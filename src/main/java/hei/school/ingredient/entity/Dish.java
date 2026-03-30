package hei.school.ingredient.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private Double sellingPrice;
    @JsonIgnore
    private List<DishIngredient> compositions;

    public Dish() {
        this.compositions = new ArrayList<>();
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, Double sellingPrice) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.sellingPrice = sellingPrice;
        this.compositions = new ArrayList<>();
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

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @JsonIgnore
    public List<DishIngredient> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<DishIngredient> compositions) {
        this.compositions = compositions;
    }

    @JsonProperty("Ingredients")
    public List<Ingredient> getIngredients() {
        return compositions.stream().map(DishIngredient::getIngredient).toList();
    }
}
