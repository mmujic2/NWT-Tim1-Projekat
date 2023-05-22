import React, { useState } from 'react'
import Card from 'react-bootstrap/Card';
import { Col, Row, Button, Container } from 'react-bootstrap';
import { right, left } from "@popperjs/core";
import './OrderCard.css'
import { orderStatus } from '../../functions/OrderStatus';
import dateFormat from 'dateformat';
import authService from '../../service/auth.service';
import { useNavigate } from 'react-router-dom';
import Actions from './Actions';


function OrderCard({ order }) {
    const [noActions,setNoActions] = useState(false)
    const navigate = useNavigate();
    const user = authService.getCurrentUser();
    const toggleMenuItems = (i) => {
        var element = document.getElementById("menu-item" + i)
        element.classList.toggle("expanded");

        if (element.style.maxHeight) {
            element.style.maxHeight = null;
        } else {
            element.style.maxHeight = element.scrollHeight + "px";
        }

    }

    const additionalInfo = () => {
        switch (user.role) {
            case "CUSTOMER":
                return (
                    <>
                        <Row>
                            <Card.Text style={{ color: "#FE724C" }}>
                                <span style={{ fontWeight: "bold" }}>{order.restaurantName}</span>
                            </Card.Text>
                        </Row>
                    </>)
                break
            case "RESTAURANT_MANAGER":
                return (
                    <>
                        <Row>
                            <Card.Text style={{ color: "white" }}>
                                <span style={{ fontWeight: "bold", color: "#FE724C" }}>Delivery address: </span>{order.customerAddress}
                            </Card.Text>
                        </Row>
                    </>)
                break
            case "COURIER":
                return (
                    <>
                        <Row>
                            <Card.Text style={{ color: "white" }}>
                                <span style={{ fontWeight: "bold", color: "#FE724C" }}>Pick up at: </span>{order.restaurantName}, Bulevar Meše Selimovića 2c, Sarajevo
                            </Card.Text>
                        </Row>
                        <Row>
                            <Card.Text style={{ color: "white" }}>
                                <span style={{ fontWeight: "bold", color: "#FE724C" }}>Drop off at: </span>{order.customerAddress}, {order.customerPhoneNumber}
                            </Card.Text>
                        </Row>

                    </>)
                break
        }
    }

    return (
        <>
            {order ?
                <>
                    <Card style={{ width: '100%', height: "fit-content", backgroundColor: "#272D2F",borderBottomRightRadius: noActions? 5 : 0 }} >

                        <Card.Header style={{ height: "4rem" }}>
                            <Card.Title style={{ fontSize: "18px", float: "left", fontWeight: "bold", position: "relative", top: 5, left: 0, color: "#FE724C" }} id="code" onClick={() => navigate("/order?id=" + order.id)}>{order.orderCode}</Card.Title>
                            <span style={{ float: "right" }}>{orderStatus(order.orderStatus)}</span>
                            <span style={{ clear: "both", color: "white", float: "right", fontSize: "14px" }}>{dateFormat(order.createdTime, "dd/mm/yyyy HH:MM")}</span>
                        </Card.Header>

                        <Card.Body className="p-2" >
                            <Row>
                                <Col className='col-5'>
                                    <div className='menu-items content' id={"menu-item" + order.id} onClick={() => toggleMenuItems(order.id)}>
                                        {Object.keys(order.menuItemCount).map((k) => {
                                            return <Card.Text>{k} x {order.menuItemCount[k]}</Card.Text>
                                        })}

                                    </div>
                                </Col>
                                <Col className="col-7" style={{ marginTop: '5rem' }}>
                                    <div style={{ position: "absolute", bottom: 5, padding: 2 }}>
                                        {additionalInfo()}
                                        <Row>
                                            <Card.Text style={{ color: "white" }}>
                                                <span style={{ fontWeight: "bold", color: "#FE724C" }}>Total price:</span> {order.totalPrice} KM
                                            </Card.Text>
                                        </Row>
                                    </div>
                                </Col>
                            </Row>

                        </Card.Body>
                    </Card>
                    <div style={{float:"right"}}>
                    <Actions status={order.orderStatus} setNoActions={setNoActions}/>
                    </div>
                </>
                : <></>}

        </>
    )
}

export default OrderCard