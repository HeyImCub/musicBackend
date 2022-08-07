package com.example.demo.repository;

import com.example.demo.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{
	


	
	Optional <Session> findByAdminCode(long id);
	Optional <Session> findBySessionCode(long id);
	void deleteBySessionCode(long id);
	
	boolean existsBySessionCode(long code);
	boolean existsByAdminCode(long code);
}
