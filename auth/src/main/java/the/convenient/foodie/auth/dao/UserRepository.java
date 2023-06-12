package the.convenient.foodie.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import the.convenient.foodie.auth.entity.Token;
import the.convenient.foodie.auth.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
      Optional<User> findByUsername(String username);
      Optional<User> findByEmail(String email);
      @Query("SELECT u FROM User u WHERE u.role=1")
      public List<User> getAllManagers();

      @Query("SELECT u FROM User u WHERE u.role=2")
      public List<User> getAllCouriers();

      @Query("SELECT u FROM User u WHERE u.uuid=:uuid")
      public Optional<User> findByUUID(String uuid);
}
