package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CompletionRepository extends JpaRepository<Completion, Integer> {
    @Query(value = "SELECT c FROM Completion c WHERE c.user = :user")
    Page<Completion> findAllUsers (@Param("user") String user, Pageable pageable);

}