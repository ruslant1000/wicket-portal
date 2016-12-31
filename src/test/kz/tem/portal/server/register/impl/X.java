package kz.tem.portal.server.register.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class X {

	public static void main(String[] args) throws Exception{
//		System.out.println("kz.tem.portal.msystext.sdd.ff.Csddf".split("\\.")[3]);
		BufferedReader br = new BufferedReader(new FileReader("C:/tmp/sss.txt"));
		
		Set<String> ss = new HashSet<String>();
		int i=0;
		String s = null;
		while (br.ready()){
			String line = br.readLine();
			if(ss.contains(line.trim().replaceAll(" ", ""))){
				continue;
			}
			i++;
			ss.add(line.trim().replaceAll(" ", ""));
			s=(s==null?"":s+";")+line.trim().replaceAll(" ", "");
			if(i==25){
				i=0;
				System.out.println(s);
				System.out.println();
				s=null;
			}
		}
	}
}
