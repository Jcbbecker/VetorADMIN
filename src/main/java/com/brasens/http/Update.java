package com.brasens.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Update {

    String version = "";
    String URL = "";

    @Override
    public String toString() {
        return "Update{" +
                "version='" + version + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
