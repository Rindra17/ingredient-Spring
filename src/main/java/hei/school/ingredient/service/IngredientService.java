package hei.school.ingredient.service;

import hei.school.ingredient.entity.Ingredient;
import hei.school.ingredient.entity.StockValue;
import hei.school.ingredient.entity.UnitType;
import hei.school.ingredient.exeptions.IngredientExeption;
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

    public List<Ingredient> getAllIngredients(int page, int size) {
        List<Ingredient> ingredients = ingredientRepository.findAll(page, size);

        for(Ingredient ingredient : ingredients){
            ingredient.setStockMovementList(
                    stockMovementRepository.findByIngredientId(ingredient.getId())
            );
        }
        return ingredients;
    }

    public Ingredient getIngredientById(int id){
        Ingredient ingredient = ingredientRepository.findById(id);

        if (ingredient == null){
            throw new IngredientExeption("Ingredient id " + id + " not found");
        }
        ingredient.setStockMovementList(
                stockMovementRepository.findByIngredientId(ingredient.getId())
        );
        return ingredient;
    }

    public StockValue getStockValueAt (int id, String at, String unit){
        Ingredient ingredient = getIngredientById(id);
        if (at == null || unit == null) {
            throw new IngredientExeption("Either mandatory query parameter `at` or `unit` is not provided.");
        }

        Instant instant = Instant.parse(at);
        UnitType uniteType = UnitType.valueOf(unit);

        return ingredient.getStockValueAt(instant, uniteType);
    }

}
