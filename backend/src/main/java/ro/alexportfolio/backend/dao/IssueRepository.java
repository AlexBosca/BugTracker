package ro.alexportfolio.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.alexportfolio.backend.model.Issue;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    Optional<Issue> findIssueByIssueId(String issueId);

    @Transactional
    @Modifying
    void deleteByIssueId(String issueId);
}
