package org.wang.chuangen.webvs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 请求编码处理
 * @author 王传根
 * @date 2016-8-19 上午10:35:11
 */
public class CharacterEncodingFilter implements Filter{
	
	protected String encoding = "UTF-8";
	protected boolean forceEncoding;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.forceEncoding = "true".equalsIgnoreCase(config.getInitParameter("forceEncoding"));
		String encoding = config.getInitParameter("encoding");
		if(encoding != null && encoding.trim().length() > 0){
			this.encoding = encoding;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(forceEncoding){
			request.setCharacterEncoding(encoding);
			response.setCharacterEncoding(encoding);
		}else if(request.getCharacterEncoding() == null || request.getCharacterEncoding().trim().length() == 0){
			request.setCharacterEncoding(encoding);
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {}
}
