package com.goodiebag.adverPizing.networks.rest.models;

import com.squareup.moshi.Json;

/**
 * Created by Kai on 25/02/18.
 */

public class NoticeBoardRespnose {
    @Json(name = "date") String date;
    @Json(name = "title") String title;
    @Json(name = "description") String description;
    @Json(name = "deadline") String deadline;
    @Json(name = "teacher") String teacher;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
