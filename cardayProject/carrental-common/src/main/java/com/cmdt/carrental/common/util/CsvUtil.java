package com.cmdt.carrental.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class CsvUtil{

	public static final String FILENAME = "filename";
	public static final String SHEET = "sheet";
	public static final String HEADER = "header";
	public static final String DATA = "data";
	public static final String SPLITPOINTER = ",";
	
	public static void exportExcel(Map<String, Object> model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		OutputStream fOut = null;
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(); //NOSONAR workbook has been close in finally
			String excelName = model.get(FILENAME)+"";
			//设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开
			response.setContentType("application/vnd.ms-excel");   
			if(BrowserUtil.getBrowserName(request.getHeader("User-Agent").toLowerCase()).equals(BrowserUtil.BROWSER_NAME_FIREFOX)){
				response.setHeader("Content-Disposition", "attachment; filename="+ new String(excelName.getBytes("UTF-8"), "iso-8859-1"));
			}else{
				response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(excelName, "UTF-8"));    
			}
			//产生Excel表头  
			HSSFSheet sheet = workbook.createSheet(model.get(SHEET)+"");  
			HSSFRow header = sheet.createRow(0); // 第0行  
			//产生标题列  
			String headers = model.get(HEADER)+"";
			String[] hdCols = headers.split(SPLITPOINTER);
			for(int k=0;k<hdCols.length;k++){
				header.createCell(k).setCellValue(hdCols[k]);  
			} 
	        //填充数据  
	        int rowNum = 1;
	        if(model.get(DATA) != null){
	        	List<String> dataList = (List)model.get(DATA);   
		        for (Iterator<String> iter = dataList.iterator(); iter.hasNext();) {  
		            String element = iter.next();  
		            HSSFRow row = sheet.createRow(rowNum++);  
		            String[] colData = element.split(",");
					for(int k=0;k<colData.length;k++){
						 HSSFCell cell = row.createCell(k);
						 cell.setCellValue(colData[k]);  
					}
		        }
	        }
			fOut = response.getOutputStream();     
	        workbook.write(fOut);
	        
		}catch(Exception e){
			throw new Exception("CSVUtilsView.exportExcel failure due to exception!", e);
		}finally{
			if (null != fOut) {
				fOut.flush();
				fOut.close();
			}
			workbook.close();
		}
	}
	
	public static File exportExcel(Map<String, Object> model) throws Exception {
		OutputStream fOut = null;
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(); //NOSONAR workbook has been close in finally
			String excelName = model.get(FILENAME)+"";
			File file =new File(excelName);
			//产生Excel表头  
			HSSFSheet sheet = workbook.createSheet(model.get(SHEET)+"");  
			HSSFRow header = sheet.createRow(0); // 第0行  
			//产生标题列  
			String headers = model.get(HEADER)+"";
			String[] hdCols = headers.split(SPLITPOINTER);
			for(int k=0;k<hdCols.length;k++){
				header.createCell(k).setCellValue(hdCols[k]);  
			} 
	        //填充数据  
	        int rowNum = 1;
	        if(model.get(DATA) != null){
	        	List<String> dataList = (List)model.get(DATA);   
		        for (Iterator<String> iter = dataList.iterator(); iter.hasNext();) {  
		            String element = iter.next();  
		            HSSFRow row = sheet.createRow(rowNum++);  
		            String[] colData = element.split(",");
					for(int k=0;k<colData.length;k++){
						 HSSFCell cell = row.createCell(k);
						 cell.setCellValue(colData[k]);  
					}
		        }
	        }
			fOut = new FileOutputStream(file);   
	        workbook.write(fOut);
	        return file;
	          
		}catch(Exception e){
			throw new Exception("CSVUtilsView.exportExcel failure due to exception!", e);
		}finally{
			if (null != fOut) {
				fOut.flush();
				fOut.close();
			}
			if(null != workbook){
				workbook.close();
			}
			
		}
	}
	
	public static void exportExcelWithMultiSheet(String fileName,List<ExportFileBean> exportFileBeanList,Map<String, Object> model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		OutputStream fOut = null;
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(); //NOSONAR workbook has been close in finally
			//设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开  
			response.setContentType("application/vnd.ms-excel");  
			if(BrowserUtil.getBrowserName(request.getHeader("User-Agent").toLowerCase()).equals(BrowserUtil.BROWSER_NAME_FIREFOX)){
				response.setHeader("Content-Disposition", "attachment; filename="+ new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
			}else{
				response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(fileName, "UTF-8"));    
			}  
			populateSheet(workbook,exportFileBeanList);
			fOut = response.getOutputStream();     
	        workbook.write(fOut);
		}catch(Exception e){
			throw new Exception("CSVUtilsView.exportExcel failure due to exception!", e);
		}finally{
			if (null != fOut) {
				fOut.flush();     
				fOut.close();
			}
			if(null != workbook){
				workbook.close();
			}
		}
	}
	
	public static File exportExcelWithMultiSheet(String fileName,List<ExportFileBean> exportFileBeanList) throws Exception {
		OutputStream fOut = null;
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(); //NOSONAR workbook has been close in finally
			File file =new File(fileName);
			populateSheet(workbook,exportFileBeanList);
			fOut = new FileOutputStream(file);    
	        workbook.write(fOut);
	        return file;
		}catch(Exception e){
			throw new Exception("CSVUtilsView.exportExcel failure due to exception!", e);
		}finally{
			if (null != fOut) {
				fOut.flush();     
				fOut.close();
			}
			if(null != workbook){
				workbook.close();
			}
		}
	}
	
	private static void populateSheet(HSSFWorkbook workbook,List<ExportFileBean> exportFileBeanList){
		for(ExportFileBean exportFileBean : exportFileBeanList){
			//产生Excel表头  
			HSSFSheet sheet = workbook.createSheet(exportFileBean.getSheet());
			HSSFRow header = sheet.createRow(0);
			//产生标题列
			String headers = exportFileBean.getHeader();
			String[] hdCols = headers.split(SPLITPOINTER);
			for(int k=0;k<hdCols.length;k++){
				header.createCell(k).setCellValue(hdCols[k]);  
			} 
			//填充数据 
			int rowNum = 1;
			List<String> dataList = exportFileBean.getData();
			if(dataList != null && dataList.size() > 0){
			    for (Iterator<String> iter = dataList.iterator(); iter.hasNext();) {  
		            String element = iter.next();  
		            HSSFRow row = sheet.createRow(rowNum++);  
		            String[] colData = element.split(",");
					for(int k=0;k<colData.length;k++){
						 HSSFCell cell = row.createCell(k);
						 cell.setCellValue(colData[k]);  
					}
		        }
			}
		}
	}
	
	public static void exportExcel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response,String SPLITPOINTER) throws Exception {
		OutputStream fOut = null;
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(); //NOSONAR workbook has been close in finally
			String excelName = model.get(FILENAME)+"";
			//设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开  
			response.setContentType("application/vnd.ms-excel");  
			if(BrowserUtil.getBrowserName(request.getHeader("User-Agent").toLowerCase()).equals(BrowserUtil.BROWSER_NAME_FIREFOX)){
				response.setHeader("Content-Disposition", "attachment; filename="+ new String(excelName.getBytes("UTF-8"), "iso-8859-1"));
			}else{
				response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(excelName, "UTF-8"));    
			}      
			//产生Excel表头  
			HSSFSheet sheet = workbook.createSheet(model.get(SHEET)+"");  
			HSSFRow header = sheet.createRow(0); // 第0行  
			//产生标题列  
			String headers = model.get(HEADER)+"";
			String[] hdCols = headers.split(SPLITPOINTER);
			for(int k=0;k<hdCols.length;k++){
				header.createCell(k).setCellValue(hdCols[k]);  
			} 
	        //填充数据  
	        int rowNum = 1;
	        if(model.get(DATA) != null){
	        	List<String> dataList = (List)model.get(DATA);   
		        for (Iterator<String> iter = dataList.iterator(); iter.hasNext();) {  
		            String element = iter.next();  
		            HSSFRow row = sheet.createRow(rowNum++);  
		            String[] colData = element.split(SPLITPOINTER);
					for(int k=0;k<colData.length;k++){
						 HSSFCell cell = row.createCell(k);
						 cell.setCellValue(colData[k]);  
					}
		        }
	        }
			fOut = response.getOutputStream();     
	        workbook.write(fOut);
		}catch(Exception e){
			throw new Exception("CSVUtilsView.exportExcel failure due to exception!", e);
		}finally{
			if (null != fOut) {
				fOut.flush();     
				fOut.close();
			}
			workbook.close();
		}
	}
	
	public static File exportExcel(Map<String, Object> model,String SPLITPOINTER) throws Exception {
		OutputStream fOut = null;
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(); //NOSONAR workbook has been close in finally
			String excelName = model.get(FILENAME)+"";
			File file =new File(excelName); 
			//产生Excel表头  
			HSSFSheet sheet = workbook.createSheet(model.get(SHEET)+"");  
			HSSFRow header = sheet.createRow(0); // 第0行  
			//产生标题列  
			String headers = model.get(HEADER)+"";
			String[] hdCols = headers.split(SPLITPOINTER);
			for(int k=0;k<hdCols.length;k++){
				header.createCell(k).setCellValue(hdCols[k]);  
			} 
	        //填充数据  
	        int rowNum = 1;
	        if(model.get(DATA) != null){
	        	List<String> dataList = (List)model.get(DATA);   
		        for (Iterator<String> iter = dataList.iterator(); iter.hasNext();) {  
		            String element = iter.next();  
		            HSSFRow row = sheet.createRow(rowNum++);  
		            String[] colData = element.split(SPLITPOINTER);
					for(int k=0;k<colData.length;k++){
						 HSSFCell cell = row.createCell(k);
						 cell.setCellValue(colData[k]);  
					}
		        }
	        }
			fOut = new FileOutputStream(file);    
	        workbook.write(fOut);
	        return file;
		}catch(Exception e){
			throw new Exception("CSVUtilsView.exportExcel failure due to exception!", e);
		}finally{
			if (null != fOut) {
				fOut.flush();     
				fOut.close();
			}
			if(null != workbook){
				workbook.close();
			}
		}
	}
	
	/**
     * 导入
     * @param file文件
     * @return
     */
	public static List<Object[]> importFile(MultipartFile file) throws Exception {
    	return importFile(file,0);
    }
	
	/**
     * 导入
     * @param file文件
     * @return
     */
    public static List<Object[]> importFile(MultipartFile file,int startRow) throws Exception {
    	List<Object[]> dataList=new ArrayList<Object[]>();
    	String fileNa=file.getOriginalFilename()!=null&&file.getOriginalFilename().length()>0?file.getOriginalFilename().toLowerCase():file.getName().toLowerCase();
    	String suffix1=".xls";
    	String suffix2=".xlsx";
    	String suffix3=".csv";
    	if(fileNa.endsWith(suffix1)){
    		dataList=importXls(file,startRow);
    	}else if(fileNa.endsWith(suffix2)){
    		dataList=importXlsx(file,startRow);
    	}else if(fileNa.endsWith(suffix3)){
    		dataList=importCsv(file);
    	}
    	return dataList;
    }
	
	/**
     * 导入
     * @param file xls文件
     * @return
     */
    public static List<Object[]> importXls(MultipartFile file,int startRow) throws Exception {
    	List<Object[]> dataList=new ArrayList<Object[]>();
    	HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
    	int numberOfSheets = hssfWorkbook.getNumberOfSheets();
		for (int numSheet = 0; numSheet < numberOfSheets; numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null)
				continue;
			int colsNum=0;
			HSSFRow hssfRow0 = hssfSheet.getRow(startRow);
			colsNum=hssfRow0.getLastCellNum();
			// 循环行Row
			for (int rowNum = startRow+1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null)
					continue;
				List<Object> datas=new ArrayList<Object>();
				for(int c=0;c<colsNum;c++){
					datas.add(getVal(hssfRow.getCell(c)));
				}
				dataList.add(datas.toArray());
			}
		}
		hssfWorkbook.close();
    	return dataList;
    }
	
    /**
     * 导入
     * @param file xlsx文件
     * @return
     */
    public static List<Object[]> importXlsx(MultipartFile file,int startRow) throws Exception {
    	List<Object[]> dataList=new ArrayList<Object[]>();
    	XSSFWorkbook xworkbook = new XSSFWorkbook(file.getInputStream());
    	int numberOfSheets = xworkbook.getNumberOfSheets();
		for (int numSheet = 0; numSheet < numberOfSheets; numSheet++) {
			XSSFSheet sheet = xworkbook.getSheetAt(numSheet);
			if (sheet == null)
				continue;
			int colsNum=0;
			XSSFRow row0 = sheet.getRow(startRow);
			colsNum=row0.getLastCellNum();
			// 循环行Row
			for (int rowNum = startRow+1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				if (row == null)
					continue;
				List<Object> datas=new ArrayList<Object>();
				for(int c=0;c<colsNum;c++){
					datas.add(getVal(row.getCell(c)));
				}
				dataList.add(datas.toArray());
			}
		}
		xworkbook.close();
    	return dataList;
    }
    
	/**
     * 导入
     * @param file csv文件
     * @return
     */
    public static List<Object[]> importCsv(MultipartFile file) throws Exception {
        List<Object[]> dataList=new ArrayList<Object[]>();
        BufferedReader br=null;
        try {
        	InputStream fileInputstream=file.getInputStream();
        	int p = (fileInputstream.read() << 8) + fileInputstream.read();
            String code = null;
            switch (p) {
                case 0xefbb: 
                    code = "UTF-8";
                    break;  
                case 0xfffe:  
                    code = "Unicode";
                    break;
                case 0xfeff:  
                    code = "UTF-16BE";
                    break;
                default:  
                    code = "GBK";
            }  
            br = new BufferedReader(new InputStreamReader(fileInputstream,code));
            String line = "";
            String csvSplitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
            while ((line = br.readLine()) != null) {
            	String[] arr=line.split(csvSplitBy,-1);
            	for(int i=0,num=arr.length;i<num;i++){
            		arr[i]=arr[i].trim();
            	}
                dataList.add(arr);
            }
            dataList.remove(0);//head 去掉
        }catch (Exception e) {
        	throw new Exception("CSVUtils.importCsv failure due to exception!", e);
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }
	
    /**
     * 导入
     * @param file csv文件
     * @return
     */
    public static List<Object[]> importCsv(File file) throws Exception {
        List<Object[]> dataList=new ArrayList<Object[]>();
        BufferedReader br=null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = "";
            String csvSplitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
            while ((line = br.readLine()) != null) {
            	String[] arr=line.split(csvSplitBy,-1);
            	for(int i=0,num=arr.length;i<num;i++){
            		arr[i]=arr[i].trim();
            		if (arr[i].startsWith("\"") && arr[i].endsWith("\""))
                    {
            			arr[i] = arr[i].substring(1, arr[i].length() - 1);
                    }
            	}
                dataList.add(arr);
            }
            if(!dataList.isEmpty()){
            	dataList.remove(0);//去掉head
            }
        }catch (Exception e) {
        	throw new Exception("CSVUtils.importCsv failure due to exception!", e);
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }
    
	/**
     * 得到Excel表中的值
     * 
     * @param hssfCell Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
	@SuppressWarnings("static-access")
	public static String getValue(HSSFCell hssfCell) {
		if(null!=hssfCell){
			if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				//返回布尔类型的值
				return String.valueOf(hssfCell.getBooleanCellValue());
			} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
				//返回数值类型的值
				return String.valueOf(hssfCell.getNumericCellValue());
			} else {
				//返回字符串类型的值
				return String.valueOf(hssfCell.getStringCellValue());
			}
		}else{
			return "";
		}
	}
	
	@SuppressWarnings({"deprecation" })
	public static Object getVal(Cell cell) {
		Object result =null;
		if(null!=cell){
			if (cell.getCellType()==Cell.CELL_TYPE_BOOLEAN) {
					result = cell.getBooleanCellValue();
			}else if (cell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
					if (DateUtil.isCellDateFormatted(cell)) {
						java.text.SimpleDateFormat TIME_FORMATTER = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						result = TIME_FORMATTER.format(cell.getDateCellValue());
					} else {
						String str = null;
						double doubleValue = cell.getNumericCellValue();
						// 是否为数值型
						if (doubleValue - (int) doubleValue < Double.MIN_VALUE) {
							// 是否为int型
							str = Integer.toString((int) doubleValue);
						} else {
							// 是否为double型
							str = Double.toString(cell.getNumericCellValue());
							DecimalFormat df = new DecimalFormat("#");
							str = df.format(cell.getNumericCellValue());
						}
						result = "" + str;
					}
			}else if (cell.getCellType()==Cell.CELL_TYPE_STRING) {
					if (cell.getStringCellValue() == null) {
						result = null;
					} else {
						result = cell.getStringCellValue().trim();
					}
			}else if (cell.getCellType()==Cell.CELL_TYPE_BLANK) {
					result = null;
			}else if (cell.getCellType()==Cell.CELL_TYPE_FORMULA) {
					try {
						result = String.valueOf(cell.getNumericCellValue());
					} catch (Exception e) {
						result = cell.getStringCellValue().trim();
					}
			}else{
					result = cell.getStringCellValue().trim();
			}
		}
		return result;
	}

}
