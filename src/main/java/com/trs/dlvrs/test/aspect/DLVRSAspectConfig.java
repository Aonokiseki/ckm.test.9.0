package com.trs.dlvrs.test.aspect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.trs.ckm.util.FakeShell;
import com.trs.hybase.client.TRSConnection;
import com.trs.hybase.client.params.ConnectParams;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan(basePackageClasses = {
	com.trs.dlvrs.api.master.TRSVRSRequest.class,
	com.trs.dlvrs.test.aspect.MethodAspect.class,
	com.trs.dlvrs.test.aspect.JdbcMethodAspect.class,
	com.trs.dlvrs.test.aspect.FakeShellAspect.class,
})
@PropertySource("file:./config/dlvrs.server.properties")
public class DLVRSAspectConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public static FakeShell fakeShell(
			@Value("${dlvrs.host}") String host, 
			@Value("${shell.user}") String user, 
			@Value("${shell.password}") String password) {
		return new FakeShell(host, 22, user, password);
	}
	
	@Bean(name="remoteDirectory")
	public static String remoteDirectory(
			@Value("${shell.test.data.directory}") String remoteDirectory) {
		return remoteDirectory;
	}
	
	@Bean(name="faceRecognitionTable")
	public static String getFaceRecognitionTable(
			@Value("${jdbc.table}") String faceRecognitionTable) {
		return faceRecognitionTable;
	}
	
	@Bean
	public static JdbcTemplate jdbcTemplate(
			@Value("${jdbc.driver.classname}") String driverClassName, 
			@Value("${jdbc.url}") String url, 
			@Value("${jdbc.user}") String user, 
			@Value("${jdbc.password}") String password) {
		DriverManagerDataSource source = new DriverManagerDataSource(url, user, password);
		source.setDriverClassName(driverClassName);
		JdbcTemplate jdbcTemplate =  new JdbcTemplate(source);
		return jdbcTemplate;
	}
	
	@Bean
	public static TRSConnection trsconnection(
			@Value("${trshybase.url}") String url, 
			@Value("${trshybase.user}") String user, 
			@Value("${trshybase.password}") String password) {
		return new TRSConnection(url, user, password, new ConnectParams());
	}
	
	@Bean(name = "graphSearchDBName")
	public static String graphSearchDBName(@Value("${trshybase.dbname}") String dbName) {
		return dbName;
	}
}
