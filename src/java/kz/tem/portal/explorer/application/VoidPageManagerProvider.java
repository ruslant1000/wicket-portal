package kz.tem.portal.explorer.application;

import java.io.Serializable;

import org.apache.wicket.Application;
import org.apache.wicket.DefaultPageManagerProvider;
import org.apache.wicket.page.DefaultPageManagerContext;
import org.apache.wicket.page.IManageablePage;
import org.apache.wicket.page.IPageManager;
import org.apache.wicket.page.IPageManagerContext;
import org.apache.wicket.page.PageStoreManager;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.pageStore.IPageStore;

public class VoidPageManagerProvider extends DefaultPageManagerProvider {

	     private IPageManagerContext iPageManagerContext; 
	     private IPageManager iPageManager; 
	     private IDataStore iDataStore; 
	     private IPageStore iPageStore; 
	
	public VoidPageManagerProvider(Application application) {
		super(application);
		 iPageManagerContext =  new DefaultPageManagerContext(); 
		          iDataStore = new VoidIDataStore(); 
		          iPageStore = new VoidIPageStore(); 
		          iPageManager = new 
		  VoidPageStoreManager(application.getName(),iPageStore,iPageManagerContext); 
	}
	
	@Override 
	     public IPageManager get(IPageManagerContext pageManagerContext) { 
	         return iPageManager; 
	     } 
	 
	     @Override 
	     protected IPageStore newPageStore(IDataStore dataStore) { 
	         return iPageStore; 
	     } 
	 
	     @Override 
	     protected IDataStore newDataStore() { 
	         return iDataStore; 
	     }
	     
	     
	     /** 
	           * A data store that doesn't store a byte 
	           */ 
	          private class VoidIDataStore implements IDataStore { 
	      
	              public byte[] getData(String s, int i) { 
	                  return null; 
	              } 
	      
	              public void removeData(String s, int i) { 
	              } 
	      
	              public void removeData(String s) { 
	              } 
	      
	              public void storeData(String s, int i, byte[] bytes) { 
	              } 
	      
	              public void destroy() { 
	              } 
	      
	              public boolean isReplicated() { 
	                  return true; 
	              } 
	      
	              public boolean canBeAsynchronous() { 
	                  return true; 
	              } 
	          } 
	      
	          /** 
	           * A page store that doesn't store a page 
	           */ 
	          private class VoidIPageStore implements IPageStore { 
	      
	              public void destroy() { 
	      
	              } 
	      
	              public IManageablePage getPage(String s, int i) { 
	                  return null; 
	              } 
	      
	              public void removePage(String s, int i) { 
	      
	              } 
	      
	              public void storePage(String s, IManageablePage iManageablePage) { 
	      
	              } 
	      
	              public void unbind(String s) { 
	      
	              } 
	      
	              public Serializable prepareForSerialization(String s, Object o) { 
	                  return null; 
	              } 
	      
	              public Object restoreAfterSerialization(Serializable serializable) 
	      { 
	                  return null; 
	              } 
	      
	              public IManageablePage convertToPage(Object o) { 
	                  return null; 
	              }

				@Override
				public Serializable prepareForSerialization(String sessionId, Serializable page) {
					// TODO Auto-generated method stub
					return null;
				} 
	          } 
	          /** 
	                * The basic store manager, extended to not support versioning 
	                */ 
	               private class VoidPageStoreManager extends PageStoreManager implements 
	           IPageManager { 
	           
	                   public VoidPageStoreManager(String applicationName, IPageStore 
	           pageStore, IPageManagerContext context) { 
	                       super(applicationName, pageStore, context); 
	                   } 
	           
	                   @Override 
	                   public boolean supportsVersioning() { 
	                       return false; 
	                   } 
	               } 
}
