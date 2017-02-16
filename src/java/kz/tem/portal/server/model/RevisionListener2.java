package kz.tem.portal.server.model;

import kz.tem.portal.explorer.application.PortalSession;

import org.hibernate.envers.RevisionListener;

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
