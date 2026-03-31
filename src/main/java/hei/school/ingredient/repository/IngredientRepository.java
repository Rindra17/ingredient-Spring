package hei.school.ingredient.repository;

import hei.school.ingredient.entity.CategoryEnum;
import hei.school.ingredient.entity.Ingredient;
import hei.school.ingredient.entity.StockMovement;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientRepository {
    private final DataSource dataSource;

    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Ingredient> findAll() {
        String sql = """
                select id, name, category, price
                from ingredient
                order by id
                """;
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ingredient.setPrice(rs.getDouble("price"));

                    ingredients.add(ingredient);
                }
            return ingredients;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Ingredient findById(int id) {
        String sql = """
                select id, name, category, price
                from ingredient
                where id = ?
                """;

        try(Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(id);
                    ingredient.setName(rs.getString("name"));
                    ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ingredient.setPrice(rs.getDouble("price"));
                    return ingredient;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
