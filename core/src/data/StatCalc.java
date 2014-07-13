package data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * This is a helper class that is used to compute program statistics.
 * @author bharat
 *
 */
public class StatCalc {
	static ArrayList<File> fileList;
	static HashMap<File,HashSet<File>> deps;
	static HashMap<File,Integer> fLen;
	static String[] legalEnds={" ",",",">",".","("}; //These are the characters that a type name can end with.
	public static void readFile() {	
		fLen=new HashMap<File,Integer>();
		deps=new HashMap<File,HashSet<File>>();
		fileList=new ArrayList<File>();
		String path="C:/Users/Bharat/workspace/DotWarsv2/src";	
		File base=new File(path);
		for(String s:base.list())
		{
			searchFiles(new File(path,s));
		}
		Iterator<File> fileIter=fileList.iterator();
		while(fileIter.hasNext())
		{
			String name=fileIter.next().getName();
			if(name.lastIndexOf(".java")==-1&&name.lastIndexOf(".scala")==-1)
			{
				fileIter.remove();
			}
		}
		analyzeFiles();
	}
	
	/**
	 * This helper method analyzes files.
	 */
	public static void analyzeFiles()
	{
		int totalLength=0;
		int codeLength=0;
		int javaCount=0;
		ArrayList<String> lineList=new ArrayList<String>();
		for(File f: fileList)
		{
			int fileCodeLength=0;	
			deps.put(f,new HashSet<File>());
			try {
				List<String> s=Files.readAllLines(Paths.get(f.getAbsolutePath()), StandardCharsets.UTF_8);
				totalLength+=s.size();
				for(String str:s)
				{
					if(str.trim().length()>1)
					{
						codeLength++;
						if(str.lastIndexOf(";")!=-1)
						{
							testLine(str,f);
							lineList.add(str);
							javaCount++;
							fileCodeLength++;
						}
						else if(str.lastIndexOf("/*") ==-1&&str.lastIndexOf("*")==-1&&str.lastIndexOf("*/")==-1&&str.lastIndexOf("//")==-1)
						{
							testLine(str,f);
							lineList.add(str);
							javaCount++;
							fileCodeLength++;
						}				
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fLen.put(f, fileCodeLength);		
		}

	}	
	/**
	 * Finds dependency
	 * @param line String from other file
	 */
	public static void testLine(String line,File f)
	{
		if(f.getName().equals("Location.scala"))
		{
			return;
		}
		if(line.length()<1)
		{
			return;
		}
		for(File otherFile: fileList)
		{
			if(otherFile.getName().equals("Location.scala"))
			{
				continue;
			}
			String fileName=otherFile.getName();
			if(fileName.contains("test"))
			{
				continue;
			}	
			String typeName=fileName.split("[.]")[0];
			for(String s:legalEnds)
			{
				if(line.contains(typeName+s))
				{
					 if(s.equals("("))
					 {
						 for(String st:legalEnds)
						 {
							 if(line.contains(st+typeName))
							 {
								 deps.get(f).add(otherFile);
								 break;
							 }
						 }
					 }
					 else
					 {
						 deps.get(f).add(otherFile);
						 break;
					 }
				}
			}	
		}
	}
	/**
	 * This is another helper method.
	 * @param dir
	 */
	public static void searchFiles(File dir)
	{
		if(dir==null)
		{
			return;
		}
		File f=new File(dir.getAbsolutePath());
		String[] children=f.list();
		if(children==null)
		{
			return;
		}
		for(String fName: children)
		{
			File next=new File(dir,fName);
			if(next.isFile())
			{	
				fileList.add(new File(dir,fName));
			}
			if(dir.isDirectory())
			{
				searchFiles(new File(dir,fName));
			}
		}
	}
}
