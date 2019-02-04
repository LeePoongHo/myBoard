package net.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.board.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{
	
}
