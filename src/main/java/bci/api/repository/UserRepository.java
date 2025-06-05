package bci.api.repository;

import bci.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository // Marca esta interfaz como un componente de repositorio de Spring
public interface UserRepository extends JpaRepository<UserEntity, UUID> { // <-- JpaRepository para UserEntity con ID de tipo UUID
    // Método para buscar un usuario por su email
    Optional<UserEntity> findByEmail(String email);
}