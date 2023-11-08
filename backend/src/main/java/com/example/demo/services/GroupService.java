package com.example.demo.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Group;
import com.example.demo.entities.Person;
import com.example.demo.repositories.GroupRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PersonService personService;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Set<Person> getGroupPersons(Long id) {
        return groupRepository.findById(id).orElse(null).getPersons();
    }

    public Group addPersonToGroup(Long id, Long personId) {
        Group group = groupRepository.findById(id).orElse(null);
        if (group != null) {
            group.getPersons().add(personService.getPersonById(personId));
            return groupRepository.save(group);
        } else {
            return null;
        }
    }
}
