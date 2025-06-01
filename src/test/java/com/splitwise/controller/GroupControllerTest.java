package com.splitwise.controller;

import com.splitwise.model.Group;

import com.splitwise.service.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {
    
    @Mock
    private GroupService groupService;
    
    @InjectMocks
    private GroupController groupController;
    
    
    @Test
    void shouldCreateGroup() {
        Group group = new Group();
        group.setName("Test Group");
        
        when(groupService.createGroup(any(Group.class))).thenReturn(group);
        
        ResponseEntity<Group> response = groupController.createGroup(group);
        
        verify(groupService).createGroup(any(Group.class));
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().getName().equals("Test Group");
    }
    
    @Test
    void shouldDeleteGroup() {
        groupController.deleteGroup(1L);
        verify(groupService).deleteGroup(1L);
    }
    
    @Test
    void shouldGetGroup() {
        Group group = new Group();
        group.setId(1L);
        group.setName("Test Group");
        
        when(groupService.getGroupById(1L)).thenReturn(group);
        
        ResponseEntity<Group> response = groupController.getGroup(1L);
        
        verify(groupService).getGroupById(1L);
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().getName().equals("Test Group");
    }
    
    @Test
    void shouldAddUserToGroup() {
        groupController.addUserToGroup(1L, 2L);
        verify(groupService).addUserToGroup(1L, 2L);
    }
    
    @Test
    void shouldRemoveUserFromGroup() {
        groupController.removeUserFromGroup(1L, 2L);
        verify(groupService).removeUserFromGroup(1L, 2L);
    }
    
    @Test
    void shouldGetGroupByName() {
        Group group = new Group();
        group.setName("Test Group");
        
        when(groupService.getGroupByName("Test Group")).thenReturn(group);
        
        ResponseEntity<Group> response = groupController.getGroupByName("Test Group");
        
        verify(groupService).getGroupByName("Test Group");
        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().getName().equals("Test Group");
    }
}
