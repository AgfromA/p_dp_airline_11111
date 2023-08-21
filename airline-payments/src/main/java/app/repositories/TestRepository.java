package app.repositories;


import app.entities.account.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    Test getTestById(Long id);
    Page<Test> findAll(Pageable pageable);

}
