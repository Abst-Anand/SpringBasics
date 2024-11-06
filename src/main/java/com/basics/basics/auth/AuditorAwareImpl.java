package com.basics.basics.auth;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {

        //get security context
        //get authentication
        //get the principle
        //get the userId and then return it
        return Optional.of(1);
    }
}
