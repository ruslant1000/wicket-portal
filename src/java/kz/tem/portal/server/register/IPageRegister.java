package kz.tem.portal.server.register;

import java.io.Serializable;
import java.util.List;

import kz.tem.portal.PortalException;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.server.model.Page;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
public interface IPageRegister extends Serializable{

	public Page addNewPage(Page page) throws PortalException;

	public void savePage(Page page) throws PortalException;

	public void deletePage(Long id) throws PortalException;

	public Page getPage(Long id) throws PortalException;
	public Page getPage(String url) throws PortalException;

	public ITable<Page> pages(int first, int count) throws PortalException;
	
	
	public List<Page> pagesTree()throws PortalException;

}
