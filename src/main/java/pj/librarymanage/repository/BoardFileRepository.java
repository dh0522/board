package pj.librarymanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pj.librarymanage.entity.BoardFileEntity;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity,Long> {
}
