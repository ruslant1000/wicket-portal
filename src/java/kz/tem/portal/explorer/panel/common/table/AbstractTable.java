package kz.tem.portal.explorer.panel.common.table;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import kz.tem.portal.explorer.panel.common.component.AjaxLabelLink;
import kz.tem.portal.explorer.panel.common.form.field.FAjaxCheckboxField;
import kz.tem.portal.server.bean.ITable;
import kz.tem.portal.utils.ExceptionUtils;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class AbstractTable<T> extends Panel{

	private String colsHash = "";
	
	private AColumn[] cols = null;
	private ITable<T> records = null;
	private int first=0;
	private int count=10;
	private int total = 0;
	
	private int[] pageSizes = new int[]{10,50,100};
	
	private WebMarkupContainer table = null;
	
	private boolean withCheckboxColumn;
	
	private List<FirstColumnCheck<T>> rowsCheckboxes = new LinkedList<FirstColumnCheck<T>>();
	
	
	private String sortColumn = null;
	private boolean sortAsc = true;
	
	public AbstractTable(String id, boolean withCheckboxColumn) {
		super(id);
		setOutputMarkupId(true);
		this.withCheckboxColumn=withCheckboxColumn;
	}
	
	public boolean isSortable(){
		return false;
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
	/**
	 * Этот параметр используется в механизме ColumnResize. По нему делается запись значений ширин колонок в КЭШ, а так же запись значений из КЭШа.
	 * @param container
	 * @return
	 */
	public String getUniqueMarkupId(MarkupContainer container){
		if(container==null)
			return "";
		return container.getId()+container.getClass().getCanonicalName()+colsHash;
	}
	
	public void build(){
		try {
			String mid = null;
			if(AbstractTable.this.get("table")!=null){
				mid = AbstractTable.this.get("table").getMarkupId();
				AbstractTable.this.remove("table");
			}else
				mid = "table"+getUniqueMarkupId(this).hashCode()+"";
			if(AbstractTable.this.get("digits")!=null){
				AbstractTable.this.remove("digits");
			}
			if(AbstractTable.this.get("info")!=null){
				AbstractTable.this.remove("info");
			}
			if(AbstractTable.this.get("sizes")!=null){
				AbstractTable.this.remove("sizes");
			}
			if(AbstractTable.this.get("total")!=null){
				AbstractTable.this.remove("total");
			}
			add(new WebMarkupContainer("info"));
			table = new WebMarkupContainer("table");
			table.setOutputMarkupId(true);
			add(table);
			
			
			
			cols = columns();
			records = getData(first, count, sortColumn, sortAsc);	
			total=records.total().intValue();
			
			buildColumns();
			buildData();
			buildPaginator();
			
			table.setMarkupId(mid);
		} catch (Exception e) {
			e.printStackTrace();
		
			throw new RuntimeException("Error in AbstractTable build method",e);
		}
	}
	
	private void buildColumns()throws Exception{
		RepeatingView view = new RepeatingView("th");
		table.add(view);
		
		if(withCheckboxColumn){
			colsHash = "chk";
			
			WebMarkupContainer wmc = new WebMarkupContainer(view.newChildId());
			view.add(wmc);
			WebMarkupContainer srtChk = new WebMarkupContainer("sort");
			srtChk.add(new WebMarkupContainer("ttl"));
			srtChk.setVisible(false);
			wmc.add(srtChk);
			
			FAjaxCheckboxField chk = new FAjaxCheckboxField("col-name", new Model<Boolean>()){

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
			wmc.add(chk);
			
			wmc.add(new AttributeModifier("style", "width:30px;"));
			wmc.add(new AttributeModifier("resizable", "off"));
		}
		
		for(final AColumn<T> col:cols){
			if(col!=null){
				colsHash=colsHash+"_"+col.getTitle();
				
				WebMarkupContainer wmc = new WebMarkupContainer(view.newChildId());
				view.add(wmc);
				if(col.getWidth()>0)
					wmc.add(new AttributeModifier("width", col.getWidth()+"px"));
				
				Label l =  new Label("col-name",col.getTitle());
				wmc.add(l);
				
				
				if(isSortable() && col.getName()!=null && col.getName().trim().length()>0){
					AjaxLink<Void> sort = new AjaxLink<Void>("sort") {
						
						@Override
						public void onClick(AjaxRequestTarget target) {
							if(sortColumn!=null && sortColumn.equals(col.getName()))
								sortAsc = !sortAsc;
							else 
								sortAsc = true;
							
							sortColumn = col.getName();
							
							try{
								build();
								target.add(AbstractTable.this);
							}catch(Exception ex){
								ex.printStackTrace();
								target.appendJavaScript("alert('"+ExceptionUtils.fullError(ex)+"');");	
							}
							
						}
					};
					wmc.add(sort);
					sort.add(new Label("ttl",col.getTitle()));
					l.setVisible(false);
					if(sortColumn!=null && sortColumn.trim().length()>0){
						if(sortColumn.equals(col.getName())){
							sort.add(new AttributeModifier("class", "col-sort-"+(sortAsc?"asc":"desc")));
						}else{
							sort.add(new AttributeModifier("class", "col-sort-none"));
						}
					}
					
					
					
				}else{
					wmc.add(new WebMarkupContainer("sort").setVisible(false));
				}
				
			}
		}
		colsHash=""+colsHash.hashCode();
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
				if(col==null)
					continue;
				
				
				String cid = td.newChildId();
				Component cell = col.cell(cid, t); 
				td.add(cell);
				
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
	
	public ITable<T> getData(int first,int count, String sortColumn, boolean sortAsc)throws Exception{
		return data(first, count);
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



	@Override
	public void renderHead(IHeaderResponse response) {
		
		
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript("ResizableColumns('"+table.getMarkupId()+"');"));
	}
	
	

	

}
