package com.management.leaderspace.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class DesignationForm {
    private Map<UUID, Integer> serviceQuantities = new HashMap<>();
}
