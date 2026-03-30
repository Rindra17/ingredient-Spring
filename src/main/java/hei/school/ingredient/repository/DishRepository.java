package hei.school.ingredient.repository;

import hei.school.ingredient.entity.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DishRepository {
    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Dish> findAll(){
        String sql = """
                select id, name, dish_type, selling_price
                from dish
                """;
        List<Dish> dishes = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                Object sellingPrice = rs.getObject("selling_price");
                dish.setSellingPrice(sellingPrice != null ? rs.getDouble("selling_price") : null);

                List<DishIngredient> ingredients = new ArrayList<>();
                ingredients.add(getDishIngredient(dish));

                dish.setCompositions(ingredients);
                dishes.add(dish);
            }
            return dishes;
       }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DishIngredient getDishIngredient(Dish dish){
        String sql = """
                select di.id, di.id_dish, di.required_quantity, di.unit, i.id as ing_id, i.name as ing_name, i.price, i.category
                   from dish_ingredient di
                join ingredient i on di.id_ingredient = i.id
                where di.id_dish = ?
                """;
        DishIngredient dishIngredient = new DishIngredient();

        try(Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, dish.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dishIngredient.setId(rs.getInt("id"));
                    dishIngredient.setQuantityRequired(rs.getInt("required_quantity"));
                    dishIngredient.setUnit(UnitType.valueOf(rs.getString("unit")));

                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("ing_id"));
                    ing.setName(rs.getString("ing_name"));
                    ing.setPrice(rs.getDouble("price"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));

                    dishIngredient.setDish(dish);
                    dishIngredient.setIngredient(ing);
                }
            }
            return dishIngredient;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
