package org.perscholas.Firstspringbootproject.controllers;

import lombok.extern.slf4j.Slf4j;
import org.perscholas.Firstspringbootproject.dao.CurrentMealsRepo;
import org.perscholas.Firstspringbootproject.dao.CustomerShippingRepo;
import org.perscholas.Firstspringbootproject.dao.MealRepo;
import org.perscholas.Firstspringbootproject.models.CurrentMeals;
import org.perscholas.Firstspringbootproject.models.CustomerShipping;
import org.perscholas.Firstspringbootproject.models.Meal;
import org.perscholas.Firstspringbootproject.models.User;
import org.perscholas.Firstspringbootproject.services.MealServices;
import org.perscholas.Firstspringbootproject.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.*;

@Controller
@Slf4j //logs
@SessionAttributes({"theuser", "thename","user"})
public class HomeController {

//    @Autowired
    private CurrentMealsRepo currentMealsRepo;

//    @Autowired
    private MealRepo mealRepo;

    private UserServices userServices;

    private CustomerShippingRepo customerShippingRepo;

    @Autowired
    public HomeController(CurrentMealsRepo currentMealsRepo, MealRepo mealRepo, UserServices userServices, CustomerShippingRepo customerShippingRepo) {
        this.currentMealsRepo = currentMealsRepo;
        this.mealRepo = mealRepo;
        this.userServices = userServices;
        this.customerShippingRepo = customerShippingRepo;
    }


    @GetMapping({ "/", "index"})
    public String showIndex(Model model) {
        log.warn("requested index");
        return "index";
    }

    @GetMapping("/listusers")
    public String listUsers(Model model, @Param("searchUsername") String username, @Param("searchPass") String password)
    {
        List<User> listUsers = userServices.findAll();
        model.addAttribute("listUsers", listUsers);
        List<User> listUsersByUserAndPass= userServices.getByUsernameAndPassword(username, password);
        Boolean isValid = userServices.isValidUser(listUsersByUserAndPass);
        model.addAttribute("isValid", isValid.toString());
        return "listusers";
    }

    @PostMapping("/home")
    public String postHome(HttpSession session, Model model, @Param("searchUsername") String username, @Param("searchPass") String password)
    {
        log.warn("post request! profile");
        List<User> listUsersByUserAndPass= userServices.getByUsernameAndPassword(username, password);
        Boolean isValid = userServices.isValidUser(listUsersByUserAndPass);
        if (isValid == false)
        {
            return "login";
        }
        User user = listUsersByUserAndPass.get(0);
//        List<Meal> userMealslist = user.getMeals();
        model.addAttribute("user", user);
//        model.addAttribute("userMealsList", userMealslist);
        List<Meal> currentms = new ArrayList<>();
        List<Integer> currentMealIds = currentMealsRepo.getAllIds();
        for(Integer mid: currentMealIds) {
            if (currentMealsRepo.getById(mid).getHighlighted()) {
                currentms.add(mealRepo.getById(mid));
            }
        }
        model.addAttribute("currentms", currentms);
        session.setAttribute("user", user);
        Integer mealnum = userServices.getByID(user.getId()).getTotalmealsdonated();
        if (mealnum == null)
        {
            user.setTotalmealsdonated(0);
            userServices.saveUser(user);
        }
        mealnum = userServices.getByID(user.getId()).getTotalmealsdonated();
        model.addAttribute("mealnum", String.valueOf(mealnum));
        return "home";
    }

    @GetMapping("/home")
    public String showHome(Model model, @ModelAttribute("user") User user){
        List<Meal> currentms = new ArrayList<>();
        List<Integer> currentMealIds = currentMealsRepo.getAllIds();
        for(Integer mid: currentMealIds) {
            if (currentMealsRepo.getById(mid).getHighlighted()) {
                currentms.add(mealRepo.getById(mid));
            }
        }
        model.addAttribute("currentms", currentms);
        Integer mealnum = userServices.getByID(user.getId()).getTotalmealsdonated();
        log.warn(String.valueOf(mealnum));
        model.addAttribute("mealnum", String.valueOf(mealnum));
        return "home";
    }

    @ModelAttribute("theuser")
    public User userInit() { return new User();}

    @ModelAttribute("user")
    public User userIni() { return new User();}

    @GetMapping("/addmeal")
    public String showAddMeal(Model model) {
        log.warn("requested addmeal");
        return "addmeal";
    }

    @PostMapping("/successaddedmeal")
    public String showMeals(@ModelAttribute("themeal") @Valid Meal meal, BindingResult bindingResult, Model model, HttpSession session)
    {
        log.warn("post request! meals");
        if( bindingResult.hasErrors()){
            log.warn(bindingResult.getAllErrors().toString());
            return "addmeal";
        }
        model.addAttribute("meal", meal);
        mealRepo.save(meal);
//        userServices.saveUser(user);
        return "successaddedmeal";
    }

    @ModelAttribute("themeal")
    public Meal mealInit() { return new Meal();}

    @ModelAttribute("thecs")
    public CustomerShipping csInit() { return new CustomerShipping();}


    @GetMapping("/profile")
    public String showProfile(Model model, @ModelAttribute("user") User user, @ModelAttribute("thecs") CustomerShipping cs){

        if (!customerShippingRepo.getAllIds().contains(user.getId()))
        {
            cs = new CustomerShipping(user.getId());
            customerShippingRepo.save(cs);
        }
        else
        {
            cs = customerShippingRepo.findByCustomerid(user.getId());
        }
        model.addAttribute("cs", cs);
        Integer mealnum = userServices.getByID(user.getId()).getTotalmealsdonated();
        model.addAttribute("mealnum", String.valueOf(mealnum));
        return "profile";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute("theuser") @Valid User theUser, BindingResult bindingResult, Model model)
    {
        log.warn("post request! from register to login");
        if( bindingResult.hasErrors()){
            log.warn(bindingResult.getAllErrors().toString());
            return "index";
        }
        model.addAttribute("user", theUser);
        userServices.saveUser(theUser);
        return "login";
    }

    @GetMapping("/login")
    public String showLogin(Model model, @Param("searchUsername") String username, @Param("searchPass") String password)
    {
        return "login";
    }

    @GetMapping("/allmeals")
    public String showAllMeals(Model model){
        List<Meal> listAllMeals = mealRepo.findAll();
        model.addAttribute("listAllMeals", listAllMeals);
        List<Meal> currentmeals = new ArrayList<>();
        List<Integer> currentMealIds = currentMealsRepo.getAllIds();
        for(Meal meal:listAllMeals) {
            if (currentMealIds.contains(meal.getMealcode()))
            {
                currentmeals.add(meal);
            }
        }
        model.addAttribute("currentmeals", currentmeals);
        return "allmeals";
    }

    @GetMapping("/choosemeals")
    public String showChooseMeals(Model model){
        List<Integer> currentMealIds = currentMealsRepo.getAllIds();
        List<Meal> meals = mealRepo.findAll();
        ArrayList<Meal> currentmeals = new ArrayList<>();
        for(Meal meal:meals) {
            if (currentMealIds.contains(meal.getMealcode()))
            {
                currentmeals.add(meal);
            }
        }
        model.addAttribute("currentmeals", currentmeals);
        return "choosemeals";
    }

    @PostMapping("/successaddedmeals")
    public String postSuccessAddedMeals(HttpSession session, Model model, @Param("searchid") Integer id, @Param("searchmealcode1") Integer mealcode1,
                                  @Param("searchmealcode2") Integer mealcode2, @Param("searchmealcode3") Integer mealcode3, @Param("searchmealcode4") Integer mealcode4){
        User u = (User) session.getAttribute("user");
        u.getMeals().clear();
        u.getMeals().add(mealRepo.getById((Integer) mealcode1));
        mealRepo.getById(mealcode1).getUsers().add(u);
        u.getMeals().add(mealRepo.getById((Integer) mealcode2));
        mealRepo.getById(mealcode2).getUsers().add(u);
        u.getMeals().add(mealRepo.getById((Integer) mealcode3));
        mealRepo.getById(mealcode3).getUsers().add(u);
        u.getMeals().add(mealRepo.getById((Integer) mealcode4));
        mealRepo.getById(mealcode4).getUsers().add(u);
        List<Meal> userMealsList = u.getMeals();
        model.addAttribute("userMealsList", userMealsList);
        userServices.saveUser(u);
        return "successaddedmeals";
    }

    @GetMapping("/deliveries")
    public String showDeliveries(Model model){
        return "deliveries";
    }

    @GetMapping("/vieworders")
    public String showViewOrders(Model model){
        List<Meal> mealList = mealRepo.findAll();
        model.addAttribute("mealList", mealList);
        return "vieworders";
    }

    @PostMapping("/updatedcontact")
    public String updatedContact(@ModelAttribute("user") User user, Model model, @Param("newfname") String fname,
    @Param("newlname") String lname, @Param("newemail") String email, @Param("newuname") String uname, @Param("newpass") String pass) {
        if(fname.length()>0) {
            user.setFirstname(fname);
        }
        if(lname.length()>0) {
            user.setLastname(lname);
        }
        if(email.length()>0) {
            user.setEmail(email);
        }
        if (uname.length()>0) {
            user.setUsername(uname);
        }
        if (pass.length()>0) {
            user.setPassword(pass);
        }
        userServices.saveUser(user);
        return "updatedcontact";
    }

    @PostMapping("/updatedshipping")
    public String updatedShipping(@ModelAttribute("user") User user, @ModelAttribute("cs") CustomerShipping cs, Model model, @Param("customershipadd1") String customershipadd1,  @Param("customershipadd2") String customershipadd2,
                                 @Param("customercity") String customercity, @Param("customerstate") String customerstate, @Param("customerzip") String customerzip) {
        cs =  customerShippingRepo.findByCustomerid(user.getId());
        if(customershipadd1.length()>0) {
            cs.setCustomershipadd1(customershipadd1);
        }
        if(customershipadd2.length()>0) {
            cs.setCustomershipadd2(customershipadd2);
        }
        if(customercity.length()>0) {
            cs.setCustomercity(customercity);
        }
        if(customerstate.length()>0) {
            cs.setCustomerstate(customerstate);
        }
        if(customerzip.length()>0) {
            cs.setCustomerzip(customerzip);
        }
        customerShippingRepo.save(cs);
        return "updatedshipping";
    }

    @PostMapping("/updatemealstochoose")
    public String showUpdateMeals(Model model, @Param("searchmealcode1") Integer mealcode1, @Param("searchmealcode2") Integer mealcode2, @Param("searchmealcode3") Integer mealcode3, @Param("searchmealcode4") Integer mealcode4,
                                  @Param("searchmealcode5") Integer mealcode5, @Param("searchmealcode6") Integer mealcode6, @Param("searchmealcode7") Integer mealcode7, @Param("searchmealcode8") Integer mealcode8) {
        Integer[] mealcodes = {mealcode1, mealcode2,mealcode3, mealcode4, mealcode5, mealcode6, mealcode7, mealcode8};
        for (Integer mc : mealcodes)
        {
            log.warn(String.valueOf(mc));
        }
        List<User> users = userServices.findAll();
        for (User user : users) {
            if (user.getMeals().size() > 0)
            {
                user.setTotalmealsdonated(user.getTotalmealsdonated() + user.getMeals().size());
                user.getMeals().clear();
            }
        }
        List<CurrentMeals> cms =currentMealsRepo.findAll();
        for (CurrentMeals m : cms) {
            currentMealsRepo.delete(m);
            mealRepo.getById(m.getMealcode()).getUsers().clear();
        }
        for (int i = 0; i<8; i++) {
            CurrentMeals cm = new CurrentMeals(mealcodes[i]);
            if (i < 4)
            {
                cm.setHighlighted(true);
            }
            else
            {
                cm.setHighlighted(false);
            }
            currentMealsRepo.save(cm);
            log.warn(String.valueOf(cm.getMealcode()));

        }
        List<Meal> currentmeal = new ArrayList<>();
        List<Integer> currentMealIds = currentMealsRepo.getAllIds();
        for(Integer mid: currentMealIds) {
            currentmeal.add(mealRepo.getById(mid));
        }
        model.addAttribute("currentmeal", currentmeal);
        return "updatemealstochoose";
    }

}
