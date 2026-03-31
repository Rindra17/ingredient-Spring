package hei.school.ingredient.repository;

import hei.school.ingredient.entity.*;
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
            return getStockMovements(movements, stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockMovement> findByIngredientIdWithouTime(int ingredientId) {

        String sql = """
            SELECT id, quantity, unit, type, creation_datetime
            FROM stock_movement
            WHERE id_ingredient = ?
        """;

        List<StockMovement> movements = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ingredientId);
            return getStockMovements(movements, stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<StockMovement> getStockMovements(List<StockMovement> movements, PreparedStatement stmt) throws SQLException {
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
    }

    public List<StockMovement> saveStockMovements(int ingredientId, List<CreateMovement> movements) {
        if (movements == null || movements.isEmpty()) {
            return new ArrayList<>();
        }

        List<StockMovement> stockMovements = new ArrayList<>();
        String sql = """
                INSERT INTO stock_movement
                (id_ingredient, quantity, unit, type, creation_datetime)
                VALUES (?, ?, ?::unit, ?::movement_type, ?)
                ON CONFLICT (id) DO NOTHING
                returning id
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (CreateMovement mvt : movements) {
                    ps.setInt(1, ingredientId);
                    ps.setDouble(2, mvt.getValue());
                    ps.setString(3, mvt.getUnit().name());
                    ps.setString(4, mvt.getType().name());
                    ps.setTimestamp(5, Timestamp.from(Instant.now()));

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            StockMovement sm = new StockMovement();
                            sm.setId(rs.getInt("id"));
                            StockValue sv = new StockValue(mvt.getValue(), mvt.getUnit());
                            sm.setStockValue(sv);
                            sm.setType(mvt.getType());
                            sm.setCreationDatetime(Instant.now());

                            stockMovements.add(sm);
                        }
                    }
                }
            return stockMovements;
            }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
