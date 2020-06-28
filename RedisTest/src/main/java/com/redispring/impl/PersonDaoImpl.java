package com.redispring.impl;

import com.redispring.inte.PersonDao;

public class PersonDaoImpl implements PersonDao {

	public void add() {
        System.out.println("save()执行了...");
    }
}