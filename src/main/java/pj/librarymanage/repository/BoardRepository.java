package pj.librarymanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pj.librarymanage.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // update board_table set board_hits = board_hits + 1 where id = ?

    // entity 를 기준으로 퀴리문 작성
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id=:id" )
    void updateHits(@Param("id") Long id); // b.id = id 의 id 가 param id와 같은것



}
