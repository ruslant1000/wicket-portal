package kz.tem.portal.api.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ThemeInfo implements Serializable{

	private String name;
	
	private String fileName;
	
	/**
	 * ������� ����, ��� � HTML ����� ���� ����� wicket:id="layout"
	 */
	private boolean layout = false;
	/**
	 * ������� ����, ��� � HTML ����� ���� ����� wicket:id="menu"
	 */
	private boolean menu = false; 
	/**
	 * ������� ����, ��� � HTML ����� ���� ����� wicket:id="menu-link"
	 */
	private boolean menuLink = false;
	/**
	 * ������� ����, ��� � HTML ����� ���� ����� wicket:id="sub-menu"
	 */
	private boolean subMenu = false;
	/**
	 * ������� ����, ��� � HTML ����� ���� ����� wicket:id="sub-menu-link"
	 */
	private boolean subMenuLink = false;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLayout() {
		return layout;
	}

	public void setLayout(boolean layout) {
		this.layout = layout;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public boolean isMenu() {
		return menu;
	}

	public void setMenu(boolean menu) {
		this.menu = menu;
	}

	public boolean isMenuLink() {
		return menuLink;
	}

	public void setMenuLink(boolean menuLink) {
		this.menuLink = menuLink;
	}

	public boolean isSubMenu() {
		return subMenu;
	}

	public void setSubMenu(boolean subMenu) {
		this.subMenu = subMenu;
	}

	public boolean isSubMenuLink() {
		return subMenuLink;
	}

	public void setSubMenuLink(boolean subMenuLink) {
		this.subMenuLink = subMenuLink;
	}

	public String toString(){
		return name;
	}
}
