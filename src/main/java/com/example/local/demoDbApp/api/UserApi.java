package com.example.local.demoDbApp.api;

import com.example.local.demoDbApp.dao.UserDao;
import com.example.local.demoDbApp.model.UserData;
import com.example.local.demoDbApp.model.UserForm;
import com.example.local.demoDbApp.pojo.UserPojo;
import com.increff.commons.springboot.common.ConvertUtil;
import com.increff.commons.springboot.common.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserApi {
    @Autowired
    private UserDao dao;

    @Transactional(rollbackFor = Exception.class)
    public UserData addUser(UserForm userForm) throws IllegalAccessException {
        ValidationUtil.validate(userForm);
        normalize(userForm);
        UserPojo pojo = ConvertUtil.convert(userForm, UserPojo.class);
        dao.persist(pojo);
        return ConvertUtil.convert(pojo, UserData.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<UserData> getAll() throws IllegalAccessException {
        List<UserPojo> userPojos = dao.selectAll();
        List<UserData> userData = new ArrayList<>(userPojos.stream()
                .map(pojo -> ConvertUtil.convert(pojo, UserData.class))
                .toList());
        return userData;
    }

//    public void insertBulk() {
//        for(int i=0;i<50;i++)
//        {
//            UserPojo userPojo = getUser(i);
//            dao.persist(userPojo);
//        }
//    }
//
//    public void testAbstractDao() {
//        try {
//            List<UserPojo> pojos = dao.selectAllUsers();
//            List<UserPojo> pojos2 = dao.selectAll();
//            List<UserPojo> pojos3 = dao.selectAll(10);
//            long count = dao.countAll();
//            UserPojo pojo = dao.select("username", "rishav");
//            UserPojo pojo2 = dao.selectSingle("rishav");
//            List<UserPojo> pojos4 = dao.selectLimit();
//            List<UserPojo> pojos5 = dao.selectMultiple("username", "username0");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public UserPojo getUser(int i) {
//        UserPojo userPojo = new UserPojo();
//        Integer k=i%5;
//        String userName = "username" + k.toString();
//        userPojo.setUsername(userName);
//        return  userPojo;
//    }

    private static void normalize(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                String value = (String) field.get(obj);
                if (value != null) {
                    field.set(obj, value.toLowerCase().trim());
                }
            }
        }
    }
}
