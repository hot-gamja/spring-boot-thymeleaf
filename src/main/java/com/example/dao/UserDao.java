package com.example.dao;

import com.example.model.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User Data Access Object
 */
@Repository
public class UserDao {

    private final SqlSessionTemplate sqlSession;

    @Autowired
    public UserDao(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * Find all users
     */
    public List<User> findAll() {
        return sqlSession.selectList("com.example.dao.UserDao.findAll");
    }

    /**
     * Find user by ID
     */
    public User findById(Long id) {
        return sqlSession.selectOne("com.example.dao.UserDao.findById", id);
    }

    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        return sqlSession.selectOne("com.example.dao.UserDao.findByUsername", username);
    }

    /**
     * Insert new user
     */
    public void insert(User user) {
        sqlSession.insert("com.example.dao.UserDao.insert", user);
    }

    /**
     * Update existing user
     */
    public void update(User user) {
        sqlSession.update("com.example.dao.UserDao.update", user);
    }

    /**
     * Delete user by ID
     */
    public void delete(Long id) {
        sqlSession.delete("com.example.dao.UserDao.delete", id);
    }

    /**
     * Count total users
     */
    public long count() {
        return sqlSession.selectOne("com.example.dao.UserDao.count");
    }
}