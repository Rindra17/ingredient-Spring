package hei.school.ingredient.repository;

import hei.school.ingredient.entity.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

                List<DishIngredient> ingredients = getDishIngredient(dish);

                dish.setCompositions(ingredients);
                dishes.add(dish);
            }
            return dishes;
       }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish findById(Integer id) {
        String sql = """
                select  id, name, dish_type, selling_price
                from dish
                where id = ?
                """;

        try (Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Dish dish = new Dish();
                    dish.setId(rs.getInt("id"));
                    dish.setName(rs.getString("name"));
                    dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                    dish.setSellingPrice(rs.getDouble("selling_price"));

                    List<DishIngredient> ingredients = getDishIngredient(dish);
                    dish.setCompositions(ingredients);

                    return dish;
                }
            }
            return null;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void detachIngredients (int dishId) {
        String deleteSql = "DELETE FROM dish_ingredient WHERE id_dish = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, dishId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void attachIngredients (int dishId, List<Integer> ingIds) {
        String insertSql = """
           insert into dish_ingredient (id_ingredient,id_dish, required_quantity, unit)
           values (?, ?, 1, 'KG')
        """;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
                for (Integer ingId : ingIds) {
                    stmt.setInt(1, ingId);
                    stmt.setInt(2, dishId);
                    stmt.addBatch();
                }
                stmt.executeBatch();
                connection.commit();
            }
            catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DishIngredient> getDishIngredient(Dish dish){
        String sql = """
                select di.id, di.id_dish, di.required_quantity, di.unit, i.id as ing_id, i.name as ing_name, i.price, i.category
                   from dish_ingredient di
                join ingredient i on di.id_ingredient = i.id
                where di.id_dish = ?
                """;
        List<DishIngredient> dishIngredients = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, dish.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DishIngredient dishIngredient = new DishIngredient();
                    dishIngredient.setId(rs.getInt("id")); dishIngredient.setQuantityRequired(rs.getInt("required_quantity"));
                    dishIngredient.setUnit(UnitType.valueOf(rs.getString("unit")));

                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("ing_id"));
                    ing.setName(rs.getString("ing_name"));
                    ing.setPrice(rs.getDouble("price"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));

                    dishIngredient.setDish(dish);
                    dishIngredient.setIngredient(ing);

                    dishIngredients.add(dishIngredient);
                }
            }
            return dishIngredients;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
