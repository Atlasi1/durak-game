package com.example.durak_game;

import java.io.Serializable;

public enum Operation implements Serializable {
    CREATE_SESSION,
    JOIN_SESSION,
    JOIN_SESSION_SUCCESS,
    JOIN_SESSION_FAILED,
    WAITING,
    STARTED
}
