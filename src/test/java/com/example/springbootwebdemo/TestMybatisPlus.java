package com.example.springbootwebdemo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootwebdemo.demo.entity.RoleEntity;
import com.example.springbootwebdemo.demo.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//不加此注解，导致@Autowired引入Null
@RunWith(SpringRunner.class)
//不加web环境的话，websocket会报错javax.websocket.server.ServerContainer not available
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestMybatisPlus {

    private static final String roleName = "testAdd";

    @Autowired
    private RoleService roleService;

    @Test
    public void add() {
        //添加时可观察到自动填充插件已生效
        roleService.save(new RoleEntity().setName(roleName));
    }

    @Test
    public void update() {
        //取出两次相同的值，取出时可观察到逻辑删除字段已生效
        RoleEntity roleEntity1 = roleService.getOne(Wrappers.lambdaQuery(RoleEntity.class).eq(RoleEntity::getName, roleName));
        RoleEntity roleEntity2 = roleService.getOne(Wrappers.lambdaQuery(RoleEntity.class).eq(RoleEntity::getName, roleName));
        //更新后乐观锁的值将改变
        roleService.updateById(roleEntity1);
        //乐观锁不对应的原因，将更新0条
        roleService.updateById(roleEntity2);
    }

    @Test
    public void delete() {
        //删除时可观察到逻辑删除已生效，删除被改为update
        roleService.remove(Wrappers.lambdaQuery(RoleEntity.class).eq(RoleEntity::getName, roleName));
    }

}
