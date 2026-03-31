package hei.school.ingredient.validator;

import hei.school.ingredient.entity.CreateMovement;
import hei.school.ingredient.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateMovementValidator {
    public void createMovementValidator(List<CreateMovement> movements) throws BadRequestException {
        if (movements.isEmpty()) {
            throw new BadRequestException("The request body (list of movements) is mandatory");
        }
    }
}