package com.example.demo.repositories;
import com.example.demo.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGroupId(Long groupId);
    Optional<Student> findByUserId(Long userId);
}