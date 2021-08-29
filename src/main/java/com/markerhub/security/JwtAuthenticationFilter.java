package com.markerhub.security;

import cn.hutool.core.util.StrUtil;
import com.markerhub.entity.SysUser;
import com.markerhub.service.SysUserService;
import com.markerhub.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//jwt身份认证拦截器
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserDetailServiceImpl userDetailService;

	@Autowired
	SysUserService sysUserService;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

		String jwt = request.getHeader(jwtUtils.getHeader()); //获取当前请求头中的jwt
		if (StrUtil.isBlankOrUndefined(jwt)) {
			//JWT为空 放行
			chain.doFilter(request, response);
			return;
		}

		Claims claim = jwtUtils.getClaimByToken(jwt);
		if (claim == null) { //当前jwt解析不合法
			throw new JwtException("token 异常");
		}
		if (jwtUtils.isTokenExpired(claim)) {
			throw new JwtException("token已过期");
		}

		String username = claim.getSubject(); //获取当前jwt的主体
		SysUser sysUser =sysUserService.getByUsername(username);
		// 获取用户的权限等信息 户名和密码之类的用户验证信息，用来做权限验证
		UsernamePasswordAuthenticationToken token
				= new UsernamePasswordAuthenticationToken(username, null,userDetailService.getUserAuthority(sysUser.getId()));

		//token设置在上下文中   SecurityContextHolder 保留系统当前的安全上下文细节，其中就包括当前使用系统的用户的信息
		SecurityContextHolder.getContext().setAuthentication(token);

		chain.doFilter(request, response);
	}
}
