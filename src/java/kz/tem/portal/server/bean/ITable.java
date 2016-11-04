package kz.tem.portal.server.bean;

import java.io.Serializable;
import java.util.List;

public interface ITable<T> extends Serializable{
	
	public List<T> records();
	
	public Long total();

}
