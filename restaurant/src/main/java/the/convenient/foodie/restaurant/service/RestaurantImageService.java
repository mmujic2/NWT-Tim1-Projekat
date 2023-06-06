package the.convenient.foodie.restaurant.service;

import jakarta.persistence.EntityNotFoundException;
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


    public Long uploadRestaurantImage(RestaurantImageUploadRequest imageData,String userUUID, Long restaurantId) {

        var restaurantImage = new RestaurantImage();
        restaurantImage.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow());
        restaurantImage.setCreated(LocalDateTime.now());
        restaurantImage.setCreatedBy(userUUID);
        restaurantImage.setImage(imageData.getImageData());


        var image = restaurantImageRepository.save(restaurantImage);
        return image.getId();
    }

    public List<RestaurantImageResponse> getRestaurantImages(Long restaurantId) {
        return restaurantImageRepository.findAllByRestaurantId(restaurantId).stream()
                .map(ri -> new RestaurantImageResponse(ri.getImage(),ri.getId()))
                .collect(Collectors.toList());

    }

    public String deleteRestaurantImage(Long id) {
        var image = restaurantImageRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Image with id " + id + " does not exist!"));
        restaurantImageRepository.delete(image);
        return "Image with id " + id + " successfully deleted!";
    }
}
