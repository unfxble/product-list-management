package ru.alexbat.managerapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.alexbat.managerapp.entity.ManagerUser;

import java.util.Optional;

@Repository
public interface ManagerUserRepository extends CrudRepository<ManagerUser, Integer> {

    Optional<ManagerUser> findByUsername(String username);
}
