import "./App.css";

import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { Route, Routes } from "react-router-dom";
import RegisterPage from "./shared/RegisterPage/RegisterPage";
import PrivateRoute from "./shared/PrivateRoute/PrivateRoute";
import Sidebar from "./shared/util/Sidebar/Sidebar";
import Header from "./shared/util/Header";
import NotFound from "./shared/util/NotFound";
import Home from "./shared/Home/Home";
import AdminRestaurants from "./admin/Home/AdminRestaurants";
import CustomerDetails from "./customer/CustomerDetails/CustomerDetails";
import RestaurantOverview from "./customer/Restaurant/RestaurantOverview";
import TokenService from "./service/token.service";
import SockJsClient from "react-stomp";
import { useState } from "react";
import Alert from "./shared/util/Alert";
import RestaurantDetails from "./restaurantManager/RestaurantDetails/RestaurantDetails";
import AddMenu from "./restaurantManager/Menus/AddMenu";
import AdminCouriers from "./admin/Home/AdminCouriers";

function App() {
  const [show, setShow] = useState(false);
  const [socketMsg, setSocketMsg] = useState([]);

  const manageSocketMessage = (msg) => {
    console.log(msg)
    var message = JSON.parse(msg)
    if(message.status) {
      setSocketMsg(message.message)
      setShow(true);
    }
  }

  return (
    <>
      {TokenService.getUserUUID() != undefined ? (
        <>
          <Alert
           msg={socketMsg}
           show={show}
           setShow={setShow}
          ></Alert>
          <SockJsClient
            url="http://localhost:7050/websocket"
            topics={["/message/" + TokenService.getUserUUID()]}
            onMessage={manageSocketMessage}
          ></SockJsClient>
        </>
      ) : (
        <></>
      )}

      <Routes>
        <Route path="/register" element={<RegisterPage></RegisterPage>} />
        <Route path="*" element={<NotFound></NotFound>} />
        <Route path="/" element={<PrivateRoute />}>
          <Route path="/" element={<Home></Home>}></Route>
          <Route
            path="/customer/details"
            element={<CustomerDetails></CustomerDetails>}
          ></Route>
          <Route
            path="/customer/restaurant/favorites"
            element={<CustomerDetails></CustomerDetails>}
          ></Route>
          <Route
            path="/customer/order/history"
            element={<CustomerDetails></CustomerDetails>}
          ></Route>
          <Route
            path="/customer/restaurant"
            element={<RestaurantOverview></RestaurantOverview>}
          ></Route>
          <Route
            path="/restaurant/details"
            element={<RestaurantDetails></RestaurantDetails>}
          ></Route>
          <Route
            path="/restaurant/menus"
            element={<RestaurantDetails></RestaurantDetails>}
          ></Route>
          <Route
            path="/restaurant/order/history"
            element={<RestaurantDetails></RestaurantDetails>}
          ></Route>
          <Route
            path="/restaurant/reviews"
            element={<RestaurantDetails></RestaurantDetails>}
          ></Route>
          <Route
            path="/restaurant/coupons"
            element={<RestaurantDetails></RestaurantDetails>}
          ></Route>
          <Route
            path="/restaurant/gallery"
            element={<RestaurantDetails></RestaurantDetails>}
          ></Route>
          <Route
            path="/menu/add"
            element={<RestaurantDetails></RestaurantDetails>}
          ></Route>
          <Route
            path="/admin/couriers"
            element={<AdminCouriers></AdminCouriers>}
          ></Route>
        </Route>
      </Routes>
    </>
  );
}

export default App;
