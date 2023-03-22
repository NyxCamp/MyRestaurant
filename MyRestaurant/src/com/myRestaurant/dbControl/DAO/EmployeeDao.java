package com.myRestaurant.dbControl.DAO;

import com.myRestaurant.dbControl.domain.Employee;

import java.util.List;

public class EmployeeDao extends BasicDao<Employee>{
    private static final Class<Employee> clazz = Employee.class;

    public List<Employee> select(String sql, Object... params) {
        return super.select(sql, clazz, params);
    }

    public Employee selectOne(String sql, Object... params) {
        return super.selectOne(sql, clazz, params);
    }

    public Employee logIn(Integer id, String password){
        String sql = "select * from employee where id=? and pwd=md5(?)";
        return this.selectOne(sql,id,password);
    }
}
