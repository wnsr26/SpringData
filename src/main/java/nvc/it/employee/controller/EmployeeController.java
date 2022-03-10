package nvc.it.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import nvc.it.employee.model.Department;
import nvc.it.employee.model.Employee;
import nvc.it.employee.model.Project;
import nvc.it.employee.repository.DepartmentRepository;
import nvc.it.employee.repository.EmployeeRepository;
import nvc.it.employee.repository.ProjectRepository;
import nvc.it.employee.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("")
    public ModelAndView employee () {
        List<Employee> employees = employeeService.findAll();
        return new ModelAndView("employee","employees",employees);
    }

    @GetMapping("/new")
    public String newEmployee (ModelMap modelMap) {
        Employee employee = new Employee();
        modelMap.addAttribute("employee", employee);
        return "newemployee";
    }

    @PostMapping("/add")
    public String saveEmployee(Employee employee, BindingResult result){
        // do save 
        if(result.hasErrors()){
            return "newemployee";
        }else{
            employeeService.save(employee);
            return "redirect:/employee";
        }
    }

    @GetMapping("/name/{name}")
    public ModelAndView getEmployeeByName(@PathVariable("name") String name){
        List<Employee> employees = employeeService.findByName(name);
        return new ModelAndView("employee", "employees",employees);
    }

    @PostMapping("/name")
    public ModelAndView searchEmployeeByName(String name, ModelMap modelMap){
        List<Employee> employees = employeeService.findByName(name);
        modelMap.addAttribute("name", name);
        return new ModelAndView("employee", "employees", employees);
    }

    @GetMapping("/salary/{salary}")
    public ModelAndView getEmployeeBySalary (@PathVariable("salary")int salary){
        List<Employee> employees = employeeService.findBySalary(salary);
        return new ModelAndView("employee", "employees", employees);
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable int id){
        Employee employees = employeeService.getById(id);
        employeeService.delete(employees);
        return new ModelAndView("redirect:/employee");
    }

    @GetMapping("/edit")
    public String editEmployee () {
        return "editemployee";
    }

    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable int id , Model modelMap){
        Employee employee = employeeService.getById(id);
        modelMap.addAttribute("employee", employee);
        return "editemployee";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute("employee") Employee employee, BindingResult result){
        if(result.hasErrors()){
            return "editemployee";
        }else{
            Employee prd = employeeService.getById(employee.getId());
            prd.setName(employee.getName());
            prd.setSalary(employee.getSalary());
            prd.setDepartment(employee.getDepartment());
            prd.setProject(employee.getProject());
            employeeService.save(prd);
            return "redirect:/employee";
        }
    }

    @ModelAttribute("departments")
    public List<Department> loadDepartments(){
        List<Department> departments = departmentRepository.findAll();
        return departments;
    }

    @ModelAttribute("projects")
    public List<Project> loadProjects(){
        List<Project> projects = projectRepository.findAll();
        return projects;
    }

}
