package com.odde.atddv2.repository;

import com.odde.atddv2.entity.User;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {

    User save(User user);

    Optional<User> findByUserNameAndPassword(String username, String password);


}