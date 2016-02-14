package de.oglimmer.cyc.web.ext;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.oglimmer.cyc.web.WebContainerProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class HttpsFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;

		FilterProcessor fp = new FilterProcessor(httpReq, httpResp);
		fp.doFilter(chain);

		// HACK: if the threadLocals are not cleared here, the webapp doesn't unload properly
		org.apache.http.client.utils.DateUtils.clearThreadLocal();
	}

	static class FilterProcessor {

		private HttpServletRequest request;
		private HttpServletResponse response;

		private String domain;
		private String servletPath;
		private String pathInfo;

		public FilterProcessor(HttpServletRequest httpReq, HttpServletResponse httpResp) {
			this.request = httpReq;
			this.response = httpResp;
			domain = httpReq.getServerName();
			servletPath = httpReq.getServletPath();// index.jsp or /rest
			pathInfo = httpReq.getPathInfo();// null or /runtime/dbpool
			log.trace("domain={}, servletPath={}, pathInfo={}", domain, servletPath, pathInfo);
		}

		public void doFilter(FilterChain chain) throws IOException, ServletException {
			if (redirectToSecureNeeded()) {
				redirectToSecure();
			} else {
				chain.doFilter(request, response);
			}
		}

		private boolean redirectToSecureNeeded() {
			return WebContainerProperties.INSTANCE.isHttpsEnabled() && !request.isSecure();
		}

		private void redirectToSecure() throws IOException {
			String queryParam = request.getQueryString() != null ? "?" + request.getQueryString() : "";
			response.sendRedirect("https://" + domain + request.getRequestURI() + queryParam);
		}
	}
}