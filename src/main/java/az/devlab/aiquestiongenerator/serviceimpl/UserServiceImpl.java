package az.devlab.aiquestiongenerator.serviceimpl;

import az.devlab.aiquestiongenerator.exception.NotFoundException;
import az.devlab.aiquestiongenerator.model.User;
import az.devlab.aiquestiongenerator.repository.UserRepository;
import az.devlab.aiquestiongenerator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
