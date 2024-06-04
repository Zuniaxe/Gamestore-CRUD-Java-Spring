package Pbo.GameStore.Controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import Pbo.GameStore.Models.Game;
import Pbo.GameStore.Models.GameCreate;
import Pbo.GameStore.Models.Purchase;
import Pbo.GameStore.services.GameRepository;
import Pbo.GameStore.services.PurchaseRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameRepository repo;

    @Autowired
    private PurchaseRepository purchaseRepo;

    @GetMapping({ "", "/" })
    public String showProductList(Model model) {
        List<Game> games = repo.findAll();
        model.addAttribute("games", games);
        return "game/index";
    }

    @GetMapping("/createGame")
    public String showCreateForm(Model model) {
        GameCreate gameCreate = new GameCreate();
        model.addAttribute("gameCreate", gameCreate);
        return "game/createGame";
    }

    @PostMapping("/createGame")
    public String createGame(
            @Valid @ModelAttribute GameCreate gameCreate,
            BindingResult result, Model model) {
        if (gameCreate.getImage().isEmpty()) {
            result.addError(new FieldError("gameCreate", "imageFile", "Please upload an image"));
        }
        if (result.hasErrors()) {
            model.addAttribute("gameCreate", gameCreate);
            return "game/createGame";
        }

        MultipartFile image = gameCreate.getImage();
        String storageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }

        // Here you should save the gameCreate entity to the database
        Game game = new Game();
        game.setName(gameCreate.getName());
        game.setPrice(gameCreate.getPrice());
        game.setStock(gameCreate.getStock());
        game.setGenre(gameCreate.getGenre());
        game.setDeveloper(gameCreate.getDeveloper());
        game.setPlatform(gameCreate.getPlatform());
        game.setPublisher(gameCreate.getPublisher());
        game.setRating(gameCreate.getRating());
        game.setTrailer(gameCreate.getTrailer());
        game.setRelease_date(gameCreate.getRelease_date());
        game.setDescription(gameCreate.getDescription());
        game.setImage(storageFileName); 

        repo.save(game);

        return "redirect:/game";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Game game = repo.findById(id).get();
            model.addAttribute("game", game);

            GameCreate gameCreate = new GameCreate();
            gameCreate.setName(game.getName());
            gameCreate.setPrice(game.getPrice());
            gameCreate.setStock(game.getStock());
            gameCreate.setGenre(game.getGenre());
            gameCreate.setDeveloper(game.getDeveloper());
            gameCreate.setPlatform(game.getPlatform());
            gameCreate.setPublisher(game.getPublisher());
            gameCreate.setRating(game.getRating());
            gameCreate.setTrailer(game.getTrailer());
            gameCreate.setRelease_date(game.getRelease_date());
            gameCreate.setDescription(game.getDescription());

            model.addAttribute("gameCreate", gameCreate);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "redirect:/game";
        }
        return "game/editGame";
    }

    @PostMapping("/edit")
    public String updateGame(
        Model model,
        @RequestParam int id,
        @Valid @ModelAttribute GameCreate gameCreate,
        BindingResult result
    ){
        try{
            Game game = repo.findById(id).get();
            model.addAttribute("game", game);

            if (result.hasErrors()) {
                return "game/editGame";
            }
            if (!gameCreate.getImage().isEmpty()){
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + game.getImage());
            

            try {
                Files.delete(oldImagePath);
            }
            catch (Exception e) {
                System.out.println("Error deleting old image: " + e.getMessage());
            }

            MultipartFile image = gameCreate.getImage();
            Date createdAt = new Date();
            String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                StandardCopyOption.REPLACE_EXISTING);
            }
            game.setImage(storageFileName);
            }

            game.setName(gameCreate.getName());
            game.setPrice(gameCreate.getPrice());
            game.setStock(gameCreate.getStock());
            game.setGenre(gameCreate.getGenre());
            game.setDeveloper(gameCreate.getDeveloper());
            game.setPlatform(gameCreate.getPlatform());
            game.setPublisher(gameCreate.getPublisher());
            game.setRating(gameCreate.getRating());
            game.setTrailer(gameCreate.getTrailer());
            game.setRelease_date(gameCreate.getRelease_date());
            game.setDescription(gameCreate.getDescription());

            repo.save(game);
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "redirect:/game";
    }

    @GetMapping("/delete")
    public String deleteGame(
        @RequestParam int id
        ) {
            try {
                Game game = repo.findById(id).get();

                Path imagePath = Paths.get("public/images/" + game.getImage());

                try {
                    Files.delete(imagePath);
                }
                catch (Exception e) {
                    System.out.println("Error deleting image: " + e.getMessage());
                }
                
                repo.delete(game);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            return "redirect:/game";
        }
    @GetMapping("/purchase-history")
    public String purchaseHistory(Model model) {
        List<Purchase> purchases = purchaseRepo.findAll();
        model.addAttribute("purchases", purchases);
        return "game/purchase-history";
    }

}
