package com.dzasek.springbootcustomauth.dao;

import com.dzasek.springbootcustomauth.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);

}
