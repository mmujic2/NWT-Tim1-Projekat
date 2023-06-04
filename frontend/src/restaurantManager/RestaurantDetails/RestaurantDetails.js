import React, { useState, useEffect } from "react";
import Sidebar from "../../shared/util/Sidebar/Sidebar";
import authService from "../../service/auth.service";
import NotFound from "../../shared/util/NotFound";
import { useLocation } from "react-router-dom";
import MainContainer from "../../shared/util/RightSideContainer/MainContainer";
import OrderHistory from "../../customer/CustomerDetails/OrderHistory";
import RestaurantInformation from "./RestaurantInformation";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import discountService from "../../service/discount.service";
import Loader from "../../shared/util/Loader/Loader";
import MenusOverview from "../Menus/MenusOverview";

function RestaurantDetails() {
  var mounted = false;
  const options = new Map([
    ["Restaurant details", "/restaurant/details"],
    ["Menus", "/restaurant/menus"],
    ["Order history", "/restaurant/order/history"],
    ["Reviews", "/restaurant/reviews"],
    ["Coupons", "/restaurant/coupons"],
  ]);
  const icons = new Map([
    ["Restaurant details", "utensils"],
    ["Menus", "book-open"],
    ["Order history", "receipt"],
    ["Reviews", "star"],
    ["Coupons", "percent"],
  ]);
  const user = authService.getCurrentUser();
  const [collapsed, setCollapsed] = useState(false);
  const location = useLocation();
  const items = ["Nesto", "Nesto", "Nesto"];
  const [loading, setLoading] = useState(true);
  const [coupons, setCoupons] = useState();
  useEffect(() => {
    if (!mounted) {
      mounted = true;
      discountService.getAllCoupons().then((res) => {
        if (res.status == 200) {
          setCoupons(res.data);
          setLoading(false);
          console.log(res.data);
        }
      });
    }
  }, []);

  return (
    <div>
      {user.role != "RESTAURANT_MANAGER" ? (
        <NotFound />
      ) : (
        <Sidebar
          optionsMap={options}
          iconsMap={icons}
          collapsed={collapsed}
          setCollapsed={setCollapsed}
        />
      )}
      <MainContainer collapsed={collapsed}>
        <Loader isOpen={loading}>
          {location.pathname == "/restaurant/details" ? (
            <RestaurantInformation />
          ) : location.pathname == "/restaurant/order/history" ? (
            <OrderHistory />
          ) : location.pathname == "/restaurant/coupons" && coupons ? (
            <ListContainer
              title={"Active coupons"}
              type="coupon"
              grid={false}
              items={coupons}
              perPage={5}
            />
          ) : location.pathname == "/restaurant/menus" ? (
            <MenusOverview />
          ) : (
            <></>
          )}
        </Loader>
      </MainContainer>
    </div>
  );
}

export default RestaurantDetails;
