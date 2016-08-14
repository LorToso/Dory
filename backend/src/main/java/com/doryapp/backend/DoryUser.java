package com.doryapp.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class DoryUser {
    @Id Long id;
    String firstName;
    String lastName;
    //@Load Ref<Location> location;
    Long location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//    public Location getLocation() {
//        return ofy().load().type(Location.class).id(location.getValue().getId()).now();
//    }
//
//    public void setLocation(Location location) {
//        //ofy().save().entity(location).now().
//        //this.location = new Ref<Location>(location);
//    }
    public Long getLocation() {
        return this.location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }
}
