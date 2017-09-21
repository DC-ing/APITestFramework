package com.utils;

import com.interfaceData.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * 读写 Excel 中不同 sheet 的表数据
 * 
 * @version 2.0
 * 
 */

public class ExcelUtils {
	
	private static Logger logger = LogManager.getLogger();
	
	public final static String XLSX = "xlsx";
	public final static String CSV = "csv";
	public final static String PUBLIC_PARAMETERS_SHEET = "公共参数";

	private String filePath = "";
	private static String fileFullPath;
	private String fileName;
	
	private Workbook workbook = null;
	
	/**
	 * 读取 excel 的构造函数，传进完成的文件路径，包含文件名称
	 * 
	 * @param fileFullPath 包含文件名称的完整路径
	 * 
	 */
	public ExcelUtils(String fileFullPath) {
		ExcelUtils.fileFullPath = fileFullPath;
		logger.info("Excel 文件绝对路径：" + fileFullPath);
		String regex = File.separator;
		//不同系统，分割的字符不一样
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			regex = "\\\\";
		}
		String[] breakupString = StringUtils.getSplitString(fileFullPath, regex);
		fileName = breakupString[breakupString.length - 1];
		logger.info("Excel 文件名称：" + fileName);
		for (int i = 0; i < breakupString.length - 1; i ++) {
			filePath = filePath + breakupString[i] + File.separator;
		}
		logger.info("Excel 所在的文件路径：" + filePath);
		this.getExcelFile(fileFullPath);
	}

	/**
	 * 打开 Excel 文件，并返回一个 FileInputStream 对象
	 *
	 * @param fileFullPath Excel文件的绝对路径
	 *
	 * @return FileInputStream
	 */
	public FileInputStream getExcelFile(String fileFullPath) {
		FileInputStream fis = new FileUtils().readFile(fileFullPath);
		try {
			if (this.getExcelType().equalsIgnoreCase(XLSX)) {
				workbook = new XSSFWorkbook(fis); 						//读取excel文件
			}
		} catch (IOException e) {
			logger.error("打开文件时\"" + fileFullPath + "\"出现错误。\n", e);
		}
		return fis;
	}

	/**
	 * 将输入流关闭
	 *
	 * @param fis 输入流
	 */
	public void closeExcelFile(InputStream fis) {
		try {
			fis.close();
		} catch (IOException e) {
			logger.error("关闭文件出现错误。\n", e);
		}
	}

	/**
	 * 获取 Excel 文件的类型，如果不是 Excel 文件，返回空指针
	 * 
	 * @return Excel 文件类型，如：xls、xlsx等
	 */
	public String getExcelType() {
		if (fileName.toLowerCase().endsWith(XLSX)) {
			return XLSX;
		} else if (fileName.toLowerCase().endsWith(CSV)) {
			return CSV;
		} else {
			logger.error("所读文件不是为 Excel 文件", new FileNotFoundException("This file isn't excel."));
			return null;
		}
	}

	/**
	 * 获得 Excel 文件的所有 sheet 名字
	 * 
	 * @return Excel 的 sheetName 数组
	 */
	public List<String> getSheetNames() {
		//用一个字符串数组，存储 excel 的 sheet 名字
		int sheetNumbers = workbook.getNumberOfSheets();
		List<String> sheetNames = new ArrayList<>(sheetNumbers);
		String sheetNameLog = "[" + fileName + "] 文件有 " + sheetNumbers + " 个表，表名：\"";
		for (int i = 0; i < sheetNumbers; i++) {
			sheetNames.add(workbook.getSheetName(i));
			//log描述
			sheetNameLog = sheetNameLog + sheetNames.get(i) + "\"";
			if (i < sheetNumbers - 1) {
				sheetNameLog = sheetNameLog + ", \"";
			} else {
				sheetNameLog = sheetNameLog + " ";
			}
		}
		logger.info(sheetNameLog);
		return sheetNames;
	}
	
	/**
	 * 判断 sheet 是否存在
	 *
	 * @param sheetName 表名
	 * @return 指定的 sheet 是否存在
	 */
	public boolean isSheetExist(String sheetName) {
		List<String> sheetNames = getSheetNames();
		boolean isExist = false;
		for (String name : sheetNames) {
			if (sheetName.equalsIgnoreCase(name)) {
				isExist = true;
			}
		}
		//日志输出
		if (isExist) {
			logger.info("表：\"" + sheetName + "\" 存在");
		} else {
			logger.info("表：\"" + sheetName + "\" 不存在");
		}
		return isExist;
	}

	/**
	 * 获得一个接口测试的全部数据，
	 * 包括：路径，描述，参数组合
	 *
	 * @param sheetName 表名（接口名称）
	 * @return 接口测试的全部数据
	 */
	public InterfaceData getInterfaceData(String sheetName) {
		//如果找不到指定的接口，返回接口数据为空
		if (!this.isSheetExist(sheetName)) {
			logger.error("无法查找到接口 [" + sheetName + "] 的测试数据。");
			return null;
		}

		//接口测试用例模板，接口路径放在第一行的第二列
		String path = this.getCellData(sheetName, 0, 1);
		//接口测试用例模板，接口描述在第二行的第二列
		String desc = this.getCellData(sheetName,1, 1);

		final int paramKeyRowNum = 3;
		Sheet sheet = workbook.getSheet(sheetName);
		//接口测试用例模板，接口参数放在第四行
		int rowNums = sheet.getLastRowNum();
		int cellNums = sheet.getRow(paramKeyRowNum).getPhysicalNumberOfCells();
		Row row;
		Cell cell;

		//获取参数表头的值
		String[] paramKeys = this.getRowData(sheetName, paramKeyRowNum, cellNums);

		//获取参数名
		List<String> paramNames = new ArrayList<>();
		//默认不读取第一个，接口的调用方法
		for (int i = 1; i < paramKeys.length; i++) {
			String param = paramKeys[i];
			//忽略除参数名以外的其他字段
			if (param.equals(InterfaceParameters.EXPECT) ||
				param.equals(InterfaceParameters.ACTUAL) ||
				param.equals(InterfaceParameters.IS_PASS) ||
				param.equals(InterfaceParameters.FULL_URL) ||
				param.equals(InterfaceParameters.EXTRACTED_PUBLIC_PARAMETERS)
					){
				continue;
			} else {
				paramNames.add(param);
			}
		}

		//初始化接口的基本信息
		InterfaceInfo interfaceInfo = new InterfaceInfo(sheetName, path, desc, paramNames);
		//初始化接口的参数组合信息
		List<InterfaceParameters> parametersList = new ArrayList<>(rowNums + 1);

		//两个循环，遍历所有的参数信息
		for (int i = paramKeyRowNum + 1; i <= rowNums; i++) {
			row = sheet.getRow(i);
			InterfaceParameters parameters = new InterfaceParameters();
			Map<String, String> paramMap = new HashMap<>();
			Map<String, ExcelCellData> writeDatasMap = new HashMap<>();

			for (int j = 0; j < paramKeys.length; j++) {
				cell = row.getCell(j);
				String content = null;
				//判断单元格的值是否为空
				if (cell != null) {
					cell.setCellType(CellType.STRING);
					content = cell.getStringCellValue();
				}

				String pKey = paramKeys[j];
				//第一列为接口调用方法
				if (j == 0) {
					parameters.setMethod(content);
					//接口完整 url
				} else if (pKey.equals(InterfaceParameters.FULL_URL)) {
					writeDatasMap.put(InterfaceParameters.FULL_URL, new ExcelCellData(i, j, content));
					//预期结果读取
				} else if (pKey.equals(InterfaceParameters.EXPECT)) {
					parameters.setExpectResult(content);
					//实际测试结果读取
				} else if (pKey.equals(InterfaceParameters.ACTUAL)) {
					writeDatasMap.put(InterfaceParameters.ACTUAL, new ExcelCellData(i, j, content));
					//测试是否通过结果读取
				} else if (pKey.equals(InterfaceParameters.IS_PASS)) {
					writeDatasMap.put(InterfaceParameters.IS_PASS, new ExcelCellData(i, j, content));
					//提取公共参数
				} else if (pKey.equals(InterfaceParameters.EXTRACTED_PUBLIC_PARAMETERS)) {
					parameters.setExtractedPublicParameters(content);
					//接口参数读取
				} else {
					//读取公共参数，参数名支持大写字母（A-Z）、小写字母（a-z）、数字（0-9）、下划线（_）、破折号（-）
					if (content.startsWith("${") && content.endsWith("}")) {
						String matchString = StringUtils.getMatchString(content, "[a-zA-Z-0-9_\\--\\-]+");
						String publicParameterValue = this.getPublicParameter(matchString);
						if (publicParameterValue != null) {
							content = publicParameterValue;
						}
					}
					paramMap.put(paramKeys[j], content);
				}
			}
			//设置一次参数组合中，需要写入的数据集合
			parameters.setExcelWriteDatas(new ExcelWriteDatas(writeDatasMap));
			//设置接口参数
			parameters.setParameters(paramMap);
			parametersList.add(parameters);
			logger.info("已读取接口参数：" + parameters.toString());
		}
		InterfaceData interfaceData = new InterfaceData(interfaceInfo, parametersList);
		logger.info("获取到接口的具体信息：" + interfaceData);
		return interfaceData;
	}

	/**
	 * 获取接口测试的配置数据，
	 * 包括：http 协议类型，
	 * 		接口测试主 url，
	 * 		测试接口测试数据结果
	 *
	 * @return 接口配置数据
	 */
	public ConfigData getInterfaceConfig() {
		String sheetName = ConfigData.CONFIG_SHEET_NAME;
		//获取所测试接口的协议，在模板中放在第一行第二列
		String scheme = this.getCellData(sheetName, 0, 1);
		//获取接口测试的主 URL，放在第二行的第二列
		String host = this.getCellData(sheetName,1,1);
		//将接口的 http 协议和主 url 封装成一个类
		InterfaceMainURL mainURL = new InterfaceMainURL(scheme, host);

		final int suiteKeyRowNum = 3;
		Sheet sheet = workbook.getSheet(sheetName);
		//接口测试用例模板，接口测试结果表头放在第四行
		int rowNums = sheet.getLastRowNum();
		int cellNums = sheet.getRow(suiteKeyRowNum).getPhysicalNumberOfCells();
		Row row;
		Cell cell;

		List<SuiteConfig> suiteConfigList = new ArrayList<>(rowNums + 1);

		//获取参数表头的值
		String[] paramKeys = this.getRowData(sheetName, suiteKeyRowNum, cellNums);
		//两个循环，遍历所有的参数信息
		for (int i = suiteKeyRowNum + 1; i <= rowNums; i++) {
			row = sheet.getRow(i);
			SuiteConfig suiteConfig = new SuiteConfig();

			for (int j = 0; j < paramKeys.length; j++) {
				cell = row.getCell(j);
				String content = null;
				//判断单元格的值是否为空
				if (cell != null) {
					cell.setCellType(CellType.STRING);
					content = cell.getStringCellValue();
				}
				switch (j) {
					//测试集合 id
					case 0:
						suiteConfig.setId(content);
						break;
					//测试接口名称
					case 1:
						suiteConfig.setInterfaceName(content);
						break;
					//运行模式
					case 2:
						suiteConfig.setRunMode(content);
						break;
					//测试结果
					case 3:
						suiteConfig.setExcelCellData(new ExcelCellData(i, j, content));
						break;
					default:
						break;
				}
			}
			suiteConfigList.add(suiteConfig);
			logger.info(suiteConfig.toString());
		}
		ConfigData configData = new ConfigData(mainURL, suiteConfigList);
		logger.info(configData);
		return configData;
	}

	/**
	 * 设置公共参数
	 *
	 * @param parameters 参数集合
	 *
	 */
	public void setPublicParameters(Map<String, String> parameters) {
		final int Key_Cell_Num = 0;
		final int Value_Cell_Num = 1;

		//不存在公共参数 sheet，则添加
		if (!this.isSheetExist(PUBLIC_PARAMETERS_SHEET)) {
			logger.info("添加表： [" + PUBLIC_PARAMETERS_SHEET + "]");
			workbook.createSheet(PUBLIC_PARAMETERS_SHEET);
		}
		Set<String> parameterKeys = parameters.keySet();
		Sheet publicParametersSheet = workbook.getSheet(PUBLIC_PARAMETERS_SHEET);
		Row row;
		Cell valueCell;
		int lastRowNum = publicParametersSheet.getLastRowNum();
		boolean isContent = false;
		//判断第一行是否有数据
		if (publicParametersSheet.getRow(0) != null) {
			isContent = true;
			//对公共参数 sheet 的值，从第一条开始遍历
			for (int i = 0; i <= lastRowNum; i++) {
				row = publicParametersSheet.getRow(i);
				if (row == null) {
					continue;
				}
				valueCell = row.getCell(Key_Cell_Num);
				String existKey = null;
				if (valueCell != null) {
					valueCell.setCellType(CellType.STRING);
					existKey = valueCell.getStringCellValue();
				}
				//想要加入的公共参数已经存在，则覆盖（此逻辑以后可更改）
				if (parameterKeys.contains(existKey)) {
					valueCell = row.getCell(Value_Cell_Num, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if (valueCell == null) {
						valueCell = row.createCell(Value_Cell_Num);
					}
					String existKeyParameter = parameters.get(existKey);
					valueCell.setCellValue(existKeyParameter);		//向指定的单元格输入内容
					logger.info("覆盖已存在的公共参数 [" + existKey + "]，重新写入值：[" + existKeyParameter + "]");
					//避免二次删除
					parameterKeys.remove(existKey);
				}
			}
		}

		//isContent 为 true，则表明公共参数表原来有数据，需要加1
		if (isContent) {
			lastRowNum ++;
		}
		//将剩下与原有参数不匹配的参数和值写入到公共参数 sheet 里
		Cell keyCell;
		for (String key : parameterKeys) {
			//写入参数的 key 和 value
			row = publicParametersSheet.getRow(lastRowNum);
			if (row == null) {
				row = publicParametersSheet.createRow(lastRowNum);
			}
			//写入参数名称
			keyCell = row.getCell(Key_Cell_Num, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			if (keyCell == null) {
				keyCell = row.createCell(Key_Cell_Num);
			}
			keyCell.setCellType(CellType.STRING);
			//写入参数值
			keyCell.setCellValue(key);valueCell = row.getCell(Value_Cell_Num, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			if (valueCell == null) {
				valueCell = row.createCell(Value_Cell_Num);
			}
			String keyParameter = parameters.get(key);
			valueCell.setCellValue(keyParameter);

			lastRowNum ++;
			logger.info("写入新的公共参数：[" + key + "], 值：[" + keyParameter + "]");
		}
		this.writeWorkbook();
	}

	/**
	 * 获取公共参数
	 *
	 * @param paramName 参数名
	 * @return 公共参数字段
	 */
	public String getPublicParameter(String paramName) {
		String content = null;
		Sheet publicSheet = workbook.getSheet(PUBLIC_PARAMETERS_SHEET);
		int lastRowNum = publicSheet.getLastRowNum();
		Row pRow;
		Cell pCell;
		//查找符合的公共参数，并读取它的值
		for (int pn = 0; pn <= lastRowNum; pn++) {
			pRow = publicSheet.getRow(pn);
			if (pRow != null) {
				pCell = pRow.getCell(0);
				if (pCell != null) {
					pCell.setCellType(CellType.STRING);
					String cellValue = pCell.getStringCellValue();
					if (paramName.equals(cellValue)) {
						pCell = pRow.getCell(1);
						pCell.setCellType(CellType.STRING);
						content = pCell.getStringCellValue();
					}
				}
			}
		}
		return content;
	}

	/**
	 * 将接口测试的结果写入到测试用例文档中
	 *
	 * @param interfaceData 接口测试数据
	 */
	public void writeInterfaceExpectResult(InterfaceData interfaceData) {
		//接口名称
		String interfaceName = interfaceData.getInterfaceInfo().getName();
		//提取接口参数中，写入的测试结果
		List<InterfaceParameters> interfaceParametersList = interfaceData.getParametersList();
		List<ExcelWriteDatas> excelWriteDatasList = new ArrayList<>(interfaceParametersList.size());
		for (int i = 0; i < interfaceParametersList.size(); i++) {
			excelWriteDatasList.add(interfaceParametersList.get(i).getExcelWriteDatas());
		}
		logger.info("正在往 [" + interfaceName + "] 接口写入测试结果");

		this.writeSheetResult(interfaceName, excelWriteDatasList);
	}

	/**
	 * 将测试集合结果写入到文件中
	 *
	 * @param configData 配置数据
	 */
	public void writeSuiteResult(ConfigData configData) {
		//获取测试集合测试结果
		List<SuiteConfig> suiteConfigList = configData.getSuiteConfigList();
		List<ExcelWriteDatas> writeDatasList = new ArrayList<>(suiteConfigList.size());

		for (int i = 0; i < suiteConfigList.size(); i++) {
			ExcelCellData cellData = suiteConfigList.get(i).getExcelCellData();
			Map<String, ExcelCellData> cellDataMap = new HashMap<>();
			cellDataMap.put(SuiteConfig.ACTUAL, cellData);
			writeDatasList.add(new ExcelWriteDatas(cellDataMap));
		}

		logger.info("正在往 [" + ConfigData.CONFIG_SHEET_NAME + "] 表写入测试集合总体结果");
		this.writeSheetResult(ConfigData.CONFIG_SHEET_NAME, writeDatasList);
	}

	/**
	 * 将指定 sheet 的测试结果写入到文件中
	 *
	 * @param sheetName 表名
	 * @param excelWriteDatasList 需写入的测试结果集合
	 */
	private void writeSheetResult(String sheetName, List<ExcelWriteDatas> excelWriteDatasList) {
		Sheet sheet = workbook.getSheet(sheetName);
		Row row;
		Cell cell;

		for (int i = 0; i < excelWriteDatasList.size(); i++) {
			ExcelWriteDatas excelWriteDatas = excelWriteDatasList.get(i);
			//获取写入数据的 key 值集合
			Map<String, ExcelCellData> excelCellDataMap = excelWriteDatas.getWriteDatasMap();
			Set<String> keysSet = excelCellDataMap.keySet();
			Iterator<String> iterator = keysSet.iterator();
			//遍历所有需要写入的参数
			while (iterator.hasNext()) {
				String paramKey = iterator.next();

				ExcelCellData excelCellData = excelCellDataMap.get(paramKey);
				int rowNum = excelCellData.getRowNum();
				int cellNum = excelCellData.getCellNum();
				String suiteResultString = excelCellData.getData();

				row = sheet.getRow(rowNum);			//获取到相应的行数
				cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if (cell == null) {
					cell = row.createCell(cellNum);
				}
				cell.setCellValue(suiteResultString);		//向指定的单元格输入内容
				logger.info("在第 " + (rowNum + 1) + " 行，第 " + (cellNum + 1) + " 列，写入数据：\"" + suiteResultString + "\"");
			}
		}

		logger.info("对 [" + sheetName + "] 表，写入数据成功");
		//写入数据到 Excel 文件中
		this.writeWorkbook();
	}

	/**
	 * 获取一行的测试数据
	 * 
	 * @param sheetName 表名
	 * @param rowNum 行数
	 * @param cells 需要读取的列数目
	 * 
	 * @return 一行的测试用例数据
	 */
	public String[] getRowData(String sheetName, int rowNum, int cells) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row row;
        Cell cell;

        logger.info("正在读取的表名：" + sheetName);
        
		//以 excel 文件一个表的行和列，定义一个二维数组
		String[] rowDatas = new String[cells];					//用来存储表数据的二维数组
		logger.info("正在读取第 " + rowNum + " 行");
		logger.info("需要读取的列数：" + cells);
		
		row = sheet.getRow(rowNum);
		for (int j = 0; j < cells; j++) {							//遍历每一列数据
			cell = row.getCell(j);
			if (cell != null) {										//如果单元格元素不为空，以字符串形式读取
				cell.setCellType(CellType.STRING);
				rowDatas[j] = cell.getStringCellValue();
			} else {												//如果单元格元素为空，则为空
				rowDatas[j] = null;
			}
		}
		return rowDatas;
	}

	/**
	 * 获取 excel 指定表名，指定单元格的数据，行、列数，从0开始
	 * 
	 * @param sheetName 表的名称
	 * @param rowNum 行数
	 * @param cellNum 列数
	 * 
	 * @return 单元格内容
	 */
	public String getCellData(String sheetName, int rowNum, int cellNum) {
		String cellData = null;
		try {
			Sheet sheet = workbook.getSheet(sheetName);
			Row row = sheet.getRow(rowNum);							//获取到相应的行数
			Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);						//获取到相应的列数
			if (cell != null) {										//如果单元格元素不为空，以字符串形式读取
				cell.setCellType(CellType.STRING);
				cellData = cell.getStringCellValue();
			} else {												//如果单元格元素为空，则为空
				cellData = null;
			}
			logger.info("在 sheet: [" + sheetName + "] 的第 " + (rowNum + 1) + " 行，第 " + (cellNum + 1) + " 列，读取数据：\"" + cellData + "\"");
		} catch (Exception e) {
			logger.error("在 sheet: [" + sheetName + "] 的第 " + (rowNum + 1) + " 行，第 " + (cellNum + 1) + " 列，读取数据失败。");
		}
		return cellData;
	}
	
	/**
	 * 写入一个数据到 excel 指定的单元格中（由行、列数标记），行、列数，从0开始
	 * 
	 * @param sheetName 表的名称
	 * @param rowNum 行数
	 * @param cellNum 列数
	 * @param testRsult 测试结果
	 * 
	 */
	private void writeCellData(String sheetName, int rowNum, int cellNum, String testRsult) {

        Sheet sheet = workbook.getSheet(sheetName);		//读取相应sheet
        
        Row row = sheet.getRow(rowNum);			//获取到相应的行数
        Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell == null) {
            cell = row.createCell(cellNum);
        }
        cell.setCellValue(testRsult);		//向指定的单元格输入内容
        logger.info("在 sheet: [" + sheetName + "] 的第 " + (rowNum + 1) + " 行，第 " + (cellNum + 1) + " 列，写入数据：\"" + testRsult + "\"");

        //暂不写入到文件中，等待调用方法写入

	}

	/**
	 * 将缓存的 workbook 写入到实际的文档中
	 *
	 */
	private void writeWorkbook() {
		try {
			FileOutputStream out = new FileOutputStream(fileFullPath); //向指定的excel文件中写数据
			workbook.write(out);
			out.flush();
			out.close();
			logger.info("对 Excel文件写入数据成功。");
		} catch (IOException e) {
			logger.error("写入数据失败。\n", e);
		}
	}
}
