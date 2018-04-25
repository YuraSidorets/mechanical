package org.patterns.mechanical;

import org.patterns.mechanical.dao.RepairRequestDao;
import org.patterns.mechanical.model.Credentials;
import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.RequestState;
import org.patterns.mechanical.model.User;
import org.patterns.mechanical.service.LoginService;
import org.patterns.mechanical.service.RepairRequestProcessor;
import org.patterns.mechanical.service.pipeline.Middleware;
import org.patterns.mechanical.service.pipeline.RepairRequestPipeline;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private LoginService loginService;
    private RepairRequestDao repairRequestDao;
    private RepairRequestPipeline requestPipeline;

    public MainController(LoginService loginService, RepairRequestDao repairRequestDao,
                          RepairRequestPipeline requestPipeline) {
        this.loginService = loginService;
        this.repairRequestDao = repairRequestDao;
        this.requestPipeline = requestPipeline;
    }

    private String homePage(User user) {
        switch (user.getRole()) {
            case "CLIENT":
                return "client";
            case "CALL_CENTER":
            case "SUPPORT":
                return "worker";
            default:
                throw new IllegalStateException("Invalid user role");
        }
    }

    @GetMapping("/")
    public String home(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("principal");
        if (user == null) {
            return "redirect:/login";
        }
        switch (user.getRole()) {
            case "CLIENT":
                model.addAttribute("requests", repairRequestDao.findByUserId(user.getId()));
                model.addAttribute("request", new RepairRequest());
                break;
            case "CALL_CENTER": {
                List<RepairRequest> requests =
                        repairRequestDao.findByEmployeeId(user.getId())
                                .stream()
                                .filter(r -> r.getStatus().equals(RequestState.CALL_CENTER))
                                .filter(r -> !r.getResolved())
                                .collect(Collectors.toList());
                model.addAttribute("requests", requests);
                break;
            }
            case "SUPPORT": {
                List<RepairRequest> requests =
                        repairRequestDao.findByEmployeeId(user.getId())
                                .stream()
                                .filter(r -> r.getStatus().equals(RequestState.SUPPORT))
                                .filter(r -> !r.getResolved())
                                .collect(Collectors.toList());
                model.addAttribute("requests", requests);
                break;
            }
        }
        return homePage(user);
    }

    @PostMapping("/request")
    public String newRequest(HttpSession httpSession, @ModelAttribute RepairRequest repairRequest, BindingResult result) {
        User user = (User) httpSession.getAttribute("principal");
        if (user == null) {
            return "redirect:/";
        }
        repairRequest.setId(null);
        repairRequest.setUserId(user.getId());
        repairRequest.setStatus(RequestState.PROCESSING);
        LocalDateTime now = LocalDateTime.now();
        repairRequest.setCreatedAt(now);
        repairRequest.setUpdatedAt(now);
        repairRequestDao.save(repairRequest);

        Middleware start = requestPipeline.construct(repairRequest);
        start.check(user, repairRequest);
        repairRequestDao.save(repairRequest);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("credentials", new Credentials());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute Credentials credentials, BindingResult result, HttpSession httpSession) {
        try {
            User principal = loginService.login(credentials);
            httpSession.setAttribute("principal", principal);
            if (principal.getRole().equals("CALL_CENTER") ||
                    principal.getRole().equals("SUPPORT")) {
                RepairRequestProcessor.getInstance().addEmployee(principal);
            }
            return "redirect:/";
        } catch (IllegalArgumentException ex) {
            result.addError(new ObjectError("credentials", "Invalid credentials"));
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/resolve")
    public String resolve(@RequestParam Long id, HttpSession session) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/login";
        }
        RepairRequest repairRequest = repairRequestDao.findOne(id)
                .orElseThrow(IllegalArgumentException::new);
        if (repairRequest.getMechanicId() != principal.getId()) {
            throw new IllegalArgumentException("Not assigned to given employee.");
        }
        String role =
                principal.getRole().equals("CALL_CENTER") ? "Call Center": "Support";
        repairRequest.setResponse("Resolved by " + role);
        repairRequest.setUpdatedAt(LocalDateTime.now());
        repairRequest.setResolved(true);
        repairRequestDao.save(repairRequest);
        return "redirect:/";
    }

    @PostMapping("/next")
    public String next(@RequestParam Long id, HttpSession session) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/login";
        }
        RepairRequest repairRequest = repairRequestDao.findOne(id)
                .orElseThrow(IllegalArgumentException::new);
        if (repairRequest.getUserId() != principal.getId()) {
            throw new IllegalArgumentException("Invalid user id");
        }
        repairRequest.setResolved(false);
        Middleware start = requestPipeline.construct(repairRequest);
        start.check(principal, repairRequest);
        repairRequestDao.save(repairRequest);
        return "redirect:/";
    }
}
