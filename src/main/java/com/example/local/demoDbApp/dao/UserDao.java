package com.example.local.demoDbApp.dao;

import com.example.local.demoDbApp.pojo.UserPojo;
import com.increff.commons.springboot.db.AbstractDao;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public class UserDao extends AbstractDao<UserPojo> {

    private static final String SELECT_MONTH_DATA = "Select u from UserPojo u";
    private static final String select_username = "Select u from UserPojo u where u.username=:username";

    @Transactional(readOnly = true)
    public List<UserPojo> selectAllUsers() {
        TypedQuery<UserPojo> q = createJpqlQuery(SELECT_MONTH_DATA);
        return selectMultiple(q);
    }

    @Transactional(readOnly = true)
    public UserPojo selectSingle(String username) {
        TypedQuery<UserPojo> q = createJpqlQuery(select_username);
        q.setParameter("username", username);
        return selectSingleOrNull(q);
    }

    @Transactional(readOnly = true)
    public List<UserPojo> selectLimit() {
        TypedQuery<UserPojo> q = createJpqlQuery(SELECT_MONTH_DATA);
        return selectMultiple(q,10);
    }
}
