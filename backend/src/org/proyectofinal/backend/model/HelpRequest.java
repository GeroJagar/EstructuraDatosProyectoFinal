package org.proyectofinal.backend.model;

import java.io.Serializable;

public class HelpRequest implements Serializable {
    private SUBJECT subject;
    private int level;
    private User requester;
}
