package hei.school.ingredient.repository;

import hei.school.ingredient.entity.CategoryEnum;
import hei.school.ingredient.entity.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IngredientRepository {
    private JdbcTemplate jdbcTemplate;

    public IngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Ingredient> findAll() {
        String sql = "select id, name, category, price from ingredient";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getInt("id"));
            ingredient.setName(rs.getString("name"));
            ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
            ingredient.setPrice(rs.getDouble("price"));
            return ingredient;
        });
    }
}
