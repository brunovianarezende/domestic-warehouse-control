package org.vaadin.ext.bruno.frontend;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import org.apache.commons.lang3.StringUtils;

public class NotBlankValidator implements Validator<String> {

    private final String errorMessage;

    public NotBlankValidator(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public ValidationResult apply(String value, ValueContext valueContext) {
        if (StringUtils.isBlank(value)) {
            return ValidationResult.error(errorMessage);
        } else {
            return ValidationResult.ok();
        }
    }
}
