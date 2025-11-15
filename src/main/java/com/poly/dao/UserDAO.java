package com.poly.dao;

import com.poly.entity.User;

import java.util.List;

public interface UserDAO {
    /**Truy vấn tất cả*/
    List<User> findAll();
    /**Truy vấn theo mã*/
    User findById(String id);
    /**Thêm mới*/
    void create(User item);
    /**Cập nhật*/
    void update(User item);
    /**Xóa theo mã*/
    void deleteById(String id);
}
