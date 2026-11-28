package com.ensa.labs.zBase.security.dao.facade;

import com.ensa.labs.zBase.security.bean.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PasswordResetTokenDao extends JpaRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    long countByUserIdAndCreatedAtAfter(String userId, Instant since);

    /** Removes every link of the user (after a successful reset, or when the account is deleted). */
    @Modifying
    @Query("delete from PasswordResetToken t where t.user.id = :userId")
    void deleteByUserId(@Param("userId") String userId);

    /** Housekeeping: links that can no longer be used. */
    @Modifying
    @Query("delete from PasswordResetToken t where t.expiresAt < :now or t.usedAt is not null")
    void deleteUnusable(@Param("now") Instant now);
}
