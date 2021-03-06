package com.dodge.board.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dodge.board.domain.Board;
import com.dodge.board.domain.Comment;
import com.dodge.board.domain.Recommendation;
import com.dodge.board.domain.Search;
import com.dodge.board.service.BoardService;


@Controller
public class BoardController implements ApplicationContextAware{
	
	@Resource(name="BasicBoardService")
	private BoardService boardService;
	
	private WebApplicationContext context = null;
	
	private Board board;
	
	//알림
	@ResponseBody
	@RequestMapping("/boards/alarm")
	public Map<Object, Object> alarm(@RequestBody Map<Object, Object> map) throws InterruptedException{
		System.out.println("게시글 알람");
		
		map.put("board", board);

		return map;
	}
	
	
	
	//즐겨찾기 추천 취소
	@ResponseBody
	@RequestMapping("/board/deleteRecommendation")
	public Map<Object, Object> deleteRecommendation(@RequestBody Map<Object, Object> map){
		System.out.println("즐겨찾기 해제");
		map.put("cnt", boardService.deleteRecommendation(map));
		return map;
	}
	
	//내가 쓴 댓글 보기
	@RequestMapping(value= {"/boards/like-list"})
	public String getLikeList(Model model, @RequestParam(value="pageNum" , defaultValue="1")int pageNum, @RequestParam(value="size" , defaultValue="10")int size
			, Search search) {
		System.out.println("좋아요 목록 보기");
		
		if(search.getSearchCondition() == null) {
			search.setSearchCondition("TITLE");
		}
		if(search.getSearchKeyword() == null) {
			search.setSearchKeyword("");
		}
		Page<Board> boardList = boardService.getLikeList(pageNum, size, search);
		model.addAttribute("boardList", boardList);
		//검색조건과 검색어를 저장하여 페이징 처리하기위해
		model.addAttribute("searchCondition", search.getSearchCondition());
		model.addAttribute("searchKeyword", search.getSearchKeyword());
		
		return "/board/getLikeList";
	}
		
	//내가 쓴 댓글 보기
	@RequestMapping(value= {"/boards/my-cm-list"})
	public String getMyCommentList(Model model, @RequestParam(value="pageNum" , defaultValue="1")int pageNum, @RequestParam(value="size" , defaultValue="10")int size
			, Search search) {
		System.out.println("내가 쓴 댓글 보기");
		
		if(search.getSearchCondition() == null) {
			search.setSearchCondition("CONTENT");
		}
		if(search.getSearchKeyword() == null) {
			search.setSearchKeyword("");
		}
		Page<Comment> myCommentList = boardService.getMyCommentList(pageNum, size, search);
		model.addAttribute("myCommentList", myCommentList);
		//검색조건과 검색어를 저장하여 페이징 처리하기위해
		model.addAttribute("searchCondition", search.getSearchCondition());
		model.addAttribute("searchKeyword", search.getSearchKeyword());
		
		return "/board/getMyCommentList";
	}
	
	//내가 쓴 글 보기
	
	@RequestMapping(value= {"/boards/my-list"})
	public String getMyBoardList(Model model, @RequestParam(value="pageNum" , defaultValue="1")int pageNum, @RequestParam(value="size" , defaultValue="10")int size
			, Search search) {
		System.out.println("내가 쓴 글 보기");
		
		if(search.getSearchCondition() == null) {
			search.setSearchCondition("TITLE");
		}
		if(search.getSearchKeyword() == null) {
			search.setSearchKeyword("");
		}
		Page<Board> boardList = boardService.getMyBoardList(pageNum, size, search);
		model.addAttribute("boardList", boardList);
		//검색조건과 검색어를 저장하여 페이징 처리하기위해
		model.addAttribute("searchCondition", search.getSearchCondition());
		model.addAttribute("searchKeyword", search.getSearchKeyword());
		
		return "/board/getMyBoardList";
	}
	
	
	
	
	
	/////////////////////////////////////댓글 관련 컨트롤러
	//대댓글 삽입
	@ResponseBody
	@RequestMapping("/boards/insertReplyComment")
	public Map<Object, Object> insertReplyComment(@RequestBody Map<Object, Object> map, Comment comment){
		System.out.println("대댓글 삽입");
		map.put("cnt", boardService.insertReplyComment(map, comment));
		return map;
	}
	
	//댓글 수정
	@ResponseBody
	@RequestMapping("/boards/updateComment")
	public Map<Object, Object> updateComment(@RequestBody Map<Object, Object> map){
		System.out.println("댓글 수정");
		
		map.put("cnt", boardService.updateComment(map));
		return map;
	}
	
	//댓글 삭제
	@ResponseBody
	@RequestMapping("/boards/deleteMyComment")
	public Map<Object, Object> deleteMyComment(@RequestBody Map<Object, Object> map){
		System.out.println("댓글 삭제");
		map.put("cnt", boardService.deleteMyComment(map));
		return map;
	}
	
	//댓글 삭제
	@ResponseBody
	@RequestMapping("/boards/deleteComment")
	public Map<Object, Object> deleteComment(@RequestBody Map<Object, Object> map){
		System.out.println("댓글 삭제");
		map.put("cnt", boardService.deleteComment(map));
		return map;
	}
	
	//댓글 등록
	@ResponseBody
	@RequestMapping("/boards/insertComment")
	public Map<Object, Object> insertComment(@RequestBody Map<Object, Object> map, Comment comment) {
		System.out.println("댓글 등록");
		map.put("cnt", boardService.insertComment(map, comment));
		
		return map;
	}
	
	//댓글 리스트
	@ResponseBody
	@RequestMapping("/boards/getCommentList")
	public List<Comment> getCommentList(@RequestBody Map<Object, Object> map){
		System.out.println("댓글 리스트");
		
		return boardService.getCommentList(map);
	}
	
	
	@RequestMapping(value = {"/boards", "/system/boards", "boards"})
	public String getBoardList(Model model, @RequestParam(value="pageNum" , defaultValue="1")int pageNum, @RequestParam(value="size" , defaultValue="10")int size
			, Search search, @RequestParam(value="likeBoard", defaultValue="0")int likeBoard,
			@RequestParam(value="sort", defaultValue="DESC")String order) {
		System.out.println("게시판 리스트");
		
		if(search.getSearchCondition() == null) {
			search.setSearchCondition("TITLE");
		}
		if(search.getSearchKeyword() == null) {
			search.setSearchKeyword("");
		}
		Page<Board> boardList = boardService.getBoardList(pageNum, size, search, likeBoard, order);
		model.addAttribute("boardList", boardList);
		//검색조건과 검색어를 저장하여 페이징 처리하기위해
		model.addAttribute("searchCondition", search.getSearchCondition());
		model.addAttribute("searchKeyword", search.getSearchKeyword());
		model.addAttribute("sort", order);
		model.addAttribute("likeBoard", likeBoard);
		
		return "board/getBoardList";
	}
	
	//글쓰기 버튼 클릭
	@GetMapping("/boards/create")
	public String insertBoard(Model model, Board board) {
		model.addAttribute("board", board); //답글 구분을 위해 가져감
		return "board/insertBoard";
	}
	
	@PostMapping(value= {"/boards"})
	public String insertBoard(@RequestParam(value="uploadFile", required = false) MultipartFile mf, Board board) throws IllegalStateException, IOException {
		System.out.println("게시글 등록");

		boardService.insertBoard(mf, board);
		this.board = board;
		
		return "redirect:boards";
	}
	
	@RequestMapping("/boards/{seq}")
	public String getBoard(Model model, @PathVariable("seq") Long seq, Search search, @RequestParam(value="pageNum" , defaultValue="1")int pageNum, @RequestParam(value="size" , defaultValue="10")int size
			, @RequestParam(value="likeBoard", defaultValue="0")int likeBoard) {
		System.out.println("글 읽기");
		
		model.addAttribute("board", boardService.getBoard(seq));
		
		
		if(search.getSearchCondition() == null) {
			search.setSearchCondition("TITLE");
		}
		if(search.getSearchKeyword() == null) {
			search.setSearchKeyword("");
		}
		Page<Board> boardList = boardService.getBoardList(pageNum, size, search, likeBoard);
		model.addAttribute("boardList", boardList);
		//검색조건과 검색어를 저장하여 페이징 처리하기위해
		model.addAttribute("searchCondition", search.getSearchCondition());
		model.addAttribute("searchKeyword", search.getSearchKeyword());
		
		
		return "board/getBoard";
	}
	
	@RequestMapping("/board/deleteBoard")
	@ResponseBody
	public Map<Object, Object> deleteBoard(@RequestBody Map<String, String> var) {
		System.out.println("게시글 삭제");
		Map<Object, Object> map = new HashMap<Object, Object>();

		map.put("cnt", boardService.deleteBoard(var));
		
		return map;
	}
	
	@RequestMapping("/boards/updateRecommendation")
	@ResponseBody
	public Map<Object, Object> updateRecommendation(@RequestBody Map<Object, Object> var, Recommendation re) {
		System.out.println("게시글 추천");
		Map<Object, Object> map = new HashMap<Object, Object>();

		map.put("map", boardService.updateRecommendation(var, re));
		
		return map;
		
	}
	
	@RequestMapping("/board/updateCheck")
	@ResponseBody
	public Map<Object, Object> updateCheck(@RequestBody Map<Object, Object> map) {
		System.out.println("게시글 수정 체크");
		map.put("cnt", boardService.updateCheck(map));
		map.put("seq", map.get("seq"));
		map.put("originalFileName", map.get("originalFileName"));
		map.put("fileName", map.get("fileName"));
		map.put("fileSize", map.get("fileSize"));
		
		return map;
	}
	
	@PostMapping("/boards/update")
	public String updateBoard(Model model, Board board) {
		System.out.println("게시글 수정 데이터 넘기기");

		model.addAttribute("board", board);

		return "board/updateBoard";
	}
	
	//파일 다운로드
	@RequestMapping("/boards/download.do")
	public ModelAndView download(HttpServletRequest request, ModelAndView mv){
		String SAVE_PATH = "/home/ec2-user/apps/Dodge/Dodge2/src/main/resources/static/file/";
		String fullPath = SAVE_PATH+request.getParameter("originalFileName");
	
		File file = new File(fullPath);
		return new ModelAndView("download", "downloadFile", file);
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		
		this.context = (WebApplicationContext)arg0;
	}
	
	// 다중파일업로드 (사진)
    @RequestMapping(value = "/file_uploader_html5.do",
                  method = RequestMethod.POST)
    @ResponseBody
    public String multiplePhotoUpload(HttpServletRequest request) {
        // 파일정보
        StringBuffer sb = new StringBuffer();
        try {
            // 파일명을 받는다 - 일반 원본파일명
            String oldName = request.getHeader("file-name");
       
            // 파일 기본경로 _ 상세경로
            String filePath = "/home/ec2-user/apps/Dodge/Dodge2/src/main/resources/static/editor/photoUpload/";
            String saveName = sb.append(new SimpleDateFormat("yyyyMMddHHmmss")
                          .format(System.currentTimeMillis()))
                          .append(UUID.randomUUID().toString())
                          .append(oldName.substring(oldName.lastIndexOf("."))).toString();
            InputStream is = request.getInputStream();
            OutputStream os = new FileOutputStream(filePath + saveName);

            int numRead;
            byte b[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
 
            while ((numRead = is.read(b, 0, b.length)) != -1) {
                os.write(b, 0, numRead);
            }
            os.flush();
            os.close();
            // 정보 출력
            sb = new StringBuffer();
            sb.append("&bNewLine=true")
              .append("&sFileName=").append(oldName)
              .append("&sFileURL=").append("http://54.180.7.238:8080/editor/photoUpload/")
                                                  
        .append(saveName);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
