package com.grupoa;

import com.grupoa.domain.*;
import com.grupoa.repository.UserRepository;
import com.grupoa.service.UserService;
import com.querydsl.core.types.Predicate;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {GrupoaApplicationTests.Initializer.class})
public class GrupoaApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @ClassRule
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres")
            .withDatabaseName("grupoa")
            .withUsername("postgres")
            .withPassword("postgres");

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void contextLoads() {
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    @Transactional
    public void testSaveUser(){
        int oldUserListSize = userRepository.findAll().size();

        User savedUser = saveUser();
        int newUserListSize = userRepository.findAll().size();

        Assertions.assertThat(oldUserListSize + 1).isEqualTo(newUserListSize);

        Assertions.assertThat(savedUser.getCpf()).isEqualTo("cpf");
        Assertions.assertThat(savedUser.getEmail()).isEqualTo("email");
        Assertions.assertThat(savedUser.getName()).isEqualTo("name");
        Assertions.assertThat(savedUser.getRa()).isEqualTo(1L);
    }

    @Test
    @Transactional
    public void testUpdateUser() throws Exception {
        User savedUser = saveUser();

        savedUser.setName("name edited");
        User updatedUser = userService.update(1L, savedUser);

        Assertions.assertThat(updatedUser.getRa()).isEqualTo(savedUser.getRa());
        Assertions.assertThat(updatedUser.getName()).isEqualTo("name edited");
    }

    @Test
    @Transactional
    public void testUpdateUserErrorChangeRa() throws Exception {
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage("Cant change ra");
        User savedUser = saveUser();

        savedUser.setRa(2L);

        userService.update(1L, savedUser);
    }

    @Test
    @Transactional
    public void testUpdateUserErrorChangeCpf() throws Exception {
        exceptionRule.expect(Exception.class);
        exceptionRule.expectMessage("Cant change cpf");
        saveUser();

        User newUser = createUser();
        newUser.setCpf("New cpf");
        userService.update(1L, newUser);
    }

    @Test
    @Transactional
    public void testGetUserByRa(){
        User savedUser = saveUser();
        User gettedUser = userService.getByRa(savedUser.getRa()).get();

        Assertions.assertThat(gettedUser.getRa()).isEqualTo(savedUser.getRa());
        Assertions.assertThat(gettedUser.getName()).isEqualTo(savedUser.getName());
        Assertions.assertThat(gettedUser.getEmail()).isEqualTo(savedUser.getEmail());
        Assertions.assertThat(gettedUser.getCpf()).isEqualTo(savedUser.getCpf());
    }

    @Test
    @Transactional
    public void testGetUnknownUser(){
        Optional<User> user = userService.getByRa(-1L);

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    @Transactional
    public void testGetAllUsers(){
        Integer beforeSaveSize = userService.getAll().size();
        saveUser();
        List<User> gettedUser = userService.getAll();

        Assertions.assertThat(gettedUser.size()).isEqualTo(beforeSaveSize + 1);
    }

    @Test
    @Transactional
    public void testDeleteUser(){
        User user = saveUser();
        Integer beforeSaveSize = userService.getAll().size();
        userService.delete(user.getRa());
        List<User> gettedUsers = userService.getAll();

        Assertions.assertThat(gettedUsers.size()).isEqualTo(beforeSaveSize - 1);
    }

    private User saveUser() {
        return userService.create(createUser());
    }
    private User createUser() {
        User user = new User();
        user.setCpf("cpf");
        user.setEmail("email");
        user.setName("name");
        user.setRa(1L);
        return user;
    }

}
