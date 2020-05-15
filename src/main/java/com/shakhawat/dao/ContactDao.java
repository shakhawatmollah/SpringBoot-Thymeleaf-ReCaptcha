package com.shakhawat.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shakhawat.entity.Contact;

@Repository
public interface ContactDao extends CrudRepository<Contact, String>{

}
