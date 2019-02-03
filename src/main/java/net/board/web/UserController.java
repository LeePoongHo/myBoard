package net.board.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.board.domain.User;
import net.board.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	private List<User> users = new ArrayList<User>();
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("")
	public String create(User user) {
		userRepository.save(user);
		return "redirect:/users";
	}
	

	
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}
	
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		
		User sessionedUser = (User) session.getAttribute("sessionedUser");
		
		if (session.getAttribute("sessionedUser") == null ) {
			return "redirect:/users/loginForm";
		}
		
		if (id != sessionedUser.getId()) {
			throw new IllegalStateException("You can't update another user's information.");
		}
		
		User user = userRepository.findById(id).get();
		model.addAttribute("user", user);
		return "/user/updateForm";
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
		
		User sessionedUser = (User) session.getAttribute("sessionedUser");
		
		if (session.getAttribute("sessionedUser") == null ) {
			return "redirect:/users/loginForm";
		}
		
		if (id != sessionedUser.getId()) {
			throw new IllegalStateException("You can't update another user's information.");
		}
		
		
		User user = userRepository.findById(id).get();
		user.update(updatedUser);
		userRepository.save(user); // 기본키인 id가 테이블에 이미 존재하면 수정하고, 없으면 추가한다.
		return "redirect:/users";
	}
	
	@GetMapping("/form")
	public String form() {
		return "/user/form";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		User user = userRepository.findByUserId(userId);
		
		if (user == null || !user.getPassword().equals(password)) {
			System.out.println("로그인 실패!!");
			return "redirect:/users/loginForm";
		} else {
			session.setAttribute("sessionedUser", user);
			System.out.println("로그인 성공!!");
			return "redirect:/";
		}

	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("sessionedUser");
		return "redirect:/";
	}
	
}
