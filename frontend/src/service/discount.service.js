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

  addCoupon(coupon) {
    try {
      return api
        .post("/coupon/add",coupon)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  deleteCoupon(id) {
    try {
      return api
        .delete("/coupon/"+id)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

}

export default new DiscountService();