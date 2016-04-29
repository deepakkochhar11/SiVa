package ee.openeid.siva.webapp.request.validation.validators;

import ee.openeid.siva.webapp.request.validation.annotations.AcceptValues;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class AcceptValuesConstraintValidator implements ConstraintValidator<AcceptValues, String> {

    private List<String> acceptedValues;

    @Override
    public void initialize(AcceptValues values) {
        acceptedValues = new ArrayList<>();
        values.value().getAcceptedValues().forEach(av -> acceptedValues.add(StringUtils.lowerCase(av)));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return acceptedValues.contains(StringUtils.lowerCase(value));
    }
}
