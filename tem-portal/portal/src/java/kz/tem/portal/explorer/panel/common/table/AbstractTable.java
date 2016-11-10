package kz.tem.portal.explorer.panel.common.table;

import java.util.LinkedList;
import java.util.List;

import kz.tem.portal.explorer.panel.common.form.field.FCheckboxField;
import kz.tem.portal.server.bean.ITable;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

@SuppressWarnings("serial")
public abstract class AbstractTable<T> extends Panel{

	
	private AColumn[] cols = null;
	private ITable<T> records = null;
	private int first=0;
	private int count=0;
	private WebMarkupContainer table = null;
	
	private boolean withCheckboxColumn;
	
	private List<FirstColumnCheck<T>> rowsCheckboxes = new LinkedList<FirstColumnCheck<T>>();
	
	
	public AbstractTable(String id, boolean withCheckboxColumn) {
		super(id);
		setOutputMarkupId(true);
	
		this.withCheckboxColumn=withCheckboxColumn;
		add(before("before"));
		build();
	}
	
	public Component before(String id){
		WebMarkupContainer cmp = new WebMarkupContainer(id);
		cmp.setOutputMarkupId(true);
		cmp.setVisible(false);
		return cmp;
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
		
		if(withCheckboxColumn){
			FCheckboxField chk = new FCheckboxField(view.newChildId(), new Model<Boolean>());
			view.add(chk);
		}
		
		for(AColumn<T> col:cols){
			view.add(new Label(view.newChildId(),col.getTitle()));
		}
	}
	
	
	
	private void buildData()throws Exception{
		rowsCheckboxes.clear();
		
		RepeatingView tr = new RepeatingView("row");
		table.add(tr);
		
		for(T t:records.records()){
			WebMarkupContainer row = new WebMarkupContainer(tr.newChildId());
			tr.add(row);
			RepeatingView td = new RepeatingView("cell");
			row.add(td);
			
			if(withCheckboxColumn){
				FirstColumnCheck<T> chk = new FirstColumnCheck<T>(td.newChildId(), t);
//				FCheckboxField chk = new FCheckboxField(td.newChildId(), new Model<Boolean>());
				td.add(chk);
				rowsCheckboxes.add(chk);
			}
			
			for(AColumn<T> col:cols){
				
				
				
				String cid = td.newChildId();
				td.add(col.cell(cid, t));
			}
		}
	}
	
	public List<T> getSelectedRows(){
		List<T> list = new LinkedList<T>();
		for (FirstColumnCheck<T> chk:rowsCheckboxes){
			if(chk.isSelected())
				list.add(chk.getRecord());
		}
		return list;
	}
	
	public abstract ITable<T> data(int first,int count)throws Exception;
	
	public abstract AColumn[] columns()throws Exception;

}
