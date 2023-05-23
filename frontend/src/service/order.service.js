import api from "./api"

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

      acceptOrderForRestaurant(orderId) {
        //promjena statusa u In preparation
        //znaci da je restoran prihvatio narudzbu
      }

      acceptOrderForCourier(orderId) {
        //promjena status u In delivery
        //znaci da je dostavljac prihvatio narudzbu za dostavu
        //posto ovo poziva courier, njegov uuid ce biti u headeru, jer je on current user
      }

      rejectOrder(orderId) {
        //promjena statusa u Rejected
        //znaci da je restoran odbio naredbu
      }

      orderReady(orderId) {
        //promjena statusa u Ready for delivery
        //znaci da je restoran pripremio narudzbu i sad je neki od dostavljaca moze preuzeti
      }

      orderDelivered(orderId) {
        //promjena statusa u Delivered
        //znaci da je narudzba dostavljena naruciocu
      }

}

export default new OrderService();