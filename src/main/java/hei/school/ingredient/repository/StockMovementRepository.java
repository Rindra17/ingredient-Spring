package hei.school.ingredient.repository;

import hei.school.ingredient.entity.MouvementType;
import hei.school.ingredient.entity.StockMovement;
import hei.school.ingredient.entity.StockValue;
import hei.school.ingredient.entity.UnitType;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockMovementRepository {
    private final DataSource dataSource;

    public StockMovementRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<StockMovement> findByIngredientId(int ingredientId, Instant from, Instant to) {

        String sql = """
            SELECT id, quantity, unit, type, creation_datetime
            FROM stock_movement
            WHERE id_ingredient = ?
            and creation_datetime >= ?
            and creation_datetime <= ?
            ORDER BY creation_datetime
        """;

        List<StockMovement> movements = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ingredientId);
            stmt.setTimestamp(2, Timestamp.from(from));
            stmt.setTimestamp(3, Timestamp.from(to));
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
