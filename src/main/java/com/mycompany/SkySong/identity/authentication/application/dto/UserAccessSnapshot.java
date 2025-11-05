package com.mycompany.SkySong.identity.authentication.application.login.dto;

import com.mycompany.SkySong.identity.authentication.domain.AccessVersion;
import com.mycompany.SkySong.identity.authentication.domain.UserRole;
import com.mycompany.SkySong.identity.authentication.domain.UserTag;

import java.util.Set;

public record UserAccessSnapshot(UserTag userTag, AccessVersion accessVersion, Set<UserRole> roles) {
}
