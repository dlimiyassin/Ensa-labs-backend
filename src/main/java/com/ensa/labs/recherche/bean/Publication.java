package com.ensa.labs.recherche.bean;

import com.ensa.labs.recherche.bean.enums.PublicationStatus;
import com.ensa.labs.recherche.bean.enums.PublicationType;
import com.ensa.labs.zBase.security.bean.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 2000)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublicationType type;

    private Integer publicationYear;

    @ElementCollection
    @CollectionTable(name = "publication_authors", joinColumns = @JoinColumn(name = "publication_id"))
    @Column(name = "author_name")
    private List<String> authors = new ArrayList<>();

    private String journal;

    private String conference;

    private String doi;

    private String pages;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    /** Web page of the publication (publisher, HAL, ResearchGate...), an alternative to the DOI. */
    @Column(length = 1000)
    private String link;

    /** Author account of the record; researchers may only update their own publications. */
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    /** Review state; null only for rows created before the workflow existed (treated as validated). */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PublicationStatus status;

    /** Reviewer's note: the reason of a rejection, or an optional remark on approval. */
    @Column(length = 2000)
    private String reviewComment;

    /** Account that approved or rejected the record last. */
    @ManyToOne
    @JoinColumn(name = "validated_by_id")
    private User validatedBy;

    private LocalDateTime validatedAt;

    /** Last time the author submitted (created or edited) the record. */
    private LocalDateTime submittedAt;

    public boolean isPublic() {
        return status == null || status == PublicationStatus.VALIDATED;
    }

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Publication that = (Publication) o; return Objects.equals(id, that.id);}    
    @Override
    public int hashCode() { return Objects.hash(id); }
}
