package kz.tem.portal.explorer.panel.common.table;

import kz.tem.portal.server.bean.ITable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

@SuppressWarnings("serial")
public abstract class AbstractTable<T> extends Panel{

	
	private AColumn[] cols = null;
	private ITable<T> records = null;
	private int first=0;
	private int count=0;
	private WebMarkupContainer table = null;
	public AbstractTable(String id) {
		super(id);
		build();
	}
	
	public void build(){
		try {
			if(AbstractTable.this.get("table")!=null){
				AbstractTable.this.remove("table");
			}
			table = new WebMarkupContainer("table");
			table.setOutputMarkupId(true);
			add(table);
			
			cols = columns();
			records = data(first, count);
			
			buildColumns();
			buildData();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void buildColumns()throws Exception{
		RepeatingView view = new RepeatingView("th");
		table.add(view);
		
		for(AColumn<T> col:cols){
			view.add(new Label(view.newChildId(),col.getTitle()));
		}
	}
	private void buildData()throws Exception{
		RepeatingView tr = new RepeatingView("row");
		table.add(tr);
		
		for(T t:records.records()){
			WebMarkupContainer row = new WebMarkupContainer(tr.newChildId());
			tr.add(row);
			RepeatingView td = new RepeatingView("cell");
			row.add(td);
			for(AColumn<T> col:cols){
				String cid = td.newChildId();
				td.add(col.cell(cid, t));
			}
		}
	}
	
	public abstract ITable<T> data(int first,int count)throws Exception;
	
	public abstract AColumn[] columns()throws Exception;

}
