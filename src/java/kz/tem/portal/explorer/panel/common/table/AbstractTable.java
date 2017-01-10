package kz.tem.portal.explorer.panel.common.table;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import kz.tem.portal.explorer.panel.common.component.AjaxLabelLink;
import kz.tem.portal.explorer.panel.common.form.field.FAjaxCheckboxField;
import kz.tem.portal.server.bean.ITable;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class AbstractTable<T> extends Panel{

	
	private AColumn[] cols = null;
	private ITable<T> records = null;
	private int first=0;
	private int count=10;
	private int total = 0;
	
	private int[] pageSizes = new int[]{10,50,100};
	
	private WebMarkupContainer table = null;
	
	private boolean withCheckboxColumn;
	
	private List<FirstColumnCheck<T>> rowsCheckboxes = new LinkedList<FirstColumnCheck<T>>();
	
	
	public AbstractTable(String id, boolean withCheckboxColumn) {
		super(id);
		setOutputMarkupId(true);
	
		this.withCheckboxColumn=withCheckboxColumn;
		
	}
	
	
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
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
			if(AbstractTable.this.get("digits")!=null){
				AbstractTable.this.remove("digits");
			}
			if(AbstractTable.this.get("sizes")!=null){
				AbstractTable.this.remove("sizes");
			}
			if(AbstractTable.this.get("total")!=null){
				AbstractTable.this.remove("total");
			}
			table = new WebMarkupContainer("table");
			table.setOutputMarkupId(true);
			add(table);
			
			cols = columns();
			records = data(first, count);	
			total=records.total().intValue();
			
			buildColumns();
			buildData();
			buildPaginator();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in AbstractTable build method",e);
		}
	}
	
	private void buildColumns()throws Exception{
		RepeatingView view = new RepeatingView("th");
		table.add(view);
		
		if(withCheckboxColumn){
			FAjaxCheckboxField chk = new FAjaxCheckboxField(view.newChildId(), new Model<Boolean>()){

				@Override
				public void onChangeValue(AjaxRequestTarget target)
						throws Exception {
					super.onChangeValue(target);
					for(FirstColumnCheck<T> chk2:rowsCheckboxes){
						chk2.setSelected(getValue());
						target.add(chk2);
					}
				}
				
			};
			view.add(chk);
			chk.add(new AttributeModifier("style", "width:30px;"));
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
	
	
	public void buildPaginator(){
		RepeatingView digits = new RepeatingView("digits");
		add(digits);
		
		AjaxLabelLink firstD = new AjaxLabelLink(digits.newChildId(),"Первая") {
			
			@Override
			public void onClick(AjaxRequestTarget target) throws Exception {
				first=0;
				build();
				target.add(AbstractTable.this);
			}
		};
		digits.add(firstD);
		firstD.add(new AttributeAppender("class", " page-first"));
		
		AjaxLabelLink prevD = new AjaxLabelLink(digits.newChildId(),"Назад") {
			
			@Override
			public void onClick(AjaxRequestTarget target) throws Exception {
				first = first - count;
				if(first<0)first =0;
				build();
				target.add(AbstractTable.this);
			}
		};
		digits.add(prevD);
		prevD.add(new AttributeAppender("class", " page-prev"));
		
		
		int selectedDigit = (first + count)/count;
		int maxDigit = (int) Math.ceil(  (double)total / (double)count );
		
		if(selectedDigit>2){
			AjaxLabelLink nm = new AjaxLabelLink(digits.newChildId(),"...") {
				
				@Override
				public void onClick(AjaxRequestTarget target) throws Exception {
					first = first - count - count;
					build();
					target.add(AbstractTable.this);
				}
			};
			digits.add(nm);
		}
		if(selectedDigit>1){
			AjaxLabelLink nm = new AjaxLabelLink(digits.newChildId(),""+(selectedDigit-1)) {
				
				@Override
				public void onClick(AjaxRequestTarget target) throws Exception {
					first = first - count;
					build();
					target.add(AbstractTable.this);
				}
			};
			digits.add(nm);
		}
		if(true){
			AjaxLabelLink nm = new AjaxLabelLink(digits.newChildId(),""+(selectedDigit)) {
				
				@Override
				public void onClick(AjaxRequestTarget target) throws Exception {
				}
			};
			nm.add(new AttributeAppender("class", " selected"));
			digits.add(nm);
		}
		if(selectedDigit<maxDigit){
			AjaxLabelLink nm = new AjaxLabelLink(digits.newChildId(),""+(selectedDigit+1)) {
				
				@Override
				public void onClick(AjaxRequestTarget target) throws Exception {
					first = first + count;
					build();
					target.add(AbstractTable.this);
				}
			};
			digits.add(nm);
		}
		if((selectedDigit+1)<maxDigit){
			AjaxLabelLink nm = new AjaxLabelLink(digits.newChildId(),"...") {
				
				@Override
				public void onClick(AjaxRequestTarget target) throws Exception {
					first = first + count + count;
					build();
					target.add(AbstractTable.this);
				}
			};
			digits.add(nm);
		}
		AjaxLabelLink nextD = new AjaxLabelLink(digits.newChildId(),"Вперед") {
			
			@Override
			public void onClick(AjaxRequestTarget target) throws Exception {
				if((first+count)<total)
					first = first + count;
				System.out.println("first = "+first);
				build();
				target.add(AbstractTable.this);
			}
		};
		digits.add(nextD);
		nextD.add(new AttributeAppender("class", " page-next"));
		
		AjaxLabelLink lastD = new AjaxLabelLink(digits.newChildId(),"Последняя") {
			
			@Override
			public void onClick(AjaxRequestTarget target) throws Exception {
				first = (int) ((total/count)*count);
				if(first==total)
					first = first - count;
				if(first<0)first=0;
				System.out.println("first = "+first);
				build();
				target.add(AbstractTable.this);
			}
		};
		digits.add(lastD);
		lastD.add(new AttributeAppender("class", " page-last"));
		
		
		
		RepeatingView sizes = new RepeatingView("sizes");
		add(sizes);
		
		for(final int size:pageSizes){
			AjaxLabelLink sz = new AjaxLabelLink(sizes.newChildId(),size+"") {
				
				@Override
				public void onClick(AjaxRequestTarget target) throws Exception {
					count = size;
					first = 0;
					build();
					target.add(AbstractTable.this);
				}
			};
			if(count==size){
				sz.add(new AttributeAppender("class", " selected"));
			}
			sizes.add(sz);
		}
		add(new Label("total",total+""));
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



	public int getFirst() {
		return first;
	}



	public void setFirst(int first) {
		this.first = first;
	}



	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}
	
	
	

}
