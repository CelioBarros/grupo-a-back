package com.grupoa.service;

import com.grupoa.domain.*;
import com.grupoa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getByRa(Long ra) {
        return userRepository.findByRa(ra);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
