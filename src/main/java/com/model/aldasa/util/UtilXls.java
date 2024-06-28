package com.model.aldasa.util;

import java.awt.Color;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UtilXls {
	public static CellStyle styleCell(XSSFWorkbook libro, char tipo) {
		CellStyle cellStyleDefault = libro.createCellStyle();
		XSSFFont fontRow = libro.createFont();
		fontRow.setFontHeightInPoints((short) 11);
		fontRow.setFontName("Calibri");
		if (tipo == 'B') {// CANTIDAD NORMALES {
			cellStyleDefault.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyleDefault.setBorderTop(CellStyle.BORDER_THIN);
			cellStyleDefault.setBorderRight(CellStyle.BORDER_THIN);
			cellStyleDefault.setBorderLeft(CellStyle.BORDER_THIN);
		}

		if (tipo == 'A') {
			fontRow.setFontHeightInPoints((short) 13);
			cellStyleDefault.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyleDefault.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			cellStyleDefault.setWrapText(true);
			
			cellStyleDefault.setBorderBottom(CellStyle.BORDER_THIN);
			cellStyleDefault.setBorderTop(CellStyle.BORDER_THIN);
			cellStyleDefault.setBorderRight(CellStyle.BORDER_THIN);
			cellStyleDefault.setBorderLeft(CellStyle.BORDER_THIN);
		}

	

		
		cellStyleDefault.setFont(fontRow);

		return cellStyleDefault;
	}
}
