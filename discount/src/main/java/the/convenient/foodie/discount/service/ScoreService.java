package the.convenient.foodie.discount.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.discount.dao.ScoreRepository;
import the.convenient.foodie.discount.dto.ScoreDto;
import the.convenient.foodie.discount.entity.Coupon;
import the.convenient.foodie.discount.entity.Score;

import java.util.ArrayList;
import java.util.List;

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

    public Score getScoreByUserId(String useruuid) {
        var score = scoreRepository.findAll().stream().filter(x -> x.getUser_id().equals(useruuid)).findFirst().orElseThrow();
        return score;
    }

    public Score incrementUserOrders(String useruuid, Integer orders) {
        var score = scoreRepository.findAll().stream().filter(x -> x.getUser_id().equals(useruuid)).findFirst().orElseThrow();
        score.setNumber_of_orders(score.getNumber_of_orders() + orders);
        if(score.getNumber_of_orders() < 0) score.setNumber_of_orders(0);
        return scoreRepository.save(score);
    }

    public Score incrementUserMoneySpent(String useruuid, Integer money) {
        var score = scoreRepository.findAll().stream().filter(x -> x.getUser_id().equals(useruuid)).findFirst().orElseThrow();
        score.setMoney_spent(score.getMoney_spent() + money);
        if(score.getMoney_spent() < 0) score.setMoney_spent(0);
        return scoreRepository.save(score);
    }
}
