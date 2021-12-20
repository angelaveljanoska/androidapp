package com.example.zivototekrug.utility;

import java.util.HashMap;

public class ActivityDto {
    public String name;
    public String description;
    public boolean priority;
    public boolean repeatable;
    public String date;
    public String status;
    public HashMap<String, String> mlad;
    public String mladId;
    public String creatorId;

    public ActivityDto(String name, String description, boolean priority, boolean repeatable, String date, String status, HashMap<String, String> mlad, String mladId, String creatorId) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.repeatable = repeatable;
        this.date = date;
        this.status = status;
        this.mlad = mlad;
        this.mladId = mladId;
        this.creatorId = creatorId;
    }
}
