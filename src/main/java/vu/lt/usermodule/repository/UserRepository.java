package vu.lt.usermodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vu.lt.usermodule.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
