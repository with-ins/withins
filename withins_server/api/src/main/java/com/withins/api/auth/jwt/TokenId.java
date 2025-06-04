package com.withins.api.auth.jwt;


public record TokenId(String provider,
                      String providerId
) { }