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

    List<Qna> findByUser_Username (String username);

    Qna findByUser_UsernameAndQnaId (String username, Long qnaId);

    List<Qna> findByProduct_pId(Long pId);
}
