package com.brasens.http;

import com.brasens.http.objects.Token;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NetworkManager {

    private Token token;
    private User user;
    private Employees selectedEmployee;

    public NetworkManager(Token token) {
        this.token = token;
    }

    public void setTokenJWT(String jwt) {
        if (token == null) {
            token = new Token();
        }
        token.setTokenJWT(jwt);
    }

    public String getTokenJWT() {
        if (token != null) {
            return token.getTokenJWT();
        }
        return null;
    }

    public void clearSession() {
        this.token = new Token();
        this.user = null;
        this.selectedEmployee = null;
    }

    public boolean isAuthenticated() {
        return token != null && token.getTokenJWT() != null && !token.getTokenJWT().isEmpty();
    }
}