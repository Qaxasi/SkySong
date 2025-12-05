package com.mycompany.skysong.identity.application.authentication.model;

import com.mycompany.skysong.identity.domain.UserRole;
import com.mycompany.skysong.identity.domain.UserTag;

import java.util.Set;

public record UserAccessSnapshot(UserTag userTag, Set<UserRole> roles) {
}
