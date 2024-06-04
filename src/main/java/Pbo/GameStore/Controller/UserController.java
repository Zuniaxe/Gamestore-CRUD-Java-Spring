package Pbo.GameStore.Controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import Pbo.GameStore.Dto.UserDto;
import Pbo.GameStore.Models.Game;
import Pbo.GameStore.Models.Purchase;
import Pbo.GameStore.services.GameRepository;
import Pbo.GameStore.services.PurchaseRepository;
import Pbo.GameStore.services.UserService;

@Controller
public class UserController {

    @Autowired
    private GameRepository repo;
        
    @Autowired
    private PurchaseRepository purchaseRepo;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String rootRedirect() {
        return "user/user-prelogin";
    }


    
    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto, Model model){
        return "/user/register";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model){
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfully");
        return "user/register";
    }

    @GetMapping("/login")
    public String login(){
        return "/user/login";
    }

    @GetMapping("user-page")
    public String userPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        

        List<Game> games = repo.findAll();
        model.addAttribute("games", games);

                // Mendapatkan semua genre unik dari game
        List<String> genres = repo.findAll().stream()
                .map(Game::getGenre)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("genres", genres);
        return "user/user-page";
}

    @GetMapping("admin-page")
    public String adminPage(Model model,  Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "game";
    }

    @GetMapping("/library-page")
    public String libraryPage(Model model, Principal principal){
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
    
        // Find purchases made by the user
        List<Purchase> purchases = purchaseRepo.findByName(principal.getName());
        // Get the games from the purchases
        List<Game> purchasedGames = purchases.stream()
                                            .map(Purchase::getGame)
                                            .collect(Collectors.toList());
        model.addAttribute("purchasedGames", purchasedGames);
        return "user/library";
    }

    @GetMapping("/genre/{genre}")
    public String gamesByGenre(@PathVariable String genre, Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);

        List<Game> games = repo.findByGenre(genre);
        model.addAttribute("games", games);
        model.addAttribute("selectedGenre", genre);
        return "user/genre-page";
    }

    @GetMapping("/genre")
    public String genre(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        List<Game> games = repo.findAll();
        model.addAttribute("games", games);
        // Mendapatkan semua genre unik dari game untuk navbar genre
        List<String> genres = repo.findAll().stream()
                .map(Game::getGenre)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("genres", genres);
        

        return "user/genre-page";
    }


}

