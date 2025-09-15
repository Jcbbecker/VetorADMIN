package com.brasens.http;

import com.brasens.http.objects.Token;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class NetworkManager {
    private Token token;

    public NetworkManager(Token token) {
        this.token = token;
    }
}
