package com.example.demo.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entities.Group;
import com.example.demo.entities.Person;
import com.example.demo.services.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        Assert.notNull(groupService, "GroupService must not be null!");
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<Set<Person>> getPersonsByGroupId(@PathVariable Long id) {
        Set<Person> persons = groupService.getGroupPersons(id);
        return ResponseEntity.ok(persons);
    }
    

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @PostMapping("/{id}/add-person/{personId}")
    public ResponseEntity<Group> addPersonToGroup(@PathVariable Long id, @PathVariable Long personId) {
        Group group = groupService.addPersonToGroup(id, personId);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}