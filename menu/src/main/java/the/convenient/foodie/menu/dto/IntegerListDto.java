package the.convenient.foodie.menu.dto;

import java.util.List;

public class IntegerListDto {
    private List<Long> integerList;

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
