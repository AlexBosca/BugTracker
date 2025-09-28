package ro.alexportfolio.backend.util.validator;

import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ro.alexportfolio.backend.dao.UserRepository;
import ro.alexportfolio.backend.model.User;

public class CorrectPasswordValidator implements ConstraintValidator<CorrectPassword, Object> {

    private String keyCheck;
    private String valueCheck;
    private String message;

    private final UserRepository userRepository;

    public CorrectPasswordValidator(final UserRepository userRepositoryParam) {
        this.userRepository = userRepositoryParam;
    }

    @Override
    public void initialize(final CorrectPassword constraintAnnotation) {
        this.keyCheck = constraintAnnotation.keyCheck();
        this.valueCheck = constraintAnnotation.valueCheck();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value,
                           final ConstraintValidatorContext context) {
        String email = (String) new BeanWrapperImpl(value).getPropertyValue(keyCheck);
        String password = (String) new BeanWrapperImpl(value).getPropertyValue(valueCheck);

        User user = userRepository.findByEmail(email).orElse(null);
        
        if(user == null) {
            return true;
        }

        boolean isValid = user.getPassword().equals(password);

        return isValid;
    }
}
