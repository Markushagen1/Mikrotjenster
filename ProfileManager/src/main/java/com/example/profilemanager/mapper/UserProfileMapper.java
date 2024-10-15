package com.example.profilemanager.mapper;

import com.example.profilemanager.Model.UserProfile;
import com.example.profilemanager.eventdriven.UserProfileDTO;

public class UserProfileMapper {
    public static UserProfileDTO toDTO(UserProfile userProfile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(userProfile.getUserId());
        dto.setName(userProfile.getName());
        dto.setAge(userProfile.getAge());
        dto.setBudget(userProfile.getBudget());
        dto.setInterests(userProfile.getInterests());
        dto.setOccupation(userProfile.getOccupation());
        return dto;
    }
}


