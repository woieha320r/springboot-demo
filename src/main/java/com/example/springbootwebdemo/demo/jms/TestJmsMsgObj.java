package com.example.springbootwebdemo.demo.jms;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 测试被JMS发送的对象
 *
  * @date 2021-12-09
 */
@Data
@ToString
@Accessors(chain = true)
public class TestJmsMsgObj implements Serializable {

    private static final long serialVersionUID = 1L;

    String name;

    List<TestJmsMsgObj> nestedsDemo;

}
