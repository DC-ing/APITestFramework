package com.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件封装类，输出日志类
 * 
 * @version 1.1
 * 
 */

public class FileUtils {
	
	private static Logger logger = LogManager.getLogger();

	//构造函数，创建此对象，无参数
	public FileUtils() {
		
	}

	/**
	 * 将读取文件的 FileInputStream 提取出来，单独一个方法
	 * 
	 * @param fileFullPath 文件路径
	 * 
	 * @return FileInputStream 对象
	 */
	public FileInputStream setFileInputStream(String fileFullPath) {
		//创建一个 File 对象，设置默认值为空
		File file = null;
		//创建一个 FileInputStream 对象，用来读取文件
        FileInputStream fis = null;
		try {
        	file = new File(fileFullPath);    		//读取完成的文件路径
            fis = new FileInputStream(file);    	//读取所在文件路径的文件
            logger.info("构建 FileInputStream 成功");
		} catch (IOException e) {
			logger.error("构建 FileInputStream 失败", e);
		}
		return fis;
	}

	/**
	 * 读取文件内容
	 *
	 * @param fileFullPath 文件路径
	 *
	 * @return File 文本
	 */
	public static List<String> readFile(String fileFullPath) {
		List<String> stringList = new ArrayList<>();

		File keyFile = new File(fileFullPath);
		String readKey = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile)));
			while ((readKey = br.readLine())!= null) {
				stringList.add(readKey);
			}
			br.close();
		} catch (FileNotFoundException e) {
			logger.error("找不到文件:" + fileFullPath + "\n", e);
		} catch (IOException e) {
			logger.error("读取文件[" + fileFullPath + "]，发生异常\n", e);
		}
		logger.info("读取到的内容有：" + stringList);
		return stringList;
	}

	/**
	 * 将字符串写入到指定文件中
	 *
	 * @param content 写入内容
	 * @param filePath 文件路径
	 */
	public static void writeFile(String content, String filePath) {
		//先创建文件，再写入
		createFile(filePath);
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(filePath, true);

			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println(content);
			printWriter.println("\n");
			printWriter.flush();
			fileWriter.flush();

			printWriter.close();
			fileWriter.close();

		} catch(IOException e) {
			logger.catching(e);
		}
	}

	/**
	 * 如果文件不存在，先创建
	 * 
	 * @param fileFullName 文件路径
	 * 
	 */
	public static void createFile(String fileFullName) {
		File newFile = new File(fileFullName);
		try {
			if (!newFile.exists()) {
				File parent = newFile.getParentFile();
				if (!parent.exists()) {		//判断路径是否存在，不存在，则创建路径
					parent.mkdirs();
				}
				newFile.createNewFile();	//文件不存在，创建空白文件
				logger.debug("创建文件：" + fileFullName);
			}
			else {
				logger.debug("文件已存在");
			}
		} catch (IOException e) {
			logger.catching(e);
		}
	}
	
	/**
	 * 删除指定文件
	 * 
	 * @param filePath 文件路径
	 * 
	 */
	public static void deleteFile(String filePath) {
		File deleteFile = new File(filePath);
		if (deleteFile.exists()) {
			if (deleteFile.delete()) {
				logger.debug("删除文件 [" + filePath + "] 成功");
			} else {
				logger.debug("删除文件 [" + filePath + "] 失败");
			}
		} else {
			 logger.debug("文件 [" + filePath + "] 不存在");
		}
	}
	
	/**
	 * 获取指定类的完整路径
	 * 
	 * @param clazz 类的路径
	 * 
	 * @return 调用 Class 的路径
	 * 
	 */
	public static String getClassPath(Class<?> clazz) {
		return clazz.getCanonicalName().replaceAll("\\.", File.separator);
	}

	/**
	 * 以 src/开头的路径变为绝对路径
	 *
	 * @param simplePath 相对路径
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String simplePath) {
		if (simplePath.startsWith(File.separator + "src" + File.separator)) {
			simplePath = System.getProperty("user.dir") + simplePath;
		}
		return simplePath;
	}

}
