package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.ComiteGestionMembre;
import com.ensa.labs.recherche.bean.Department;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.dao.DepartmentRepository;
import com.ensa.labs.recherche.dao.LabRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.zBase.db.data.LabData;
import com.ensa.labs.zBase.db.data.MemberData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LabSeeder {

    private final LabRepository labRepository;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    public LabSeeder(LabRepository labRepository, MemberRepository memberRepository, DepartmentRepository departmentRepository) {
        this.labRepository = labRepository;
        this.memberRepository = memberRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Lab> seed() {
        List<Member> allMembers = memberRepository.findAll();

        return LabData.LABS.stream()
                .map(seed -> upsertLab(seed, allMembers))
                .toList();
    }

    private Lab upsertLab(LabData.LabSeed seed, List<Member> allMembers) {
        Lab lab = labRepository.findByAcronym(seed.acronym()).orElseGet(Lab::new);

        lab.setTitleFr(seed.titleFr());
        lab.setTitleEn(seed.titleEn());
        lab.setAcronym(seed.acronym());
        lab.setUniversity(seed.university());
        lab.setProgram(seed.program());
        lab.setEstablishment(seed.establishment());
        lab.setPhone(seed.phone());
        lab.setEmail(seed.email());
        lab.setAccreditationStart(LocalDate.of(seed.accreditationStartYear(), 1, 1));
        lab.setAccreditationEnd(LocalDate.of(seed.accreditationEndYear(), 12, 31));

        Department department = departmentRepository.findByName(seed.department())
                .orElseGet(() -> {
                    Department d = new Department();
                    d.setName(seed.department());
                    return departmentRepository.save(d);
                });
        lab.setDepartment(department);

        Member directeur = findByFullName(allMembers, seed.directeurFullName());
        Member directeurAdjoint = findByFullName(allMembers, seed.directeurAdjointFullName());
        lab.setDirecteur(directeur);
        lab.setDirecteurAdjoint(directeurAdjoint);

        Set<Member> members = allMembers.stream()
                .filter(m -> seed.acronym().equalsIgnoreCase(resolveMemberLabAcronym(m)))
                .collect(Collectors.toSet());
        lab.setMembers(members);

        List<ComiteGestionMembre> comite = seed.comiteGestion().stream()
                .flatMap(c -> c.roles().stream().map(role -> {
                    ComiteGestionMembre m = new ComiteGestionMembre();
                    m.setNomEnseignant(c.nomEnseignant());
                    m.setRoleComite(role);
                    return m;
                }))
                .toList();
        lab.setComiteGestion(comite);

        Lab savedLab = labRepository.save(lab);

        members.forEach(m -> m.setLab(savedLab));
        memberRepository.saveAll(new HashSet<>(members));

        return savedLab;
    }

    private String resolveMemberLabAcronym(Member member) {
        return LabData.MAIN_MEMBERS.stream()
                .filter(d -> d.firstName().equals(member.getFirstName()) && d.lastName().equals(member.getLastName()))
                .map(MemberData::labAcronym)
                .findFirst()
                .orElse("");
    }

    private Member findByFullName(List<Member> members, String fullName) {
        String normalized = normalize(fullName);
        return members.stream()
                .filter(m -> normalize(m.getFirstName() + " " + m.getLastName()).equals(normalized))
                .findFirst()
                .orElse(null);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
