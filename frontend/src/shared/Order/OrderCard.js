import React from 'react'
import Card from 'react-bootstrap/Card';
import { Col, Row, Button, Container } from 'react-bootstrap';
import { right, left } from "@popperjs/core";
import { Diversity1Outlined, Diversity1Rounded } from '@mui/icons-material';


function OrderCard({ order}) {
    return (
        <>
            {order ?
                <Card style={{ width: '100%', height: "10rem", overflow: 'hidden', backgroundColor: "#D9D9D9" }} >


                    <Card.Title style={{ fontSize: "16px", fontWeight: "bold", float: left }}>{order.code}</Card.Title>

                    <Card.Body className="p-2" >
                        <div>
                            {order.menuItems.map(i =>
                                <Card.Text>{i.name} x {i.quantity}</Card.Text>)}

                        </div>
                        <Card.Text>
                            {order.totalPrice}
                        </Card.Text>

                    </Card.Body>

                </Card> : <></>}

        </>
    )
}

export default OrderCard