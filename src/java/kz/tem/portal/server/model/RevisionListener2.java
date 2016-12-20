package kz.tem.portal.server.model;

import javax.servlet.http.HttpSession;

import kz.tem.portal.explorer.application.PortalSession;

import org.apache.wicket.session.HttpSessionStore;
import org.hibernate.envers.RevisionListener;
import org.springframework.web.context.request.RequestContextHolder;

public class RevisionListener2 implements RevisionListener{

	@Override
	public void newRevision(Object arg0) {
		
		RevisionEntity entity = (RevisionEntity) arg0;
//		entity.setUsername(RequestContextHolder.currentRequestAttributes().getSessionId());
		try{
			if(PortalSession.get()!=null && PortalSession.get().getUser()!=null){
				entity.setUsername(PortalSession.get().getUser().getLogin());	
			}
		}catch(Exception ex){
//			ex.printStackTrace();
		}
		
	}

}
