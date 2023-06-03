import api from "./api";

class DiscountService {
  getAllCoupons() {
    try {
      return api
        .get("/coupon/all")
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }
}

export default new DiscountService();