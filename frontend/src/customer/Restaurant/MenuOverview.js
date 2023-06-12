import React, { useEffect, useState } from "react";
import menuService from "../../service/menu.service";
import tokenService from "../../service/token.service";
import { Tabs, Tab } from "react-bootstrap";
import "./MenuOverview.css";
import { Trash, X, HeartFill } from "react-bootstrap-icons";
import MenuItem from "./MenuItem";
import Loader from "../../shared/util/Loader/Loader";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import { Spinner, Container } from "react-bootstrap";
import { Row, Col, Button } from "react-bootstrap";
import { MDBInput, MDBSpinner } from "mdb-react-ui-kit";
import discountService from "../../service/discount.service";
import userService from "../../service/user.service";
import orderService from "../../service/order.service";
import Alert from "../../shared/util/Alert";
import CustomAlert from "../../shared/util/Alert";
import shoppingCart from "../../images/Shopping cart.svg"

function MenuOverview({ restaurant, setAlert, setShowAlert }) {
  const [menus, setMenus] = useState([]);
  const [loading, setLoading] = useState(true);
  const [orderList, setOrderList] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const [deliveryPrice, setDeliveryPrice] = useState(0);

  const [couponCode, setCouponCode] = useState("");
  const [totalDiscount, setTotalDiscount] = useState(0);
  const [usedCode, setUsedCode] = useState(null);
  const [couponId, setCouponId] = useState(null);
  const [couponuuid, setCouponUuid] = useState(null);
  const [couponValid, setCouponValid] = useState(false);
  const [checkingCoupon, setCheckingCoupon] = useState(false);
  const [checkedCoupon, setCheckedCoupon] = useState(false);
  const [estDeliveryTime, setEstDeliveryTime] = useState(0);
  const [orderCreated, setOrderCreated] = useState(false);

  const [requiredScore, setRequiredScore] = useState(undefined);
  const [currentScore, setCurrentScore] = useState(undefined);
  const [freeDeliveryUsed, setFreeDeliveryUsed] = useState(false);
  const [hasFreeDelivery, setHasFreeDelivery] = useState(false);
  const [freeDeliveryType, setFreeDeliveryType] = useState("");
  const [placingOrder, setPlacingOrder] = useState(false);

  const marginBetweenOrderItems = "10px";

  useEffect(() => {
    //var c1 = restaurant.mapCoordinates.split(", ").map(x => parseFloat(x) / 180 * Math.PI);
    //var c2 = tokenService.getUser().user.mapCoordinates.split(", ").map(x => parseFloat(x) / 180 * Math.PI);
    //var d = Math.acos(Math.sin(c1[0])*Math.sin(c2[0])+Math.cos(c1[0] )*Math.cos(c2[0])*Math.cos(c2[1]-c1[1])) * 6371 * 10;
    setDeliveryPrice(userService.getDistanceToRestaurant(restaurant));
    console.log(restaurant);
    discountService.getRequiredScore().then((response) => {
      if (response.status < 300) {
        setRequiredScore(response.data);
      } else {
        setAlert({ msg: "Error while fetching required score!" });
        setShowAlert(true);
      }
    });

    discountService.getUserScore().then((response) => {
      if (response.status < 300) {
        setCurrentScore(response.data);
      } else {
        setAlert({ msg: "Error while fetching user score!" });
        setShowAlert(true);
      }
    });
  }, []);

  useEffect(() => {
    console.log(requiredScore);
  }, [requiredScore]);

  useEffect(() => {
    console.log(currentScore);
  }, [currentScore]);

  useEffect(() => {
    var price = 0;
    var time = 0;
    orderList.forEach((o) => {
      console.log(o);
      if (parseInt(o.menuItem.prep_time) > time) time = o.menuItem.prep_time;
      if (o.menuItem.discount_price != null) {
        price += o.menuItem.discount_price * o.count;
      } else {
        price += o.menuItem.price * o.count;
      }
    });
    time += Math.round(deliveryPrice * 2.5);

    setEstDeliveryTime(time);
    setTotalPrice(price);
  }, [orderList]);

  const checkCoupon = () => {
    setCheckingCoupon(true);

    discountService
      .getAllCouponsForRestaurant(restaurant.uuid)
      .then((response) => {
        console.log(response);
        var coupon = undefined;
        if (response.status == 200) {
          var coupons = response.data;
          for (var i = 0; i < coupons.length; i++) {
            if (coupons[i].code === couponCode) {
              coupon = coupons[i];
              break;
            }
          }
        }
        if (coupon != undefined) {
          setCouponValid(true);
          setCouponCode("");
          setCheckingCoupon(false);
          setCheckedCoupon(true);
          setCouponId(coupon.id);
          setCouponUuid(coupon.coupon_uuid);
          setTotalDiscount(coupon.discount_percentage);
        } else {
          setCouponValid(false);
          setCouponCode("");
          setCheckingCoupon(false);
          setCheckedCoupon(true);
          setCouponUuid(null);
          setCouponId(null);
          setTotalDiscount(0);
        }
      });
  };

  const checkFreeDelivery = () => {
    setFreeDeliveryUsed(true);
    if (
      requiredScore != undefined &&
      currentScore != undefined &&
      currentScore.money_spent >= requiredScore.money_required
    ) {
      setHasFreeDelivery(true);
      setFreeDeliveryType("money");
    } else if (
      requiredScore != undefined &&
      currentScore != undefined &&
      currentScore.number_of_orders >= requiredScore.orders_required
    ) {
      setHasFreeDelivery(true);
      setFreeDeliveryType("order");
    } else {
      setHasFreeDelivery(false);
    }
  };

  const removeItemFromOrder = (item) => {
    var newOrderList = [];
    orderList.forEach((i) => {
      if (i.menuItem.id != item.menuItem.id) newOrderList.push(i);
    });
    setOrderList(newOrderList);
  };

  const orderListToUI = () => {
    return orderList.map((item) => (
      <>
        <Row style={{ marginBottom: "5px" }}>
          <Col xs={7}>
            {item.menuItem.name} x{item.count}
          </Col>
          <Col xs={3}>
            {(item.menuItem.discount_price != null
              ? item.menuItem.discount_price
              : item.menuItem.price) * item.count}{" "}
            KM
          </Col>
          <Col xs={1}>
            <Button
              onClick={() => removeItemFromOrder(item)}
              style={{
                backgroundColor: "#fe724c",
                borderColor: "#fe724c",
                width: "fit-content",
                height: "25px",
                width: "25px",
                padding: "0px",
              }}
            >
              <Trash></Trash>
            </Button>
          </Col>
        </Row>
      </>
    ));
  };

  const placeOrder = () => {
    setPlacingOrder(true);
    var user = tokenService.getUser();
    var orderRequest = {};

    orderRequest.totalPrice =
      (totalPrice + deliveryPrice * !hasFreeDelivery) *
      (1 - totalDiscount / 100);

    orderRequest.restaurantId = restaurant.uuid;
    orderRequest.estimatedDeliveryTime = estDeliveryTime;
    orderRequest.couponId = couponuuid;

    orderRequest.deliveryFee = deliveryPrice * !hasFreeDelivery;
    orderRequest.menuItemIds = [];
    for (var i = 0; i < orderList.length; i++) {
      for (var j = 0; j < orderList[i].count; j++) {
        orderRequest.menuItemIds.push(orderList[i].menuItem.id);
      }
    }
    orderRequest.restaurantName = restaurant.name;
    orderRequest.customerPhoneNumber = user.user.phoneNumber;
    orderRequest.customerAddress = user.user.address;
    orderRequest.restaurantAddress = restaurant.address;
    console.log(orderRequest);

    orderService.createOrder(orderRequest).then((response) => {
      if (response.status < 300) {
        setOrderCreated(true);
        setShowAlert(true);
        setAlert({ msg: "Order successfully created!", type: "success" });

        if (couponId != null) {
          discountService.applyCoupon(couponId);
        }
        if (hasFreeDelivery) {
          if (freeDeliveryType === "order") {
            discountService.incrementUserOrders(
              (1 - requiredScore.orders_required).toFixed(0)
            );
            discountService.incrementUserMoney(orderRequest.totalPrice);
          } else if (freeDeliveryType === "money") {
            discountService.incrementUserOrders(1);
            discountService.incrementUserMoney(
              (orderRequest.totalPrice - requiredScore.money_required).toFixed(
                0
              )
            );
          }
        } else {
          discountService.incrementUserOrders(1);
          discountService.incrementUserMoney(
            orderRequest.totalPrice.toFixed(0)
          );
        }
      } else {
        console.log(response);
        setShowAlert(true);
        setAlert({ msg: "Error creating order!", type: "error" });
      }

      setPlacingOrder(false);
    });
  };

  useEffect(() => {
    if (restaurant.uuid != null) {
      console.log(restaurant.uuid);
      menuService.getActiveRestaurantMenus(restaurant.uuid).then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setMenus(res.data);
          setLoading(false);
        }
      });
    }
  }, []);

  return (
    <>
      <Loader isOpen={loading}>
        <div style={{ overflowY: "auto" }}>
          <hr className="tab-separator" />
          <Tabs defaultActiveKey={0} id="my-tabs" style={{ padding: 10 }}>
            {menus.map((menus, index) => (
              <Tab
                eventKey={index}
                title={<span style={{ color: "black" }}>{menus.name}</span>}
                key={index}
              >
                <div>
                  {menus.menuItems ? (
                    <>
                      <Container
                        style={{
                          backgroundColor: "#D9D9D9",
                          width: "100%",
                          margin: "auto",
                          marginTop: "20px",
                          marginBottom: "20px",
                          maxWidth: "60%",
                          float: "left",
                        }}
                      >
                        {menus.menuItems ? (
                          <ListContainer
                            items={menus.menuItems}
                            title={""}
                            showFilters={false}
                            perPage={5}
                            type={"menu"}
                            grid={false}
                            orderList={orderList}
                            setOrderList={setOrderList}
                          ></ListContainer>
                        ) : (
                          <div
                            style={{
                              display: "flex",
                              justifyContent: "center",
                            }}
                          >
                            <Spinner
                              animation="border"
                              style={{ color: "white", marginTop: "20%" }}
                            />
                          </div>
                        )}
                      </Container>
                      <Container
                        style={{
                          backgroundColor: "#D9D9D9",
                          width: "100%",
                          margin: "auto",
                          marginTop: "20px",
                          marginBottom: "20px",
                          maxWidth: "38%",
                          float: "right",
                        }}
                      >
                        Order
                        <Container
                          style={{
                            backgroundColor: "#FFFFFF",
                            width: "100%",
                            margin: "auto",
                            marginTop: "5px",
                            marginBottom: "5px",
                          }}
                        >
                          {restaurant.open ? (
                            tokenService.getUser().user.mapCoordinates !=
                            null ? (
                              orderList.length > 0 ? (
                                <>
                                  {orderListToUI()}

                                  <hr></hr>

                                  <Row
                                    style={{
                                      fontSize: "16px",
                                      marginTop: "20px",
                                    }}
                                  >
                                    <Col xs={8}>
                                      <MDBInput
                                        id="form1"
                                        type="text"
                                        placeholder="Got a coupon?"
                                        value={couponCode}
                                        onChange={(e) =>
                                          setCouponCode(e.target.value)
                                        }
                                      />
                                    </Col>
                                    <Col xs={3}>
                                      {!checkingCoupon ? (
                                        <Button
                                          style={{
                                            fontSize: "16px",
                                            width: "100%",
                                          }}
                                          onClick={checkCoupon}
                                        >
                                          {" "}
                                          Check{" "}
                                        </Button>
                                      ) : (
                                        <MDBSpinner></MDBSpinner>
                                      )}
                                    </Col>
                                  </Row>
                                  {checkedCoupon ? (
                                    couponValid ? (
                                      <div
                                        style={{
                                          fontSize: "10px",
                                          color: "green",
                                        }}
                                      >
                                        Coupon valid!{" "}
                                      </div>
                                    ) : (
                                      <div
                                        style={{
                                          fontSize: "10px",
                                          color: "red",
                                        }}
                                      >
                                        Coupon invalid!{" "}
                                      </div>
                                    )
                                  ) : (
                                    <></>
                                  )}
                                  {!freeDeliveryUsed ? (
                                    <Row>
                                      <Col xs={8}>
                                        <div
                                          style={{
                                            fontSize: "16px",
                                            marginTop: "20px",
                                          }}
                                        >
                                          Delivery fee:{" "}
                                          {deliveryPrice.toFixed(2)} KM
                                        </div>
                                      </Col>
                                      <Col xs={3}>
                                        <Button
                                          style={{
                                            fontSize: "16px",
                                            marginTop: marginBetweenOrderItems,
                                          }}
                                          onClick={checkFreeDelivery}
                                        >
                                          {" "}
                                          Use{" "}
                                        </Button>
                                      </Col>
                                    </Row>
                                  ) : hasFreeDelivery ? (
                                    <>
                                      <div
                                        style={{
                                          fontSize: "16px",
                                          marginTop: "10px",
                                        }}
                                      >
                                        Delivery fee:{" "}
                                        <del>{deliveryPrice.toFixed(2)}</del>{" "}
                                        0.00 KM
                                      </div>
                                      <div
                                        style={{
                                          fontSize: "10px",
                                          color: "green",
                                        }}
                                      >
                                        Free delivery used!{" "}
                                      </div>
                                    </>
                                  ) : (
                                    <>
                                      <div
                                        style={{
                                          fontSize: "16px",
                                          marginTop: "10px",
                                        }}
                                      >
                                        Delivery fee: {deliveryPrice.toFixed(2)}{" "}
                                        KM
                                      </div>
                                      <div
                                        style={{
                                          fontSize: "10px",
                                          color: "red",
                                        }}
                                      >
                                        Not enough score for free delivery!{" "}
                                      </div>
                                    </>
                                  )}

                                  {couponValid ? (
                                    <>
                                      <div
                                        style={{
                                          fontSize: "16px",
                                          marginTop: marginBetweenOrderItems,
                                        }}
                                      >
                                        Total discount: {totalDiscount}%
                                      </div>
                                      <div
                                        style={{
                                          fontSize: "16px",
                                          marginTop: marginBetweenOrderItems,
                                        }}
                                      >
                                        Total price:{" "}
                                        <del>
                                          {(
                                            totalPrice +
                                            deliveryPrice * !hasFreeDelivery
                                          ).toFixed(2)}
                                        </del>{" "}
                                        {(
                                          (totalPrice +
                                            deliveryPrice * !hasFreeDelivery) *
                                          (1 - totalDiscount / 100)
                                        ).toFixed(2)}{" "}
                                        KM{" "}
                                      </div>
                                    </>
                                  ) : (
                                    <div
                                      style={{
                                        fontSize: "16px",
                                        marginTop: marginBetweenOrderItems,
                                      }}
                                    >
                                      Total price:{" "}
                                      {(
                                        totalPrice +
                                        deliveryPrice * !hasFreeDelivery
                                      ).toFixed(2)}{" "}
                                      KM
                                    </div>
                                  )}
                                  <div
                                    style={{
                                      fontSize: "16px",
                                      marginTop: marginBetweenOrderItems,
                                    }}
                                  >
                                    Delivery time: {estDeliveryTime.toFixed(0)}{" "}
                                    min
                                  </div>
                                  <hr></hr>
                                  {orderCreated ? (
                                    <></>
                                  ) : placingOrder ? (
                                    <Row>
                                      <Col xs={5}></Col>
                                      <Col xs={1}>
                                        <MDBSpinner></MDBSpinner>
                                      </Col>
                                      <Col xs={5}></Col>
                                    </Row>
                                  ) : (
                                    <Row>
                                      <Col xs={2}></Col>
                                      <Col xs={7}>
                                        <Button onClick={placeOrder}>
                                          Place order
                                        </Button>
                                      </Col>
                                      <Col xs={2}></Col>
                                    </Row>
                                  )}
                                </>
                              ) : (
                                <div style={{textAlign:"center",color:"grey"}}>
                                  <img src={shoppingCart} style={{width:"300px",marginLeft:"40px"}}/>
                                  <br/>
                                  <span>Your cart is empty</span>
                                </div>
                              )
                            ) : (
                              <>
                                Please add address and phone number to account
                                before ordering
                              </>
                            )
                          ) : (
                            <> Restaurant is currently closed </>
                          )}
                        </Container>
                      </Container>
                    </>
                  ) : (
                    <></>
                  )}
                </div>
              </Tab>
            ))}
          </Tabs>
        </div>
      </Loader>
    </>
  );
}

export default MenuOverview;
