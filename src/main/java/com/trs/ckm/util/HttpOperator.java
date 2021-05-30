package com.trs.ckm.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.web.client.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;


public class HttpOperator {
	/** 默认连接超时时间, 单位ms */
	private final static int DEFAULT_CONNECT_TIMEOUT = 60000; //60秒
	/** 默认返回数据的超时时间, 单位ms*/
	private final static int DEFAULT_READ_TIMEOUT = 60000; //60秒
	/** 单例模式 */
	private final static HttpOperator HTTP_OPERATOR = new HttpOperator();
	
	private int connectTimeout;
	private int readTimeout;
	
	private HttpOperator() {
		connectTimeout = DEFAULT_CONNECT_TIMEOUT;
		readTimeout = DEFAULT_READ_TIMEOUT;
	}
	
	public static HttpOperator get() {
		return HTTP_OPERATOR;
	}
	
	public static HttpOperator get(int connectTimeout, int readTimeout) {
		HTTP_OPERATOR.connectTimeout = connectTimeout;
		HTTP_OPERATOR.readTimeout = readTimeout;
		return HTTP_OPERATOR;
	}
	
	public int getConnectTimeout() {
		return this.connectTimeout;
	}
	public HttpOperator setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}
	public int getReadTimeout() {
		return this.readTimeout;
	}
	public HttpOperator setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}
	/**
	 * 发送 POST 请求
	 * @param url 
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	public String post(String url, MultiValueMap<String, String> parameter) throws IOException {
		RestTemplate rest = new RestTemplate();
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		/* 设置超时时间  */
		clientHttpRequestFactory.setConnectTimeout(connectTimeout);
		clientHttpRequestFactory.setReadTimeout(readTimeout);
		rest.setRequestFactory(clientHttpRequestFactory);
		/* 获取到自带的消息转换器后, 将以UTF-8编码的新转换器放到队首的位置, 防止乱码 */
		rest.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpEntity<MultiValueMap<String, String>> request = 
				new HttpEntity<MultiValueMap<String,String>>(parameter, new HttpHeaders());
		ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.POST, request, String.class);
		return responseEntity.getBody();
	}
	/**
	 * 发送特殊的 POST 请求
	 * @param url
	 * @param parameter
	 * @return
	 */
	public String specialPost(String url, MultiValueMap<String, Object> parameter) {
		RestTemplate rest = new RestTemplate();
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(connectTimeout);
		clientHttpRequestFactory.setReadTimeout(readTimeout);
		rest.setRequestFactory(clientHttpRequestFactory);
		rest.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = 
				new HttpEntity<MultiValueMap<String, Object>>(parameter, headers);
		ResponseEntity<String> responseEntity = rest.postForEntity(url, httpEntity, String.class);
		return responseEntity.getBody();
	}
	/**
	 * 发送 json 格式的请求
	 * @param url
	 * @param jsonRequestBody
	 * @return
	 */
	public String postJson(String url, String jsonRequestBody) {
		RestTemplate rest = new RestTemplate();
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(connectTimeout);
		clientHttpRequestFactory.setReadTimeout(readTimeout);
		rest.setRequestFactory(clientHttpRequestFactory);
		rest.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		/*
		 * Q: 为什么不设置为 MediaType.APPLICATION_JSON_UTF8
		 * A: request body 是表单, 只是表单的内容是json, 而不是整个body都是json.
		 */
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> httpEntity = new HttpEntity<String>(jsonRequestBody, headers);
		ResponseEntity<String> responseEntity = rest.postForEntity(url, httpEntity, String.class);
		return responseEntity.getBody();
	}
	/**
	 * 发送 GET 请求
	 * @param url
	 * @return
	 */
	public String get(String url) {
		RestTemplate rest = new RestTemplate();
		rest.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpEntity<Object> request = new HttpEntity<Object>(new Object(), new HttpHeaders());
		return rest.exchange(url, HttpMethod.GET, request, String.class).getBody();
	}
	/**
	 * 发送 POST 请求, 返回二进制数据
	 * @param url
	 * @param parameter
	 * @return
	 */
	public byte[] postForBinary(String url, MultiValueMap<String, String> parameter) {
		RestTemplate rest = new RestTemplate();
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(connectTimeout);
		clientHttpRequestFactory.setReadTimeout(readTimeout);
		rest.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpEntity<MultiValueMap<String, String>> request = 
				new HttpEntity<MultiValueMap<String, String>>(parameter, new HttpHeaders());
		ResponseEntity<byte[]> responseEntity = rest.exchange(url, HttpMethod.POST, request, byte[].class);
		return responseEntity.getBody();
	}
	
}
