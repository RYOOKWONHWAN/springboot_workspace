package com.example.shop.common.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class fileUpload {

	public static UUID saveCopyFile(MultipartFile file, String filePath) {
		String fileName = file.getOriginalFilename();
		
		//16진수 난수값 발생
		//중복파일명을 처리하기 위한 난수 발생
		UUID random = UUID.randomUUID();
		
		
		
//		File ff = new File(urlPath(filePath), random + "_" + fileName);
		File ff = new File(filePath, random + "_" + fileName);
		
		try {
			FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(ff));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return random;
	}// end saveCopyFile()
	
	//첨부파일 저장할 위치
	//첨부파일이 저장된 서버의 위치
	public static String urlPath(HttpServletRequest request) {
		
		String root = request.getSession().getServletContext().getRealPath("/");
		System.out.println("root" + root);
		
		//temp 폴더 없으면 생성해줘야함
		String saveDirectory = root + "temp" + File.separator;
		
		File fe = new File(saveDirectory);
		if(!fe.exists())
			fe.mkdir();
		
		return saveDirectory;
	}
	
	
}//end class
