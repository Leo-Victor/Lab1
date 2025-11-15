package com.poly.servlet;

import com.poly.dao.UserDAO;
import com.poly.dao.UserDAOImpl;
import com.poly.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebServlet({
        "/user/crud/index",
        "/user/crud/edit",
        "/user/crud/create",
        "/user/crud/update",
        "/user/crud/delete",
        "/user/crud/reset"
})
public class UserCRUDServlet extends HttpServlet{
    private UserDAO dao = new UserDAOImpl();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        /*User form = new User();
        try {
            BeanUtils.populate(form, req.getParameterMap());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        String message = "Enter user information";
        String path = req.getServletPath();
        if (path.contains("edit")) {
            String id = req.getPathInfo().substring(1);
            message = "Edit: " + id;
        } else if (path.contains("create")) {
            message = "Create: " + form.getId();
            form = new User();
        } else if (path.contains("update")) {
            message = "Update: " + form.getId();
        } else if (path.contains("delete")) {
            message = "Delete: " + form.getId();
            form = new User();
        } else if (path.contains("reset")) {
            form = new User();
        }
        List<User> list = List.of(new User(), new User(), new User());
        req.setAttribute("message", message);
        req.setAttribute("user", form);
        req.setAttribute("users", list);
        req.getRequestDispatcher("/pages/user-crud.jsp").forward(req, resp);*/
        User form = new User();
        try {
            // Lấy dữ liệu từ form vào đối tượng User
            BeanUtils.populate(form, req.getParameterMap());
        } catch (IllegalAccessException | InvocationTargetException e) {
            // Xử lý lỗi BeanUtils nếu cần
        }

        String message = "Enter user information";
        String path = req.getServletPath();

        // 1. Xử lý logic theo đường dẫn
        if (path.contains("edit")) {
            String id = req.getPathInfo().substring(1);

            // Bổ sung: Truy vấn user theo id [cite: 382]
            form = dao.findById(id);

            message = "Đang chỉnh sửa User: " + id;

        } else if (path.contains("create")) {
            dao.create(form); // Bổ sung: Thêm User mới vào CSDL [cite: 383]
            message = "✅ Đã thêm User mới: " + form.getId();
            form = new User(); // Xóa trắng form

        } else if (path.contains("update")) {
            dao.update(form); // Bổ sung: Cập nhật User vào CSDL [cite: 384]
            message = "✏️ Đã cập nhật User: " + form.getId();
            // Giữ nguyên dữ liệu trên form

        } else if (path.contains("delete")) {
            dao.deleteById(form.getId()); // Bổ sung: Xóa User khỏi CSDL [cite: 385]
            message = "🗑️ Đã xóa User: " + form.getId();
            form = new User(); // Xóa trắng form

        } else if (path.contains("reset")) {
            form = new User();
            message = "Enter user information";
        }

        // 2. Truy vấn danh sách User
        // Thay thế List.of(...) bằng dao.findAll() [cite: 386, 387]
        List<User> list = dao.findAll();

        // 3. Thiết lập thuộc tính và chuyển tiếp (Forward)
        req.setAttribute("message", message);
        req.setAttribute("user", form);
        req.setAttribute("users", list);

        req.getRequestDispatcher("/pages/user-crud.jsp").forward(req, resp);
    }
}
