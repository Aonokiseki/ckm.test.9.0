package com.trs.ckm.test.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
//@ComponentScan(basePackages={"com.trs.ckm.api.master", "com.trs.ckm.test.aspect"})
@ComponentScan(basePackageClasses = {
		com.trs.ckm.api.master.TRSCkmRequest.class,
		com.trs.ckm.api.master.ControllerPath.class,
		com.trs.ckm.test.aspect.MethodAspect.class
		})
@PropertySource("file:./config/server.properties")
public class AspectConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
