package com.chen.yugong.demo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Blog implements Serializable {
    Integer bid; // 文章ID
    String name; // 文章标题
    Integer authorId; // 文章作者ID
    String comment;
}
