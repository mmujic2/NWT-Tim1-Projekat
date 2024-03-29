package the.convenient.foodie.menu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public class IntegerListDto {
    private List<Long> integerList;

    @JsonCreator
    public IntegerListDto(List<Long> integerList) {
        this.integerList = integerList;
    }

    public List<Long> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Long> integerList) {
        this.integerList = integerList;
    }
}
