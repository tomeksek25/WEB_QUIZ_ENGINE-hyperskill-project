package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    @Query(value = "SELECT q FROM Quiz q ORDER BY id")
    Page<Quiz> findById (int id, Pageable pageable);

}
