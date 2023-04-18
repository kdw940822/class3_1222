package com.itwillbs.spring_mvc_board_kdw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.spring_mvc_board_kdw.service.BoardService;
import com.itwillbs.spring_mvc_board_kdw.vo.BoardVO;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	// 글쓰기 폼
	// => 세션 아이디가 존재하지 않으면 "로그인 필수!", "MemberLoginForm.me" 저장 후 success 로 이동
	@GetMapping("/BoardWriteForm.bo")
	public String writeForm(HttpSession session, Model model) {
		if(session.getAttribute("sId") == null) {
			model.addAttribute("msg", "로그인 필수!");
			model.addAttribute("target", "MemberLoginForm.me");
			return "success";
		}
		
		return "board/board_write_form";
	}
	
	@GetMapping("/BoardList.bo")
	public String boardList(HttpSession session, Model model) {
		return "board/board_list";
	}
	
	
	
	
//	@PostMapping("/BoardWritePro.bo")
//	public String writePro(String board_name, String board_subject, String board_content, MultipartFile file) {
//		System.out.println(board_name+", "+board_subject+", "+board_content);
//		System.out.println("업로드 파일명 : " + file.getOriginalFilename());
//		return "redirect:/BoardList.bo";
//	}
	
//	@PostMapping("/BoardWritePro.bo")
//	public String writePro(@RequestParam Map<String, String> map, MultipartFile file) {
//		System.out.println(map.get("board_name")+", "+map.get("board_subject")+", "+map.get("board_content"));
//		System.out.println("업로드 파일명 : " + file.getOriginalFilename());
//		return "redirect:/BoardList.bo";
//	}
	
	@PostMapping("/BoardWritePro.bo")
	public String writePro(BoardVO board, HttpSession session, Model model) {
//		System.out.println(board);
//		System.out.println("업로드 파일명 : " + board.getFile().getOriginalFilename());
		
		String uploadDir = "/resources/upload";
//		String saveDir = request.getServletContext().getRealPath(uploadDir);
		String saveDir = session.getServletContext().getRealPath(uploadDir);
		
//		D:\workspace_spring3\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Spring_MVC_Board\resources\ upload
//		System.out.println("실제 업로드 경로 : " + saveDir);
		
		try {
			Date date = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			System.out.println(sdf.format(date));
			board.setBoard_file_path("/"+sdf.format(date));
			
			saveDir = saveDir + board.getBoard_file_path();
			
			Path path = Paths.get(saveDir);
			Files.createDirectories(path);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MultipartFile mFile = board.getFile();
//		MultipartFile[] mFile = board.getFile();
		
		String originalFileName = mFile.getOriginalFilename();
		System.out.println(originalFileName);
		
		String uuid = UUID.randomUUID().toString().substring(0,8);
//		System.out.println(uuid);
		
		
		board.setBoard_file(uuid + "_" + originalFileName);
		System.out.println("실제 업로드 될 파일명 : "+originalFileName);
		
		
		int insertCount = service.registBoard(board);
		
		

		
		if(insertCount > 0) {
			try {
				mFile.transferTo(new File(saveDir, board.getBoard_file()));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return "redirect:/BoardList.bo";
		}else {
			model.addAttribute("msg", "글쓰기 실패!");
			return "fail_back";
		}
		
		
	}
	
}













