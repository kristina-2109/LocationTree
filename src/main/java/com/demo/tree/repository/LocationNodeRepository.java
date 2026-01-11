package com.demo.tree.repository;

import com.demo.tree.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationNodeRepository extends JpaRepository<Location, Long> {
    List<Location> findByParentIdOrderByOrderIndex(Long parentId);

    @Query("SELECT COUNT(l) FROM Location l WHERE l.parent.id = :parentId")
    long countChildren(@Param("parentId") Long parentId);

}
