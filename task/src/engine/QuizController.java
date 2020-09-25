package engine;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.ipc.http.HttpSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompletionRepository completionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    ObjectMapper objectMapper;


    public QuizController() {
        objectMapper = new ObjectMapper();
    }



    @GetMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable int id) throws NoSuchElementException {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        ));
        return ResponseEntity.ok().body(quiz);
    }


    @GetMapping(path = "/api/quizzes")
    public Page<Quiz> getQuizzes(@RequestParam Optional<Integer> id,
                                 @RequestParam Optional<Integer> page) {
        return quizRepository.findById(id.orElse(0),
                PageRequest.of(page.orElse(0), 10));

    }

    @GetMapping(path = "/api/quizzes/completed")
    public Page<Completion> getQuizzesCompletions(@RequestParam Optional<Integer> page, Principal principal) {
        return completionRepository.findAllUsers(principal.getName(),
                PageRequest.of(page.orElse(0), 10, Sort.by("completedAt").descending()));

    }

    @PostMapping(path = "/api/quizzes", consumes = "application/json")
    public Quiz createQuiz(@RequestBody String quiz, Principal principal) {
        try {
            Quiz newQuiz = objectMapper.readValue(quiz, Quiz.class);
            newQuiz.setUser(principal.getName());
            if ((newQuiz.getTitle() == null) || (newQuiz.getTitle().equals("")) ||
                    (newQuiz.getText() == null) || (newQuiz.getText().equals("")) ||
                    newQuiz.getOptions().length < 2) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");

            } else {
                return quizRepository.save(newQuiz);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }



    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public String solveQuizz(@RequestBody HashMap<String, int[]> answer, @PathVariable int id, Principal principal) {
        if (quizRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        } else {
            answer.computeIfAbsent("answer", k -> new int[0]);
            Arrays.sort(answer.get("answer"));
            if (Arrays.equals(answer.get("answer"), quizRepository.getOne(id).getAnswer())) {
                Completion completion = new Completion();
                completion.setId(id);
                completion.setUser(principal.getName());
                completion.setCompletedAt(LocalDateTime.now());
                completionRepository.save(completion);
                return "{\"success\":true, "
                        + "\"feedback\":\"Congratulations, you're right!\"}";
            } else {
                return "{\"success\":false, "
                        + "\"feedback\":\"Wrong answer! Please, try again.\"}";
            }

        }

    }

    @PostMapping(path = "/api/register")
    public HttpSender.Response registerUser(@RequestBody HashMap<String, String> user) {
        try {
            System.out.println(user);
            User newUser = new User();
            newUser.setEmail(user.get("email"));
            newUser.setPassword(passwordEncoder.encode(user.get("password")));
            newUser.setRoles("USER");
            System.out.println(newUser.getEmail());
            System.out.println(newUser.getPassword());
            if (user.get("password").length() < 5 || !newUser.getEmail().contains("@") || !newUser.getEmail().contains(".") ) {
                throw new Exception();
            } else {
                userRepository.save(newUser);
                return new HttpSender.Response(200, "OK");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"
            );
        }
    }


    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<Object> deleteQuiz(@PathVariable int id, Principal principal) {


        quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        ));

        if (quizRepository.getOne(id).getUser().equals(principal.getName())) {
            quizRepository.delete(quizRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            )));
        } else {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.noContent().build();



    }


}

