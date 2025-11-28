package com.mycompany.SkySong.identity.application.dto;

import com.mycompany.skysong.identity.domain.AccessVersion;
import com.mycompany.skysong.identity.domain.UserRole;
import com.mycompany.skysong.identity.domain.UserTag;

import java.util.Set;

public record UserAccessSnapshot(UserTag userTag, AccessVersion accessVersion, Set<UserRole> roles) {
}
