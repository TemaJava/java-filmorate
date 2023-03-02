package ru.yandex.practicum.filmorate.daoTests.user;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(statements = "DELETE FROM FRIENDSHIP")
@Sql(statements = "DELETE FROM USERS")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTests {
    private final UserRepositoryImpl repository;

    @Test
    void getEmptyUsersList() {
        Assertions.assertEquals(Collections.emptyList(), repository.getAllUsers());
    }

    @Test
    void createUserTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user);
        AssertionsForClassTypes.assertThat(repository.getUserById(user.getId()))
                .hasFieldOrPropertyWithValue("login", "testLogin")
                .hasFieldOrPropertyWithValue("email", "test@mail.com");
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
    }

    @Test
    void updateUserTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user);
        user.setName("UpdatedName");
        user.setEmail("updated@mail.com");
        repository.update(user);
        AssertionsForClassTypes.assertThat(repository.getUserById(user.getId()))
                .hasFieldOrPropertyWithValue("name", "UpdatedName")
                .hasFieldOrPropertyWithValue("email", "updated@mail.com");
    }


    @Test
    void getUserByIdTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user);
        AssertionsForClassTypes.assertThat(repository.getUserById(user.getId()))
                .hasFieldOrPropertyWithValue("email", user.getEmail());
    }

    @Test
    void getAllUsersTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user);
        user.setName("NewName");
        repository.create(user);
        AssertionsForClassTypes.assertThat(repository.getAllUsers()).isNotNull();
        Assertions.assertEquals(2, repository.getAllUsers().size());
    }

    @Test
    void deleteUserByIdTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user);
        repository.deleteUserById(user.getId());
        Assertions.assertEquals(0, repository.getAllUsers().size());
    }

    @Test
    void getUnavailableUserThrowsExceptionTest() {
        AssertionsForClassTypes.assertThatThrownBy(() -> repository.getUserById(1))
                .isInstanceOf(NotFoundException.class);
        AssertionsForClassTypes.assertThatThrownBy(() -> repository.deleteUserById(1))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateUnavailableUserThrowsExceptionTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        AssertionsForClassTypes.assertThatThrownBy(() -> repository.update(user))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addFriendshipTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user);
        User user2 = User.builder()
                .email("test2@mail.com").login("testLogin2").name("testName2")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user2);
        Assertions.assertEquals(Collections.emptyList(), (repository.getUserFriendsById(user.getId())));
        repository.addFriend(user.getId(), user2.getId());
        Assertions.assertEquals(1, repository.getUserFriendsById(user.getId()).size());
    }

    @Test
    void removeFriendshipTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user);
        User user2 = User.builder()
                .email("test2@mail.com").login("testLogin2").name("testName2")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user2);
        repository.addFriend(user.getId(), user2.getId());
        Assertions.assertEquals(1, repository.getUserFriendsById(user.getId()).size());
        repository.deleteFriend(user.getId(), user2.getId());
        Assertions.assertEquals(0, repository.getUserFriendsById(user.getId()).size());
    }

    @Test
    void getCommonFriendsTest() {
        User user = User.builder()
                .email("test@mail.com").login("testLogin").name("testName")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user);
        User user2 = User.builder()
                .email("test2@mail.com").login("testLogin2").name("testName2")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user2);
        User user3 = User.builder()
                .email("test3@mail.com").login("testLogin3").name("testName3")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        repository.create(user3);
        repository.addFriend(user.getId(), user2.getId());
        repository.addFriend(user2.getId(), user3.getId());
        repository.addFriend(user.getId(), user3.getId());
        Assertions.assertEquals(2, repository.getUserFriendsById(user.getId()).size());
        Assertions.assertEquals(1, repository.getCommonFriends(user.getId(), user2.getId()).size());

    }

}
