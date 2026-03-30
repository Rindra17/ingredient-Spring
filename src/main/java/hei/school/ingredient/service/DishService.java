package hei.school.ingredient.service;

import hei.school.ingredient.entity.Dish;
import hei.school.ingredient.exception.NotFoundException;
import hei.school.ingredient.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    public Dish findById(Integer id) {
        Dish dish = dishRepository.findById(id);
        if (dish == null) {
            throw new NotFoundException("Dish with id " + id + " not found");
        }

        return dish;
    }

    public Dish updateIngredient(int id, List<Integer> ingIds) {
        Dish dish = findById(id);

        List<Integer> validIds = dishRepository.filterIngredient(ingIds);

        dishRepository.updateDishIngredients(dish.getId(), validIds);
        return dish;
    }
}
