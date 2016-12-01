package kz.tem.portal.server.plugin.engine;

public class Test {
public static void main(String[] args) {
	String s = "kz.tem.portal.msystext.server.register.impl.CategoryRegisterImpl$$EnhancerBySpringCGLIB$$22989903";
	if(s.contains("$$EnhancerBySpringCGLIB$$")){
		System.out.println("ok");
		System.out.println(s.split("\\$\\$EnhancerBySpringCGLIB\\$\\$")[0]);
	}
}
}
