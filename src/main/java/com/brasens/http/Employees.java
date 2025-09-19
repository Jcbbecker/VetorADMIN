package com.brasens.http;

import com.brasens.Tank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class Employees {
    private UUID id;
    private String login;
    private String email;
    private String password;
    private String key;
    private List<Tank> tanks = new ArrayList<>();
}
