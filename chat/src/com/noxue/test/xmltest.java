package com.noxue.test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.noxue.dao.*;
import com.noxue.javabean.User;

public class xmltest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		User user1 = new User();
		
		user1.setName("noxue");
		user1.setPassword("123456");
		user1.setEmail("30670835@qq.com");
		user1.setSex("male");
		
		user u = new user();
		u.adduser(user1);
		
		user u1 = new user();
		try {
			System.out.println(u1.getMaxId());
		} catch (DocumentException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int createXMLFile(String filename){
	       /** ���ز������, 0��ʧ��, 1��ɹ� */
	       int returnValue = 0;
	       /** ����document���� */
	       Document document = DocumentHelper.createDocument();
	       /** ����XML�ĵ��ĸ�books */
	       Element booksElement = document.addElement("books");
	       /** ����һ��ע�� */
	       booksElement.addComment("This is a test for dom4j, holen, 2004.9.11");
	       /** �����һ��book�ڵ� */
	       Element bookElement = booksElement.addElement("book");
	       /** ����show�������� */
	       bookElement.addAttribute("show","yes");
	       /** ����title�ڵ� */
	       Element titleElement = bookElement.addElement("title");
	       /** Ϊtitle�������� */
	       titleElement.setText("Dom4j Tutorials");
	      
	       /** ���Ƶ���ɺ�����book */
	       bookElement = booksElement.addElement("book");
	       bookElement.addAttribute("show","yes");
	       titleElement = bookElement.addElement("title");
	       titleElement.setText("Lucene Studing");
	       bookElement = booksElement.addElement("book");
	       bookElement.addAttribute("show","no");
	       titleElement = bookElement.addElement("title");
	       titleElement.setText("Lucene in Action");
	      
	       /** ����owner�ڵ� */
	       Element ownerElement = booksElement.addElement("owner");
	       ownerElement.setText("O'Reilly");
	      
	       try{
	           /** ��document�е�����д���ļ��� */
	           XMLWriter writer = new XMLWriter(new FileWriter(new File(filename)));
	           writer.write(document);
	           writer.close();
	           /** ִ�гɹ�,�践��1 */
	           returnValue = 1;
	       }catch(Exception ex){
	           ex.printStackTrace();
	       }
	             
	       return returnValue;
	    }

}
