package com.test.aspect;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SystemLogAspect {
	DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger(SystemLogAspect.class.getName());

	@Pointcut("execution(* com.test..service.*.*(..))")
	private void inServiceLayer() {
	}

	@Around("inServiceLayer()")
	public Object doConcurrentOperation(ProceedingJoinPoint pjp)
			throws Throwable {
		ZonedDateTime dateTime = ZonedDateTime.now();
		String currentTime = dateTime.format(formatter);
		String method = pjp.getSignature().getDeclaringTypeName() + "."
				+ pjp.getSignature().getName();
		logger.info(currentTime + " : start execute method " + method);
		Object rs = null;
		try {
			rs = pjp.proceed();
		} catch (Exception e) {
			logger.info(currentTime + " : execute method " + method
					+ " throws exception " + e);
			throw e;
		}
		logger.info(currentTime + " : end execute method " + method);
		return rs;
	}
}
