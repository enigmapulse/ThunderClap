package com.enigmapulse.thunderclap.repo;
// this automatically implements all the required CRUD operation for user into database
import java.util.List;
import java.util.Optional;

import com.enigmapulse.thunderclap.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// we pass two parameters here
//class of our entity and type of pur id

//there are two standards here: crud repository and jpa repository
//jpa is a more advanced version of crud offering more features than just create, update, delete etc

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findByUsernameContaining(String partialName);
}
//TLDR Optional return function is modern practice
//Using Optional<AppUser> for the findByUsername method signals that the user
// you’re trying to retrieve might not exist. Instead of returning null when
// no matching AppUser is found, it returns an Optional that can be empty.
// This encourages you to handle the "user not found" case explicitly
// (for example, using methods like isPresent(), orElse(), orElseThrow())
// and helps avoid potential NullPointerExceptions.