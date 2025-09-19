package com.brasens;

import com.brasens.http.Employees;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class Tank {
    private UUID id;

    private int tank1;
    private int tank2;
    private String name;
    private String key;
    private Employees employee;
}