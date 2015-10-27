package com.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.test.ldap.pojo.Person;
import com.test.ldap.service.LdapService;
import com.test.model.LdapGroup;
import com.test.model.LdapGroupServerInfo;

@RestController
@RequestMapping("/ldap")
public class LdapController {
	@Autowired
	private LdapService ldapService;

	// @Autowired
	// private PersonRepo repo;

	@RequestMapping(value = "/group/", method = RequestMethod.POST)
	public DeferredResult<?> saveLdapServerGroup(@RequestBody LdapGroup group) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

		ldapService
				.saveNewServerGroup(group)
				.subscribe(
						savedID -> {
							ResponseEntity<Long> responseEntity = new ResponseEntity<>(
									savedID, HttpStatus.OK);
							deferredResult.setResult(responseEntity);
						},
						error -> {
							ResponseEntity<String> responseEntity = new ResponseEntity<>(
									error.getMessage(), HttpStatus.BAD_REQUEST);
							deferredResult.setResult(responseEntity);
						});
		return deferredResult;
	}

	@RequestMapping(value = "/group/tree", method = RequestMethod.POST)
	public DeferredResult<?> loadLdapGroupInServer(
			@RequestBody LdapGroupServerInfo groupServerInfo) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

		ldapService
				.loadGroupOfServer(groupServerInfo)
				.subscribe(
						groupTree -> {
							ResponseEntity<LdapGroup> responseEntity = new ResponseEntity<>(
									groupTree, HttpStatus.OK);
							deferredResult.setResult(responseEntity);
						},
						error -> {
							ResponseEntity<String> responseEntity = new ResponseEntity<>(
									error.getMessage(), HttpStatus.BAD_REQUEST);
							deferredResult.setResult(responseEntity);
						});
		return deferredResult;
	}

	@RequestMapping(value = "/group/{serverID}", method = RequestMethod.GET)
	public DeferredResult<?> getLdapGroupTree(
			@PathVariable("serverID") long serverID) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

		ldapService
				.getGroupTree(serverID)
				.subscribe(
						groupTree -> {
							ResponseEntity<LdapGroup> responseEntity = new ResponseEntity<>(
									groupTree, HttpStatus.OK);
							deferredResult.setResult(responseEntity);
						},
						error -> {
							ResponseEntity<String> responseEntity = new ResponseEntity<>(
									error.getMessage(), HttpStatus.BAD_REQUEST);
							deferredResult.setResult(responseEntity);
						});

		return deferredResult;
	}

	@RequestMapping(value = "/person/{groupID}", method = RequestMethod.GET)
	public DeferredResult<?> getLdapGroupPersons(
			@PathVariable("groupID") long groupID) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

		ldapService
				.getGroupPerson(groupID)
				.subscribe(
						persons -> {
							ResponseEntity<List<Person>> responseEntity = new ResponseEntity<>(
									persons, HttpStatus.OK);
							deferredResult.setResult(responseEntity);
						},
						error -> {
							ResponseEntity<String> responseEntity = new ResponseEntity<>(
									error.getMessage(), HttpStatus.BAD_REQUEST);
							deferredResult.setResult(responseEntity);
						});
		return deferredResult;
	}

	@RequestMapping(value = "/group/show", method = RequestMethod.POST)
	public DeferredResult<?> getLdapGroupTreeNoDB(
			@RequestBody LdapGroupServerInfo groupServerInfo) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

		ldapService
				.loadGroupOfServerNoDB(groupServerInfo)
				.subscribe(
						groupTree -> {
							ResponseEntity<LdapGroup> responseEntity = new ResponseEntity<>(
									groupTree, HttpStatus.OK);
							deferredResult.setResult(responseEntity);
						},
						error -> {
							ResponseEntity<String> responseEntity = new ResponseEntity<>(
									error.getMessage(), HttpStatus.BAD_REQUEST);
							deferredResult.setResult(responseEntity);
						});

		return deferredResult;
	}
}
