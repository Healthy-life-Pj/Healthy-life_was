package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.qna.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {

    List<Qna> findAll ();

    @Query("SELECT q FROM Qna q WHERE q.user.username = :username AND q.product.pId = :pId")
    List<Qna> findByUserAndProduct(@Param("username") String username, @Param("pId") Long pId);

    List<Qna> findByUser_Username (String username);

    Qna findByUser_UsernameAndQnaId (String username, Long qnaId);

}
