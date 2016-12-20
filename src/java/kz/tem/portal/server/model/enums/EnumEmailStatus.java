package kz.tem.portal.server.model.enums;

public enum EnumEmailStatus {

	CREATED("Создано"),SENDED("Отправлено"),SEND_ERROR("Ошибка отправки"), FATAL_ERROR("Ошибка сообщения");
	
	private String description;
	
	private EnumEmailStatus(String description){
		this.description=description;
	}
	
	public String toString(){
		return description;
	}
}
