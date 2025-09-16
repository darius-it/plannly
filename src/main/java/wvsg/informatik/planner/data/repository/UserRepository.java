package wvsg.informatik.planner.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wvsg.informatik.planner.data.entity.UserEntity;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByUsername(String username);
}

