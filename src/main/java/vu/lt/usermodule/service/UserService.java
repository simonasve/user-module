package vu.lt.usermodule.service;

import com.psp.uzduotis.PasswordChecker;
import com.psp.uzduotis.PhoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vu.lt.usermodule.model.User;
import vu.lt.usermodule.repository.UserRepository;
import com.psp.uzduotis.EmailValidator;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        if(validateEmailAddress(user.getEmailAddress()) &&
            validatePassword(user.getPassword()) &&
            validatePhoneNumber(user.getPhoneNumber())) {
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private boolean validateEmailAddress(String emailAddress) {
        EmailValidator emailValidator = new EmailValidator();

        return emailValidator.findAddressSign(emailAddress) &&
               // emailValidator.hasInvalidSymbol(emailAddress) && This can't be validated correctly because of a fault in the validator
                emailValidator.hasValidDomainName(emailAddress) &&
                !emailValidator.findTLD(emailAddress).equals("TLD Not Found.");
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        PhoneValidator phoneValidator = new PhoneValidator();

        Long correctPhoneNumber = phoneValidator.changeByPrefix(phoneNumber);

        return phoneValidator.correctLength(String.valueOf(correctPhoneNumber)) &&
                phoneValidator.hasOnlyDigits(String.valueOf(correctPhoneNumber));
    }

    private boolean validatePassword(String password) {
        PasswordChecker passwordChecker = new PasswordChecker();

        return passwordChecker.hasSpecialSymbol(password) &&
                passwordChecker.hasUppercase(password) &&
                passwordChecker.minimumLength(10, password);
    }
}
