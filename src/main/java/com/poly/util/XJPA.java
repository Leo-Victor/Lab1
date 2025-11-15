package com.poly.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class XJPA {
    private static EntityManagerFactory factory;

    static {
        // Khởi tạo EntityManagerFactory một lần duy nhất.
        // Tên "PolyOE" phải khớp với tên trong persistence.xml
        factory = Persistence.createEntityManagerFactory("PolyOE"); //[cite: 158]
    }

    public static EntityManager getEntityManager(){
        // Cung cấp một EntityManager mới
        return factory.createEntityManager(); //[cite: 162]
    }
}
