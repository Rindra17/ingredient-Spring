package hei.school.ingredient.repository;

import hei.school.ingredient.entity.MouvementType;
import hei.school.ingredient.entity.StockMovement;
import hei.school.ingredient.entity.StockValue;
import hei.school.ingredient.entity.UnitType;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockMovementRepository {
    private DataSource dataSource;

    public StockMovementRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<StockMovement> findByIngredientId(int ingredientId) {

        String sql = """
            SELECT id, quantity, unit, type, creation_datetime
            FROM stock_movement
            WHERE id_ingredient = ?
            ORDER BY creation_datetime
        """;

        List<StockMovement> movements = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ingredientId);
            try(ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    StockMovement sm = new StockMovement();
                    sm.setId(rs.getInt("id"));

                    StockValue stockValue = new StockValue();
                    stockValue.setQuantity(rs.getDouble("quantity"));
                    stockValue.setUnit(UnitType.valueOf(rs.getString("unit")));
                    sm.setStockValue(stockValue);

                    sm.setType(MouvementType.valueOf(rs.getString("type")));
                    sm.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());

                    movements.add(sm);
                }
            }
            return movements;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
