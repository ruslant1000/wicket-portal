package kz.tem.portal.api.model;

import java.io.Serializable;
/**
 * 
 * @author Ruslan Temirbulatov
 *
 */
@SuppressWarnings("serial")
public class ThemeInfo implements Serializable{

	private String name;
	
	private String fileName;
	
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="layout"
	 */
	private boolean layout = false;
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="menu"
	 */
	private boolean menu = false; 
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="menu-link"
	 */
	private boolean menuLink = false;
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="sub-menu"
	 */
	private boolean subMenu = false;
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="sub-menu-link"
	 */
	private boolean subMenuLink = false;
	
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="user"
	 */
	private boolean user = false;
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="user-name"
	 */
	private boolean userName = false;
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="user-logout"
	 */
	private boolean userLogout = false;
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="guest"
	 */
	private boolean guest= false;
	/**
	 * Признак того, что в HTML файле есть метка wicket:id="guest-login"
	 */
	private boolean guestLogin= false;
	
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
	

	public boolean isUser() {
		return user;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

	public boolean isUserName() {
		return userName;
	}

	public void setUserName(boolean userName) {
		this.userName = userName;
	}

	public boolean isUserLogout() {
		return userLogout;
	}

	public void setUserLogout(boolean userLogout) {
		this.userLogout = userLogout;
	}

	public boolean isGuest() {
		return guest;
	}

	public void setGuest(boolean guest) {
		this.guest = guest;
	}

	public boolean isGuestLogin() {
		return guestLogin;
	}

	public void setGuestLogin(boolean guestLogin) {
		this.guestLogin = guestLogin;
	}

	public String toString(){
		return name;
	}
}
