package the.convenient.foodie.discount.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.discount.dao.RequiredScoreRepository;
import the.convenient.foodie.discount.dto.RequiredScoreDto;
import the.convenient.foodie.discount.entity.RequiredScore;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequiredScoreService {
    @Autowired
    RequiredScoreRepository requiredScoreRepository;

    public List<RequiredScore> getAllRequiredScores() {
        return new ArrayList<>(requiredScoreRepository.findAll());
    }

    public RequiredScore getRequiredScore(Integer id) {
        var exception = new EntityNotFoundException("Required score with id " + id + " does not exist!");
        var requiredScore = requiredScoreRepository.findById(id);
        return requiredScore.orElseThrow(() -> exception);
    }

    public RequiredScore addNewRequiredScore(RequiredScoreDto requiredScoreDto) {
        RequiredScore requiredScore = new RequiredScore(requiredScoreDto);
        requiredScoreRepository.save(requiredScore);
        return requiredScore;
    }

    public String deleteRequiredScore(Integer id) {
        var requiredScore = requiredScoreRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Required score with id " + id + " does not exist!"));
        requiredScoreRepository.deleteById(id);
        return "Required score with id " + id + " is successfully deleted!";
    }

    public RequiredScore updateRequiredScore(RequiredScoreDto requiredScoreDto, Integer id) {
        var exception = new EntityNotFoundException("Required score with id " + id + " does not exist!");
        var requiredScore = requiredScoreRepository.findById(id).orElseThrow(() -> exception);
        requiredScore.setMoney_required(requiredScoreDto.getMoney_required());
        requiredScore.setOrders_required(requiredScoreDto.getOrders_required());
        requiredScoreRepository.save(requiredScore);
        return requiredScore;
    }
}
