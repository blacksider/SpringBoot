package com.test.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.model.LdapPerson;

public interface LdapPersonReposiry extends JpaRepository<LdapPerson, String> {
	
}
