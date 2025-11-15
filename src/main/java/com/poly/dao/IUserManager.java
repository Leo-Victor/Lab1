package com.poly.dao;

import com.poly.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class IUserManager {
    // Khởi tạo EntityManagerFactory và EntityManager
    // "PolyOE" là tên của Persistence Unit (định nghĩa trong file persistence.xml)
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("PolyOE");
    EntityManager em = factory.createEntityManager();
    /**
     * Tìm kiếm và trả về tất cả các đối tượng (ví dụ: User)
     */
    public void findAll() {
        String jpql = "SELECT o FROM User o";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        List<User> list = query.getResultList();
        list.forEach(user -> {
            String fullname = user.getFullname();
            boolean admin = user.getAdmin();
            System.out.println(fullname+":"+admin);
        });
    }

    /**
     * Tìm kiếm đối tượng bằng ID
     */
    public void findById() {
        User user = em.find(User.class,"<<user-id>>");
        String fullname = user.getFullname();
        boolean admin = user.getAdmin();
        System.out.println(fullname+":"+admin);
    }

    /**
     * Thêm đối tượng mới vào cơ sở dữ liệu
     */
    public void create() {
        User user = new User("U01","123","teo@gmail.com","Tèo",false);
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    /**
     * Cập nhật thông tin đối tượng đã tồn tại
     */
    public void update() {
        User user = em.find(User.class,"U01");
        user.setFullname("Nguyễn Văn Tèo");
        user.setEmail("teonv@gmail.com");
        try{
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa đối tượng bằng ID
     */
    public void deleteById() {
        User user = em.find(User.class, "U01");
        try {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }



    public void findByEmailSuffixAndNotAdmin() {
        // 1. Viết câu lệnh JPQL với tham số (named parameters)
        String jpql = "SELECT o FROM User o WHERE o.email LIKE :search AND o.admin = :role";

        // 2. Tạo đối tượng truy vấn (TypedQuery)
        TypedQuery<User> query = em.createQuery(jpql, User.class);

        // 3. Thiết lập giá trị cho các tham số
        // Tham số email LIKE: "%@fpt.edu.vn" [cite: 125, 127]
        query.setParameter("search", "%@fpt.edu.vn");

        // Tham số role: false (không phải admin) [cite: 125, 128]
        query.setParameter("role", false);

        // 4. Lấy danh sách kết quả
        List<User> list = query.getResultList();

        // 5. Duyệt và xuất kết quả
        System.out.println("--- KẾT QUẢ TÌM KIẾM THEO EMAIL VÀ ROLE ---");
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy User nào phù hợp.");
        } else {
            list.forEach(user -> {
                String fullname = user.getFullname(); // Lấy họ tên
                String email = user.getEmail();       // Lấy email
                // Xuất ra Họ tên và Email
                System.out.println("Họ tên: " + fullname + ", Email: " + email);
            });
        }
    }


    public void findUsersByPage(int pageNumber, int pageSize) {
        // 1. Viết câu lệnh JPQL (tương tự findAll)
        String jpql = "SELECT o FROM User o";
        TypedQuery<User> query = em.createQuery(jpql, User.class);

        // 2. Thiết lập kích thước trang tối đa (Max Results)
        query.setMaxResults(pageSize); // setMaxResults(5) [cite: 131, 134, 143]

        // 3. Tính và thiết lập vị trí bắt đầu (First Result)
        // Để lấy trang 3, vị trí bắt đầu là: (3 - 1) * 5 = 10
        int startPosition = (pageNumber - 1) * pageSize;
        query.setFirstResult(startPosition); // setFirstResult(10)

        // 4. Lấy danh sách kết quả
        List<User> list = query.getResultList();

        // 5. Duyệt và xuất kết quả
        System.out.println("\n--- DANH SÁCH USER TRANG " + pageNumber + " (Kích thước: " + pageSize + ") ---");
        if (list.isEmpty()) {
            System.out.println("Không có user nào trong trang này.");
        } else {
            list.forEach(user -> {
                // Xuất ra ID, Họ tên và Email
                System.out.println("ID: " + user.getId() + ", Họ tên: " + user.getFullname() + ", Email: " + user.getEmail());
            });
        }
    }
}
