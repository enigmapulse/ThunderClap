package com.enigmapulse.thunderclap;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity // means data from its fields would be added to our database
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String username;

    @Setter
    private String password;

    public AppUser() {}

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

//@getter and @setter are annotations from lombok just to simplify writing getter and setter functions
//@getter above the class name just means that all the variables defined inside that class have a getter function
//since we do not want a setter function for id because it is automatically generated as we add users
//thus we have put setters above require fields individually instead of putting it directly on the class
