package hei.school.ingredient.validator;

import hei.school.ingredient.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class StockMovementValidator {
    public void stockMovementParamValidator(String from, String to) throws BadRequestException {
        StringBuilder msg = new StringBuilder();
        int i = 0;
        if (from == null) {
            msg.append("Query parameter 'from' is required\n");
            i += 1;
        }
        if (to == null) {
            msg.append("Query parameter 'to' is required\n");
            i += 1;
        }
        if (!msg.isEmpty()) {
            if (i == 2) {
                throw new BadRequestException("Either mandatory query parameter `from` or `to` is not provided.");
            }
            else {
                throw new BadRequestException(msg.toString());
            }
        }
    }
}

