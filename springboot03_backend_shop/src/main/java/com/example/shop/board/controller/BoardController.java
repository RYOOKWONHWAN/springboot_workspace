package com.example.shop.board.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shop.board.dto.BoardDTO;
import com.example.shop.board.dto.PageDTO;
import com.example.shop.board.service.BoardService;
import com.example.shop.common.file.fileUpload;

import lombok.val;
//@CrossOrigin(origins= {"http://localhost:3000");
@CrossOrigin("*")
@RestController
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private PageDTO pdto;

	
	@Value("${spring.servlet.multipart.location}")
	private String filePath;
	
	private int currentPage;

	public BoardController() {
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/board/list/{currentPage}")
	public Map<String, Object> listExecute(@PathVariable("currentPage") int currentPage, PageDTO pv) {
		Map<String, Object> map = new HashMap<>();
		int totalRecord = boardService.countProcess();
		if (totalRecord >= 1) {
			if (pv.getCurrentPage() == 0)
				pv.setCurrentPage(1);
			else
				this.currentPage = pv.getCurrentPage();

			this.pdto = new PageDTO(this.currentPage, totalRecord);
			map.put("alist", boardService.listProcess(this.pdto));
			map.put("pv", this.pdto);
		}

		return map;
	}

	
	//@requestBody:json => 자바객체
	//@ResponseBody:자바객체 => json
	//@PathVariable: /board/list/:num		=>/board/list/{num}
	//@RequestParam: /board/list?num=value  =>/board/list?num=1 =>/board/list
	// multipart/form-data : @requestbody 선언없이 그냥 받음 boardDTO
	// 제목글쓸떄는 currentPage 값이 null로 넘어오기때문에, PageDTO로 값을 받는다.
	@PostMapping(value = "/board/write")
	public String writeProExcute(BoardDTO dto, PageDTO pv, HttpServletRequest req, HttpSession session,
			RedirectAttributes ratt) {
		MultipartFile file = dto.getFilename();
		// System.out.println(file.getOriginalFilename()); //첨부파일 이름

		// matis 자동매핑?값초기화? //spring 는 Null값
		// System.out.println(dto.getMembersDTO().getMemberName());

		// 파일첨부가 있으면..
		if (!file.isEmpty()) {
			UUID random = fileUpload.saveCopyFile(file, filePath);
			dto.setUpload(random + "_" + file.getOriginalFilename());
		}

		dto.setIp(req.getRemoteAddr());

//		AuthInfo authInfo = (AuthInfo)session.getAttribute("authInfo");
//		dto.setMemberEmail(authInfo.getMemberEmail());
//<input type= "hidden" name ="memberEmail" value ="${sessionScope.authInfo.memberEmail}"/>
// write.jsp 에서 이걸로 대체

		boardService.insertProcess(dto);

		if (dto.getRef() != 0) {
//			ratt.addAttribute("currentPage", pv.getCurrentPage());
			return String.valueOf(pv.getCurrentPage());
		}else {
			return String.valueOf(1);
		}

//		return "redirect:/board/list.do";

	}
	@GetMapping("/board/view/{num}")
	public BoardDTO viewExecute(@PathVariable("num")int num) {
		return boardService.contentProcess(num);
	}
	
	@PutMapping("/board/update")
	public void updateExecute(BoardDTO dto, HttpServletRequest request) throws IllegalStateException, IOException{
		MultipartFile file= dto.getFilename();
		if(!file.isEmpty()) {
			UUID random= fileUpload.saveCopyFile(file, filePath);
			dto.setUpload(random+"_"+file.getOriginalFilename());
			file.transferTo(new File(random+"_"+file.getOriginalFilename()));
			
		}
		boardService.updateProcess(dto, filePath);
		
		
	}
	@DeleteMapping("/board/delete/{num}")
	public void deleteExecute(@PathVariable("num") int num, HttpServletRequest request) {
		
		boardService.deleteProcess(num, filePath);
		
	}
	
	@GetMapping("/board/contentdownload/{filename}")
	public ResponseEntity<Resource> downloadExecute(@PathVariable("filename")String filename) throws IOException {
		
		String fileName = filename.substring(filename.indexOf("_")+1);
		String str = URLEncoder.encode(fileName,"UTF-8");
		str= str.replaceAll("\\+","%20");
		Path path= Paths.get(filePath+"\\"+filename);
		Resource resource=new InputStreamResource(Files.newInputStream(path));
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+str+";")
				.body(resource);
		
		
		
		
		
		
		
		
		
	}
	

}
