package the.convenient.foodie.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.convenient.foodie.restaurant.dto.restaurantimage.RestaurantImageResponse;
import the.convenient.foodie.restaurant.dto.restaurantimage.RestaurantImageUploadRequest;
import the.convenient.foodie.restaurant.model.RestaurantImage;
import the.convenient.foodie.restaurant.repository.RestaurantImageRepository;
import the.convenient.foodie.restaurant.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantImageService {

    @Autowired
    RestaurantImageRepository restaurantImageRepository;
    @Autowired
    RestaurantRepository restaurantRepository;


    public void uploadRestaurantImages(List<RestaurantImageUploadRequest> imageData,String userUUID, Long restaurantId) {
        List<RestaurantImage> restaurantImages = new ArrayList<>();
        imageData.forEach(data-> {
            var restaurantImage = new RestaurantImage();
            restaurantImage.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow());
            restaurantImage.setCreated(LocalDateTime.now());
            restaurantImage.setCreatedBy(userUUID);
            restaurantImage.setImage(data.getImageData());
            restaurantImages.add(restaurantImage);
        });

        restaurantImageRepository.saveAll(restaurantImages);
    }

    public List<RestaurantImageResponse> getRestaurantImages(Long restaurantId) {
        return restaurantImageRepository.findAllByRestaurantId(restaurantId).stream()
                .map(ri -> new RestaurantImageResponse(ri.getImage()))
                .collect(Collectors.toList());

    }
}
