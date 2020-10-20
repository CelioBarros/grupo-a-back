package com.grupoa.service;

import com.grupoa.domain.*;
import com.grupoa.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<User> get(Predicate predicate, Pageable pageable) {
        Page<User> result = userRepository.findAll(predicate, pageable);
        return result;
    }

    public void delete(Long ra) {
        userRepository.deleteByRa(ra);
    }

    public User update(User user) throws Exception {
        checkCanUpdate(user);
        return userRepository.save(user);
    }

    private void checkCanUpdate(User user) throws Exception {
        User registeredUser = getByRa(user.getRa()).get();
        if (!registeredUser.getRa().equals(user.getRa())) throw new Exception("Cant change ra");
        if (!registeredUser.getCpf().equals(user.getCpf())) throw new Exception("Cant change cpf");
    }
}
