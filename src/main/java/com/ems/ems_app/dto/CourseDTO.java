package com.ems.ems_app.dto;

import lombok.*;

import java.util.UUID;
//@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
   private String code;
    private String name;
    private UUID avatarId;
    private UUID groupId;
    private UUID dirDocId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(UUID avatarId) {
        this.avatarId = avatarId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public UUID getDirDocId() {
        return dirDocId;
    }

    public void setDirDocId(UUID dirDocId) {
        this.dirDocId = dirDocId;
    }
}
