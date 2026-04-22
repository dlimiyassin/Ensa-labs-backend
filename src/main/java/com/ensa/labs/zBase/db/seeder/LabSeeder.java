package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.bean.enums.MemberRoleInLab;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LabSeeder {

    private final LabRepository labRepository;
    private final MemberRepository memberRepository;

    public LabSeeder(LabRepository labRepository, MemberRepository memberRepository) {
        this.labRepository = labRepository;
        this.memberRepository = memberRepository;
    }

    public Lab seed() {
        Member directeur = memberRepository.findAll().stream()
                .filter(m -> m.getRoleInLab() == MemberRoleInLab.DIRECTEUR)
                .toList().getFirst();

        Member directeurAdj = memberRepository.findAll().stream()
                .filter(m -> m.getRoleInLab() == MemberRoleInLab.DIRECTEUR_ADJOINT)
                .toList().getFirst();

        Set<Member> members = memberRepository.findAll().stream()
                .filter(m -> m.getRoleInLab() == MemberRoleInLab.MEMBER)
                .collect(Collectors.toSet());

        return labRepository.findByAcronym(LabData.ACRONYM)
                .orElseGet(() -> {
                    Lab lab = new Lab();
                    lab.setTitleFr(LabData.TITLE_FR);
                    lab.setTitleEn(LabData.TITLE_EN);
                    lab.setAcronym(LabData.ACRONYM);
                    lab.setDirecteur(directeur);
                    lab.setDirecteurAdjoint(directeurAdj);
                    lab.setMembers(members);                    lab.setUniversity("Université Sultan Moulay Slimane");
                    lab.setProgram("Programme de Structuration de la Recherche Scientifique");
                    lab.setEstablishment("ENSA BM");
                    lab.setPhone("212661487420");
                    lab.setEmail("r.allaoui@usms.ma");
                    lab.setAccreditationStart(LocalDate.of(2026, 1, 1));
                    lab.setAccreditationEnd(LocalDate.of(2029, 12, 31));

                    return labRepository.save(lab);
                });
    }
}