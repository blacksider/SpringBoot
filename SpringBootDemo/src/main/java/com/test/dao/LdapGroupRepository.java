package com.test.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.model.LdapGroup;

public interface LdapGroupRepository extends JpaRepository<LdapGroup, Long> {
	List<LdapGroup> findByDn(String dn);

	List<LdapGroup> findByServerDN(String serverdn);
}
