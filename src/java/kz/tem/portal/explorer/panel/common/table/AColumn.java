package kz.tem.portal.explorer.panel.common.table;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kz.tem.portal.explorer.panel.common.form.IForm;
import kz.tem.portal.utils.ExceptionUtils;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class AColumn<T> implements Serializable{
	
	
	private String title;
	private String name;
	/**
	 * Для автоматического подбора компоненты в режиме редактирования
	 */
	private AColumnEditType editType = AColumnEditType.UNCKNOWN;
	private boolean required;
	private int width = 100;
	
	public AColumn(String title, String name, AColumnEditType editType, boolean required, int width) {
		super();
		this.title = title;
		this.name = name;
		this.editType=editType;
		this.required=required;
		this.width=width;
	}
	public AColumn(String title, String name, AColumnEditType editType, boolean required) {
		super();
		this.title = title;
		this.name = name;
		this.editType=editType;
		this.required=required;
	}
	public AColumn(String title, String name, AColumnEditType editType) {
		super();
		this.title = title;
		this.name = name;
		this.editType=editType;
	}
	public AColumn(String title, String name) {
		super();
		this.title = title;
		this.name = name;
	}
	public AColumn(String title) {
		super();
		this.title = title;
	}
	public AColumn(String title, int width) {
		super();
		this.title = title;
		this.width=width;
	}
	
	public abstract Component cell(String id, T record)throws Exception;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AColumnEditType getEditType() {
		return editType;
	}
	public void setEditType(AColumnEditType editType) {
		this.editType = editType;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void addReadField(IForm form, T entity){
		try{
			form.addReadOnlyComponent(this, entity);
		}catch(Exception ex){
			ex.printStackTrace();
			form.addLabel(title,ExceptionUtils.fullError(ex));
		}
	}
	
	public void addEditField(IForm form, T entity){
		try{
			if(readOnly){
				form.addReadOnlyComponent(this, entity);
			}else if(editType.equals(AColumnEditType.STRING)){
				form.addFieldString(title, model(entity), required);
			}else if(editType.equals(AColumnEditType.COMBOBOX)){
				form.addCombobox(title, model(entity), choices(), required);
			}else if(editType.equals(AColumnEditType.AREA)){
				form.addFieldArea(title, model(entity), required);
			}else if(editType.equals(AColumnEditType.DATE)){
				form.addFieldDate(title, model(entity),datePattern(), required);
			}else{
				form.addLabel(title, "Неизвестный тип поля: "+editType);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			form.addLabel(title,ExceptionUtils.fullError(ex));
		}
	}
	
	public IModel model(T record){
		return new PropertyModel<String>(record, name);
	}
	
	public List choices()throws Exception{
		throw new Exception("Not implemented yet.");
	}
	public String datePattern() throws Exception{
		return "yyyy.MM.dd";
	}
	private boolean readOnly = false;
	public AColumn readOnly(boolean status){
		readOnly = status;
		return AColumn.this;
	}

	
}
