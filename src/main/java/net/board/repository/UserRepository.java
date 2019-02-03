package net.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.board.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
