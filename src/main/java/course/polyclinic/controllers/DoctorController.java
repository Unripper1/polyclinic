package course.polyclinic.controllers;

import course.polyclinic.DTO.ResultDTO;
import course.polyclinic.components.Result;
import course.polyclinic.enums.Status;
import course.polyclinic.repo.ResultRepo;
import course.polyclinic.services.AppointmentService;
import course.polyclinic.services.CustomerService;
import course.polyclinic.services.DoctorService;
import course.polyclinic.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctor")
//@PreAuthorize("hasAuthority('ROLE_DOCTOR')")
public class DoctorController {
    private final DoctorService doctorService;
    private final CustomerService customerService;
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final ResultRepo resultRepo;
    @GetMapping("/lk")
//    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public String lk(Model model){
        model.addAttribute("appointments",userService.loadUserByUsername(userService.getCurrentUsername()).getDoctor().getAppointments());
        model.addAttribute("doctor",userService.loadUserByUsername(userService.getCurrentUsername()).getDoctor());
        return "lk_doc";
    }
    @GetMapping("/app/{id}")
    public String app(@PathVariable("id") long id,Model model){
        model.addAttribute("appointments",appointmentService.find(id).getCustomer().getAppointments());
//        model.addAttribute("appointment", appointmentService.find(id));
        return "app";
    }
    @PostMapping("/app/{id}")
    public String appPost(@PathVariable("id") long id, ResultDTO resultDTO){
        Result result=new Result().setDiagnosis(resultDTO.getDiagnosis()).setTherapy(resultDTO.getTherapy()).setDescription(resultDTO.getDescription());
        resultRepo.save(result.setAppointment(appointmentService.find(id)));
        appointmentService.save(appointmentService.find(id).setStatus(Status.SUCCESSFUL));
        return "app";
    }
}
