package com.grupoa.repository;

import com.grupoa.domain.User;
import com.grupoa.domain.QUser;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface UserRepository extends
        JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User>,
        QuerydslBinderCustomizer<QUser> {

    @Override
    default void customize(QuerydslBindings bindings, QUser root) {
        // Binding all Strings.
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }

    Optional<User> findByRa(Long ra);
}
