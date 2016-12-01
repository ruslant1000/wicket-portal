package kz.tem.portal.explorer.application;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;

public class PortalFilter extends WicketFilter{

//	@Override
//	protected String getFilterPath(HttpServletRequest request) {
//		String  path =super.getFilterPath(request); 
//		System.out.println("*********************");
//		System.out.println("   filter-path:   "+path);
//		System.out.println("*********************");
//		return  path;
//	}
//
//	@Override
//	public String getRelativePath(HttpServletRequest request) {
//		String path =super.getRelativePath(request);
//		System.out.println("*********************");
//		System.out.println("   relative-path:   "+path);
//		System.out.println("*********************");
//		return path;
//	}

	@Override
	protected boolean processRequestCycle(RequestCycle requestCycle,
			WebResponse webResponse, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		return super.processRequestCycle(requestCycle, webResponse, httpServletRequest,
				httpServletResponse, chain);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("***********************");
		System.out.println("    "+request.getScheme());
		System.out.println("***********************");
		// TODO Auto-generated method stub
		super.doFilter(request, response, chain);
	}
	
	

	
	
}


