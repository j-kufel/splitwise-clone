package com.splitwise.service;

import com.splitwise.model.Group;
import com.splitwise.model.User;
import com.splitwise.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserService userService;
    
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }
    
    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group with ID " + id + " not found"));
    }
    
    public Group getGroupByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Group with name " + name + " not found"));
    }
    
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
    
    public void addUserToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID " + groupId));
        User user = userService.getUserById(userId);
        group.getMembers().add(user);
        groupRepository.save(group);
    }
    
    public void removeUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with ID " + groupId));
        User user = userService.getUserById(userId);
        group.getMembers().remove(user);
        groupRepository.save(group);
    }

    public boolean isOwnedByCurrentUser(Group group, User user) {
        return group.getCreatedBy().equals(user);
    }
}
