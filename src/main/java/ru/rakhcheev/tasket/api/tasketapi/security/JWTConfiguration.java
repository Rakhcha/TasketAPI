package ru.rakhcheev.tasket.api.tasketapi.security;

public class JWTConfiguration {
    protected static long EXPIRATION_TIME = 864_000_000;
    protected static String SECRET = "RomZayJcNPuB0mKd";
    protected static String HEADER_STRING = "Authorization";
    protected static String TOKEN_PREFIX = "Bearer ";
}
