package com.doryapp.backend;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Lorenzo Toso on 30.08.2016.
 */
public class FirebaseAuthenticator implements Authenticator {
    @Override
    public User authenticate(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        String token2 = httpServletRequest.getParameter("oauth_token");
        if (token == null)
            return null;

        Task<FirebaseToken> verificationTask = FirebaseAuth.getInstance().verifyIdToken(token);

        FirebaseToken verifiedToken = null;
        try {
            verifiedToken = Tasks.await(verificationTask);
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
        return new User(verifiedToken.getUid(), verifiedToken.getEmail());
    }
}
