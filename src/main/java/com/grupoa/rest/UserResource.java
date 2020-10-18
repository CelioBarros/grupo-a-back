package com.grupoa.rest;

import com.grupoa.domain.User;
import com.grupoa.service.UserService;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);


    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST  /users : Create a new user.
     *
     * @param user the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user,
     * or with status 400 (Bad Request) if the user has already an RA
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(
            @RequestBody User user) throws URISyntaxException {

        log.debug("REST user to save User : {}", user);
        User result = userService.create(user);
        return ResponseEntity.created(new URI("/api/users/" + result.getRa()))
                .body(result);
    }
    /**
     * GET  /users/:ra : get the "ra" user.
     *
     * @param ra the ra of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user,
     * or with status 404 (Not Found)
     */
    @GetMapping("/users/{ra}")
    @Timed
    public ResponseEntity<User> getUserById(@PathVariable Long ra) {
        log.debug("REST user to get User : {}", ra);
        Optional<User> result = userService.getByRa(ra);
        return (!result.isPresent()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok().body(result.get());
    }

    /**
     * GET  /users : get all users.
     *
     * * @return the ResponseEntity with status 200 (OK) and the list of users in body
     */
    @GetMapping("/users")
    @Timed
    @Transactional(readOnly = true)
    public Page<User> getUsers(
            @QuerydslPredicate(root = User.class) Predicate predicate,
            @ApiParam Pageable pageable) {
        log.debug("REST user to get Users: {}");
        Page<User> result = userService.get(predicate, pageable);
        return result;
    }


    /**
     * GET  /users/list : get all users.
     *
     * @return the users list
     */
    @GetMapping("/users/list")
    @Timed
    @Transactional(readOnly = true)
    public List<User> getUsersList() {
        log.debug("REST user to get Users: {}");
        List<User> result = userService.getAll();
        return result;
    }

    /**
     * DELETE  /users/:ra : delete the "ra" user.
     *
     * @param ra the ra of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{ra}")
    @Transactional
    public ResponseEntity<Void> deleteActivity(@PathVariable Long ra) {
        log.debug("REST request to delete User: {}", ra);
        userService.delete(ra);
        return ResponseEntity.ok().build();
    }

    /**
     * PUT  /users : Updates an existing activity.
     *
     * @param user the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the user is not valid,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @PutMapping("/users")
    @Timed
    public ResponseEntity<User> updateUser(
            @RequestBody User user) {

        log.debug("REST request to update User : {}", user);
        User result = userService.update(user);
        return ResponseEntity.ok()
                .body(result);
    }

}
