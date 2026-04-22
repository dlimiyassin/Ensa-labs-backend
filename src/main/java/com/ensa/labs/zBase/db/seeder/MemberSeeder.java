package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.bean.enums.MemberGrade;
import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.zBase.db.data.LabData;
import com.ensa.labs.zBase.db.data.MemberData;
import com.ensa.labs.zBase.security.bean.Role;
import com.ensa.labs.zBase.security.bean.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MemberSeeder {

    private final MemberRepository memberRepository;
    private final UserSeeder userSeeder;

    public MemberSeeder(MemberRepository memberRepository, UserSeeder userSeeder) {
        this.memberRepository = memberRepository;
        this.userSeeder = userSeeder;
    }

    public Map<String, Member> seed(Map<String, Role> roles) {

        Map<String, Member> members = new HashMap<>();

        for (MemberData data : LabData.MAIN_MEMBERS) {

            // 1️⃣ Always resolve user FIRST (idempotent already)
            User user = userSeeder.createResearcher(data, roles);

            // 2️⃣ Check if a member already exists for this user
            Optional<Member> existing = memberRepository.findByUserId(user.getId());

            Member member;

            if (existing.isPresent()) {
                member = existing.get(); // reuse existing
            } else {
                member = new Member();   // create only if not exists
                member.setUser(user);
            }

            // Update fields (safe for reruns)
            // member.setLab(lab);
            member.setFirstName(data.firstName());
            member.setLastName(data.lastName());
            member.setSpeciality(data.speciality());
            member.setEstablishment(data.establishment());
            member.setGrade(MemberGrade.valueOf(data.grade()));
            member.setRoleInLab(data.memberRoleInLab());

            Member saved = memberRepository.save(member);

            members.put(data.firstName() + data.lastName(), saved);
        }

        return members;
    }
}