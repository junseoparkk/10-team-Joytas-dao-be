package com.example.daobe.lounge.domain.repository;

import com.example.daobe.lounge.domain.Lounge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoungeRepository extends JpaRepository<Lounge, Long> {

    @Query("""
            SELECT l FROM Lounge l
            LEFT JOIN LoungeSharer ls ON l = ls.lounge
            WHERE l.user.id = :userId OR ls.user.id = :userId
            AND ls.status = 'ACTIVE'
            """)
    List<Lounge> findLoungeByUserId(@Param("userId") Long userId);
}
