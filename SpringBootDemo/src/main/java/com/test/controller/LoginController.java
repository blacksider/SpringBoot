package com.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.test.reqVo.ReqUser;
import com.test.service.UserService;

@RestController
public class LoginController {

	@Autowired
	private UserService userService;
	
	// 验证用户信息
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public DeferredResult<?> loginUser(HttpServletRequest request,
			@RequestBody @Valid ReqUser user) {
		DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

		HttpSession existedSession = request.getSession(false);
		// session不为空 先判断session中是否有SecurityContext，尝试获取其中的token
		if (existedSession != null) {
			SecurityContext context = (SecurityContext) existedSession
					.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
			if (context != null) {
				ResponseEntity<String> resEntity = new ResponseEntity<String>(
						context.getAuthentication().getAuthorities().toString(),
						HttpStatus.OK);
				deferredResult.setResult(resEntity);
				return deferredResult;
			}
		}

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword());

		userService
				.authenticate(token)
				.subscribe(
						auth -> {
							HttpSession newSession = request.getSession(true);
							SecurityContextHolder.getContext()
									.setAuthentication(auth);
							newSession.setAttribute(
									HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
									SecurityContextHolder.getContext());

							ResponseEntity<String> resEntity = new ResponseEntity<String>(
									token.getAuthorities().toString(),
									HttpStatus.OK);

							deferredResult.setResult(resEntity);
						},
						error -> {
							ResponseEntity<String> resEntity = new ResponseEntity<String>(
									error.getMessage(),
									HttpStatus.INTERNAL_SERVER_ERROR);

							deferredResult.setResult(resEntity);
						});

		return deferredResult;

	}
}
