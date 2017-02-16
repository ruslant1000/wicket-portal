package kz.tem.portal.explorer.panel.common.table;

import kz.tem.portal.server.bean.ITable;

@SuppressWarnings("serial")
public abstract class AbstractTableV2<T> extends AbstractTable<T>{

	public AbstractTableV2(String id, boolean withCheckboxColumn) {
		super(id, withCheckboxColumn);
	}

	@Override
	@Deprecated
	public ITable<T> data(int first, int count) throws Exception {
		return null;
	}
	
	public abstract ITable<T> data(int first, int count,String sortColumn, boolean sortAsc) throws Exception ;

	

	@Override
	public ITable<T> getData(int first, int count, String sortColumn,
			boolean sortAsc) throws Exception {
		return data(first, count, sortColumn, sortAsc);
	}

	@Override
	public boolean isSortable() {
		return true;
	}


	
	

}
