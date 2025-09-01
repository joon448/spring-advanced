package org.example.expert.domain.manager.repository;

import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ManagerRepositoryTest {
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TodoRepository todoRepository;

    @Test
    void TodoID로_매니저_조회_시_리스트_반환() {
        // given
        User owner = userRepository.save(new User("owner@test.com", "pw", UserRole.USER));
        Todo todo = todoRepository.save(new Todo("title", "contents", "SUN", owner));

        User m1User = userRepository.save(new User("m1@test.com", "pw", UserRole.USER));
        User m2User = userRepository.save(new User("m2@test.com", "pw", UserRole.USER));

        Manager m1 = managerRepository.save(new Manager(m1User, todo));
        Manager m2 = managerRepository.save(new Manager(m2User, todo));

        // when
        List<Manager> managers = managerRepository.findByTodoIdWithUser(todo.getId());

        managers.forEach(m ->
                System.out.printf("m.id=%d, todoId=%d, user=%s%n",
                        m.getId(), m.getTodo().getId(), m.getUser().getEmail())
        );
        // then
        assertEquals(3, managers.size());
        assertTrue(managers.stream().anyMatch(m -> m.getId().equals(owner.getId())
                && m.getUser() != null
                && "owner@test.com".equals(m.getUser().getEmail())));
        assertTrue(managers.stream().anyMatch(m -> m.getId().equals(m1.getId())
                && m.getUser() != null
                && "m1@test.com".equals(m.getUser().getEmail())));
        assertTrue(managers.stream().anyMatch(m -> m.getId().equals(m2.getId())
                && m.getUser() != null
                && "m2@test.com".equals(m.getUser().getEmail())));
    }
}
