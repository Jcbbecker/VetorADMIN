package com.brasens.http.objects;

import lombok.*;

import java.io.Serializable;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode
public class Token implements Serializable {
    private String tokenJWT = "";
    private boolean connected = false;
}
