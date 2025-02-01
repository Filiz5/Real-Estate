package com.team01.realestate.service.user;

import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.enums.RoleType;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.repository.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRole(RoleType roleType){
        Role role = roleRepository.findByEnumRoleEquals(roleType);
        if (role==null){
            throw new ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND);
        }
        return role;
    }



    public List<Role> getAllUserRole() {
        return roleRepository.findAll();
    }
}

