package com.example.emailspamapi.service;

import com.example.emailspamapi.model.User;
import com.example.emailspamapi.model.UserRole;
import com.example.emailspamapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREATE - Создать нового пользователя
    public User createUser(User user) {
        // Проверяем уникальность username и email
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // Хешируем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Устанавливаем роль по умолчанию если не указана
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }

        return userRepository.save(user);
    }

    // READ - Получить всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // READ - Получить пользователя по ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // READ - Получить пользователя по username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // READ - Получить пользователя по email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // UPDATE - Обновить пользователя
    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Проверяем уникальность username если он изменяется
            if (!user.getUsername().equals(userDetails.getUsername()) &&
                    userRepository.existsByUsername(userDetails.getUsername())) {
                throw new RuntimeException("Username already exists: " + userDetails.getUsername());
            }

            // Проверяем уникальность email если он изменяется
            if (!user.getEmail().equals(userDetails.getEmail()) &&
                    userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already exists: " + userDetails.getEmail());
            }

            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());

            // Обновляем пароль только если он предоставлен и не пустой
            if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            // Обновляем роль если предоставлена
            if (userDetails.getRole() != null) {
                user.setRole(userDetails.getRole());
            }

            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    // UPDATE - Обновить только роль пользователя
    public User updateUserRole(Long id, UserRole role) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(role);
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    // DELETE - Удалить пользователя
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Проверить существует ли пользователь по username
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Проверить существует ли пользователь по email
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Получить количество пользователей
    public long getUserCount() {
        return userRepository.count();
    }

    // Получить пользователей по роли
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    // Поиск пользователей по username (частичное совпадение)
    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
}