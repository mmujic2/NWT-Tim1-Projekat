import React from "react";
import Card from "react-bootstrap/Card";
import { Col, Row, Button, Container } from "react-bootstrap";
import defaultImage from "../../images/default.png";
import StarRatings from "react-star-ratings";
import { Clock } from "react-bootstrap-icons";
import { Add, Edit, Delete } from "@mui/icons-material";
import "./RestaurantCard.css";
import { right, left } from "@popperjs/core";
import authService from "../../service/auth.service";
import { useNavigate } from "react-router-dom";
import priceImage from "../../images/price.png";
import { MDBInput } from 'mdb-react-ui-kit';
import { useState } from "react";

function MenuItem({ menuItem, grid = true, setOrderList, orderList }) {
  const [value, setValue] = useState(0);

  const addToOrder = (e) => {
    var found = false;
    var orderListCopy = JSON.parse(JSON.stringify(orderList));
    for(var i = 0; i < orderListCopy.length; i++) {
      if(orderListCopy[i].menuItem.id == menuItem.id) {
        orderListCopy[i].count += parseInt(value);
        found = true;
        break;
      }
    }
    if(!found) {
      orderListCopy.push({menuItem: menuItem, count: parseInt(value)})
    }
    setOrderList(orderListCopy)
  }

  return (
    <div>
      <Card
        style={{
          width: "100%",
          height: "10rem",
          overflow: "hidden",
          backgroundColor: "#D9D9D9",
        }}
        className="box"
      >
        <Row>
          <Col className={grid ? "col-5 px-2" : "col-3 px-2"}>
            <Card.Img
              variant="top"
              src={menuItem.image ? `${menuItem.image} ` : defaultImage}
              style={{ objectFit: "cover", height: "10rem", width: "100%" }}
            />
          </Col>
          <Col className="col-7 p-0">
            <Card.Body className="p-2">
              <Card.Title
                style={{ fontSize: "16px", fontWeight: "bold", float: left }}
              >
                {menuItem.name}
                
              </Card.Title>

              <Card.Text style={{ clear: left, fontSize: "14px" }}>
                <div style={{ color: "#606060" }}>{menuItem.description}</div>
                {setOrderList != undefined && orderList != undefined 
                ? 
                  <div>
                    <div
                      style={{
                        position: "absolute",
                        top: 15,
                        right: 20,
                        display: "flex",
                      }}
                    >
                      <Row style={{marginBottom: "5px", marginLeft: "0px"}}>
                        <Col style={{padding: "0px"}}>
                          <MDBInput
                            value={value} 
                            onChange={(e) => setValue(e.target.value)} 
                            id='typeNumber' 
                            type='number'
                            style={{width: "50px", height: "25px", fontSize: "16px"}}/>
                        </Col>
                        <Col style={{padding: "0px"}}>
                          <Button
                            style={{width: "50px", height: "25px", fontSize: "16px", padding: "0px"}}
                            onClick={(e) => addToOrder(e)}
                          > 
                          Add
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  </div>
                :
                  <></>
                }
                

                <br />
                <div>
                  <div
                    style={{
                      position: "absolute",
                      bottom: 23,
                      right: 5,
                      display: "flex",
                    }}
                  >
                    <Clock
                      style={{
                        verticalAlign: "middle",
                        fontSize: "18px",
                        paddingTop: "1px",
                      }}
                    ></Clock>
                    <div
                      style={{
                        paddingLeft: "2px",
                        fontWeight: "bold",
                      }}
                    >
                      Cooking time:
                    </div>
                    <div
                      style={{
                        paddingLeft: "2px",
                        color: "#606060",
                      }}
                    >
                      {menuItem.prep_time}
                    </div>
                    <div
                      style={{
                        paddingLeft: "2px",
                        color: "#606060",
                      }}
                    >
                      min
                    </div>
                  </div>
                  <div
                    style={{
                      position: "absolute",
                      bottom: 2,
                      right: 5,
                      display: "flex",
                    }}
                  >
                    <img
                      src={priceImage}
                      style={{
                        width: "22px",
                        height: "auto",
                      }}
                    />
                    <div
                      style={{
                        paddingLeft: "2px",
                        fontWeight: "bold",
                      }}
                    >
                      {menuItem.price.toFixed(2)}
                    </div>
                    <div
                      style={{
                        paddingLeft: "2px",
                        fontWeight: "bold",
                      }}
                    >
                      KM
                    </div>
                  </div>
                </div>
              </Card.Text>
            </Card.Body>
          </Col>
        </Row>
      </Card>
    </div>
  );
}

export default MenuItem;
