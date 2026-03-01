package com.growthtracker.model;

import lombok.Getter;

@Getter
public enum Priority {
    LOW(0),
    MEDIUM(1),
    HIGH(2),
    URGENT(3);

    private final int weight;

    Priority(int weight) {
        this.weight = weight;
    }
}
