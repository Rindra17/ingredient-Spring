package hei.school.ingredient.service;

import hei.school.ingredient.entity.*;
import hei.school.ingredient.exception.NotFoundException;
import hei.school.ingredient.repository.IngredientRepository;
import hei.school.ingredient.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final StockMovementRepository stockMovementRepository;

    public IngredientService(IngredientRepository ingredientRepository, StockMovementRepository stockMovementRepository) {
        this.ingredientRepository = ingredientRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(int id){
        Ingredient ingredient = ingredientRepository.findById(id);

        if (ingredient == null){
            throw new NotFoundException("Ingredient id " + id + " not found");
        }
        return ingredient;
    }

    public StockValue getStockValueAt (int id, Instant at, UnitType unit){
        Ingredient ingredient = getIngredientById(id);

        return ingredient.getStockValueAt(at, unit);
    }

    public List<StockMovement> getStockMovement(int id, Instant from, Instant to) {
        return stockMovementRepository.findByIngredientId(id, from, to);
    }

    public List<StockMovement> saveStockMovement(int id, List<CreateMovement> movements) {

        return stockMovementRepository.saveStockMovements(id, movements);
    }

}
