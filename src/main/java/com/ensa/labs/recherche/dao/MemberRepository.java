package com.ensa.labs.recherche.dao;

import com.ensa.labs.recherche.bean.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByLabIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(String labId, String firstName, String lastName);
    Optional<Member> findByUserId(String userId);
    List<Member> findByLabAcronym(String acronym);
}
