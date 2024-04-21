package bg.sofia.uni.fmi.mjt.cooking.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {

    @Test
    void testValidateParametersWithNullData() {
        assertThrows(IllegalArgumentException.class, () ->
                Validator.validateParameters(null, null, null),
            "Three null arguments are not legal input data");
    }

}
