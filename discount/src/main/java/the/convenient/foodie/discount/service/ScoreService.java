package the.convenient.foodie.discount.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.discount.dao.ScoreRepository;
import the.convenient.foodie.discount.dao.dto.ScoreDto;
import the.convenient.foodie.discount.entity.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    public List<Score> getAllScores() {
        return new ArrayList<>(scoreRepository.findAll());
    }

    public Score getScore(Integer id) {
        var exception = new EntityNotFoundException("Score with id " + id + " does not exist!");
        var score = scoreRepository.findById(id);
        return score.orElseThrow(() -> exception);
    }

    public Score addNewScore(ScoreDto scoreDto) {
        Score score = new Score(scoreDto);
        scoreRepository.save(score);
        return score;
    }

    public String deleteScore(Integer id) {
        var score = scoreRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Score with id " + id + " does not exist!"));
        scoreRepository.deleteById(id);
        return "Score with id " + id + " is successfully deleted!";
    }

    public Score updateScore(ScoreDto scoreDto, Integer id) {
        var exception = new EntityNotFoundException("Score with id " + id + " does not exist!");
        var score = scoreRepository.findById(id).orElseThrow(() -> exception);
        score.setUser_id(scoreDto.getUser_id());
        score.setMoney_spent(scoreDto.getMoney_spent());
        score.setNumber_of_orders(scoreDto.getNumber_of_orders());
        scoreRepository.save(score);
        return score;
    }




}
