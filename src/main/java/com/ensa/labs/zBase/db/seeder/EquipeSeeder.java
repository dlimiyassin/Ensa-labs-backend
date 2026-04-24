package com.ensa.labs.zBase.db.seeder;

import com.ensa.labs.recherche.bean.AxeRecherche;
import com.ensa.labs.recherche.bean.Equipe;
import com.ensa.labs.recherche.bean.Lab;
import com.ensa.labs.recherche.bean.Member;
import com.ensa.labs.recherche.dao.AxeRechercheRepository;
import com.ensa.labs.recherche.dao.EquipeRepository;
import com.ensa.labs.recherche.dao.MemberRepository;
import com.ensa.labs.zBase.db.data.LabData;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class EquipeSeeder {

    private final EquipeRepository equipeRepository;
    private final MemberRepository memberRepository;
    private final AxeRechercheRepository axeRechercheRepository;

    public EquipeSeeder(EquipeRepository equipeRepository, MemberRepository memberRepository, AxeRechercheRepository axeRechercheRepository) {
        this.equipeRepository = equipeRepository;
        this.memberRepository = memberRepository;
        this.axeRechercheRepository = axeRechercheRepository;
    }

    public void seed(Lab lab) {
        LabData.LABS.stream()
                .filter(seed -> seed.acronym().equalsIgnoreCase(lab.getAcronym()))
                .findFirst()
                .ifPresent(seed -> seed.equipes().forEach(equipeSeed -> {
                    Equipe equipe = equipeRepository.findAll().stream()
                            .filter(e -> e.getLab() != null
                                    && lab.getId().equals(e.getLab().getId())
                                    && e.getName().equalsIgnoreCase(equipeSeed.name()))
                            .findFirst()
                            .orElseGet(Equipe::new);

                    equipe.setName(equipeSeed.name());
                    equipe.setLab(lab);

                    Set<Member> members = memberRepository.findAll().stream()
                            .filter(m -> equipeSeed.memberFullNames().stream().anyMatch(fn -> normalize(fn).equals(normalize(m.getFirstName() + " " + m.getLastName()))))
                            .collect(java.util.stream.Collectors.toSet());
                    equipe.setMembers(members);

                    Member responsable = memberRepository.findAll().stream()
                            .filter(m -> normalize(equipeSeed.responsableFullName()).equals(normalize(m.getFirstName() + " " + m.getLastName())))
                            .findFirst()
                            .orElse(null);
                    equipe.setResponsable(responsable);

                    Equipe savedEquipe = equipeRepository.save(equipe);

                    Set<AxeRecherche> axes = new HashSet<>();
                    for (String title : equipeSeed.axesRecherche()) {
                        AxeRecherche axe = axeRechercheRepository.findAll().stream()
                                .filter(a -> a.getEquipe() != null
                                        && savedEquipe.getId().equals(a.getEquipe().getId())
                                        && a.getTitle().equalsIgnoreCase(title))
                                .findFirst()
                                .orElseGet(AxeRecherche::new);
                        axe.setTitle(title);
                        axe.setEquipe(savedEquipe);
                        axe.setLab(null);
                        axes.add(axeRechercheRepository.save(axe));
                    }
                    savedEquipe.setAxesRecherche(axes);
                    equipeRepository.save(savedEquipe);
                }));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
