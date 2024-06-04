package Pbo.GameStore.Controller;



import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Pbo.GameStore.Models.Game;
import Pbo.GameStore.Models.Purchase;
import Pbo.GameStore.services.GameRepository;
import Pbo.GameStore.services.PurchaseRepository;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private GameRepository repo;

    @Autowired
    private PurchaseRepository purchaseRepo;
    
    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/{id}")
    public String getPurchasePage(@PathVariable Integer id, Model model,  Principal principal) {
        Game game = repo.findById(id).orElseThrow();
        model.addAttribute("game", game);
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "purchase/purchase-page";
    }

    @GetMapping("/payment/{id}")
    public String showPaymentPage(@PathVariable Integer id, Model model, Principal principal) {
        Game game = repo.findById(id).orElseThrow();
        model.addAttribute("game", game);
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "purchase/payment-page";
    }

    @PostMapping("/payment/process")
    public String processPayment(
        @RequestParam("name") String name,
        @RequestParam("gameid") Integer gameId,
        @RequestParam("paymentMethod") String paymentMethod,
        @RequestParam("phoneNumber") String phoneNumber) {

        Game game = repo.findById(gameId).orElseThrow();
        
        Purchase purchase = new Purchase();
        purchase.setName(name);
        purchase.setGame(game);
        purchase.setPaymentMethod(paymentMethod);
        purchase.setPhoneNumber(phoneNumber);

        purchaseRepo.save(purchase);

        return "redirect:/user-page"; // redirect to a success page
    }
}

