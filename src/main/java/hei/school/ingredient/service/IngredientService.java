package hei.school.ingredient.service;

import hei.school.ingredient.entity.Ingredient;
import hei.school.ingredient.entity.StockValue;
import hei.school.ingredient.entity.UnitType;
import hei.school.ingredient.exception.BadRequestException;
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
        List<Ingredient> ingredients = ingredientRepository.findAll();

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
            throw new BadRequestException("Ingredient id " + id + " not found");
        }
        ingredient.setStockMovementList(
                stockMovementRepository.findByIngredientId(ingredient.getId())
        );
        return ingredient;
    }

    public StockValue getStockValueAt (int id, Instant at, UnitType unit){
        Ingredient ingredient = getIngredientById(id);

        return ingredient.getStockValueAt(at, unit);
    }

}
