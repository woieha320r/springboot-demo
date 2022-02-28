package com.example.springbootwebdemo;

import com.example.springbootwebdemo.demo.entity.RoleEntity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestListStream {

    /**
     * stream会改变列表內部元素
     */
    @Test
    public void testPeek() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity().setName("1").setDeleted(false));
        System.out.println(roles.parallelStream().peek(role -> role.setDeleted(true)).collect(Collectors.toList()));
        System.out.println(roles);
    }

    @Test
    public void testMap() {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(new RoleEntity().setName("1").setDeleted(false));
        System.out.println(roles.parallelStream().map(role -> "ROLE_" + role.getName()).collect(Collectors.toList()));
        System.out.println(roles);
    }


}
