import React from "react";
import Loader from "../../shared/util/Loader/Loader";
import CustomAlert from "../../shared/util/Alert";
import { Container } from "react-bootstrap";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import { Spinner } from "react-bootstrap";
import { useState, useEffect } from "react";
import orderService from "../../service/order.service";
import userService from "../../service/user.service";
import restaurantService from "../../service/restaurant.service";

function RestaurantOrders() {
  const [pendingOrders, setPendingOrders] = useState();
  const [inPreparationOrders, setInPreparationOrders] = useState();
  const [readyOrders, setReadyOrders] = useState();
  const [loading, setLoading] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const [alert, setAlert] = useState({});

  useEffect(() => {
    setLoading(true);
    orderService
      .getRestaurantPendingOrders(restaurantService.getCurrentRestaurantUUID())
      .then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setPendingOrders(res.data);
        } else {
          setAlert({ msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });

    orderService
      .getRestaurantReadyOrders(restaurantService.getCurrentRestaurantUUID())
      .then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setReadyOrders(res.data);
        } else {
          setAlert({ msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });
    orderService
      .getRestaurantInPreparationOrders(
        restaurantService.getCurrentRestaurantUUID()
      )
      .then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setInPreparationOrders(res.data);
        } else {
          setAlert({ msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });
  }, []);

  const acceptOrder = (oldOrder, newOrder) => {
    setPendingOrders(pendingOrders.filter((o) => o.id != oldOrder.id));
    setInPreparationOrders([...inPreparationOrders, newOrder]);
  };

  const rejectOrder = (order) => {
    setPendingOrders(pendingOrders.filter((o) => o.id != order.id));
  };

  const movePendingOrder = (oldOrder, newOrder, action) => {
    if (action == "Accept") {
      acceptOrder(oldOrder, newOrder);
    } else {
      rejectOrder(oldOrder);
    }
  };

  const readyOrder = (oldOrder, newOrder) => {
    setInPreparationOrders(
      inPreparationOrders.filter((o) => o.id != oldOrder.id)
    );
    setReadyOrders([...readyOrders, newOrder]);
  };

  return (
    <>
      <Loader isOpen={loading}>
        <CustomAlert
          setShow={setShowAlert}
          show={showAlert}
          type={alert.type}
          msg={alert.msg}
        ></CustomAlert>
        <>
          <Container
            style={{
              backgroundColor: "#D9D9D9",
              width: "95%",
              margin: "auto",
              marginTop: "20px",
              marginBottom: "20px",
              maxWidth: "95%",
            }}
          >
            {pendingOrders ? (
              <ListContainer
                items={pendingOrders}
                title={"Pending orders"}
                showFilters={false}
                perPage={4}
                grid={false}
                setItems={setPendingOrders}
                type="order"
                moveOrder={movePendingOrder}
                setAlert={setAlert}
                setShowAlert={setShowAlert}
                alert={alert}
              ></ListContainer>
            ) : (
              <div style={{ display: "flex", justifyContent: "center" }}>
                <Spinner
                  animation="border"
                  style={{ color: "white", marginTop: "20%" }}
                />
              </div>
            )}

            {inPreparationOrders ? (
              <ListContainer
                items={inPreparationOrders}
                title={"In preparation"}
                showFilters={false}
                perPage={4}
                grid={false}
                setItems={setInPreparationOrders}
                type="order"
                moveOrder={readyOrder}
                setAlert={setAlert}
                setShowAlert={setShowAlert}
                alert={alert}
              ></ListContainer>
            ) : (
              <div style={{ display: "flex", justifyContent: "center" }}>
                <Spinner
                  animation="border"
                  style={{ color: "white", marginTop: "20%" }}
                />
              </div>
            )}

            {readyOrders ? (
              <ListContainer
                items={readyOrders}
                title={"Ready for delivery"}
                showFilters={false}
                perPage={4}
                grid={false}
                setItems={setReadyOrders}
                type="order"
              ></ListContainer>
            ) : (
              <div style={{ display: "flex", justifyContent: "center" }}>
                <Spinner
                  animation="border"
                  style={{ color: "white", marginTop: "20%" }}
                />
              </div>
            )}
          </Container>
        </>
      </Loader>
    </>
  );
}

export default RestaurantOrders;
