import api from "./api"
import tokenService from "./token.service";

class OrderService {

    getUserOrders() {
        try {
          return api
              .get("/order/getforuser")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

      changeOrderStatus(orderId, status) {
        api
          .put("/order/status/" + orderId + "/" + status)
          .then(response => {
        })
      }

      acceptOrderForRestaurant(orderId) {
        //promjena statusa u In preparation
        //znaci da je restoran prihvatio narudzbu
        this.changeOrderStatus(orderId, "In preparation")
      }

      acceptOrderForCourier(orderId) {
        //promjena status u In delivery
        //znaci da je dostavljac prihvatio narudzbu za dostavu
        //posto ovo poziva courier, njegov uuid ce biti u headeru, jer je on current user
        api
          .put("/order/adddeliveryperson/" + orderId)
          .then(response => {

        })
      }

      rejectOrder(orderId) {
        //promjena statusa u Rejected
        //znaci da je restoran odbio naredbu
        this.changeOrderStatus(orderId, "Rejected")
      }

      orderReady(orderId) {
        //promjena statusa u Ready for delivery
        //znaci da je restoran pripremio narudzbu i sad je neki od dostavljaca moze preuzeti
        this.changeOrderStatus(orderId, "Ready for delivery")
      }

      orderDelivered(orderId) {
        //promjena statusa u Delivered
        //znaci da je narudzba dostavljena naruciocu
        this.changeOrderStatus(orderId, "Delivered")
      }

      getReadyForDeliveryOrders() {
        return api.get("/order/get/readyfordelivery")
      }

      getDeliveryPersonOrders() {
        return api.get("/order/get/deliveryperson")
      }

      getRestaurantPastOrders() {
        //treba vratiti narudzbe za restoran ciji restaurantManagerUUID je onaj uuid u headeru
        //narudzbe moraju biti Delivered
        return [];
      }

}

export default new OrderService();