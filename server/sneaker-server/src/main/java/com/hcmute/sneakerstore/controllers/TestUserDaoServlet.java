package com.hcmute.sneakerstore.controllers;

import com.hcmute.sneakerstore.business.User;
import com.hcmute.sneakerstore.data.DBUtils;
import com.hcmute.sneakerstore.data.JpaProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/testUserDao")
public class TestUserDaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        StringBuilder output = new StringBuilder();

        @Cleanup EntityManager em = JpaProvider.getEntityManager();
        EntityTransaction tran = em.getTransaction();

        try {
            // Test insert (create) User
            tran.begin();
            User newUser = User.builder()
                               .firstName("Huy")
                               .lastName("Tran")
                               .email("huycatlo@example.com")
                               .gender(true)
                               .birthday(LocalDate.of(2003, 2, 18))
                               .phoneNumber("1234567890")
                               .build();
            
            em.persist(newUser);
            tran.commit();
            output.append("insert: Created user with ID: ").append(newUser.getId()).append("\n");

            // Test select (read) User
            tran.begin();
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
            query.setParameter("id", newUser.getId());
            User selectedUser = query.getSingleResult();
            tran.commit();
            output.append("select: ").append(selectedUser != null ? selectedUser.toString() : "No user found").append("\n");

            // Test update User
            tran.begin();
            User userToUpdate = em.find(User.class, newUser.getId());
            userToUpdate.setEmail("updatedemail@example.com"); // Example update
            em.merge(userToUpdate);
            tran.commit();
            output.append("update: Updated user email").append("\n");

            // Test delete User
            tran.begin();
            TypedQuery<User> deleteQuery = em.createQuery("DELETE FROM User u WHERE u.id = :id", User.class);
            deleteQuery.setParameter("id", newUser.getId());
            long deleteCount = DBUtils.executeUpdateOrDelete(em, deleteQuery);
            tran.commit();
            output.append("delete: ").append(deleteCount == 1 ? "Success" : "Failed").append("\n");

        } catch (Exception e) {
            if (tran.isActive()) {
                tran.rollback();
            }
            output.append("An error occurred: ").append(e.getMessage());
        }

        response.getWriter().print(output.toString());
    }
}
