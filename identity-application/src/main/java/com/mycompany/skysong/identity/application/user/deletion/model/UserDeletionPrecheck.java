package com.mycompany.skysong.identity.application.user.deletion.model;

public record UserDeletionPrecheck(boolean userExists, boolean isLastAdmin) {
}
