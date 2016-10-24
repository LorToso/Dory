package com.doryapp.dory.activities;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Lorenzo Toso on 24.10.2016.
 */
public interface FirebaseUserProvider {
    FirebaseUser getUser();
}
