import React from 'react'
import Card from 'react-bootstrap/Card';
import { Col, Row, Button, Container } from 'react-bootstrap';
import { right, left } from "@popperjs/core";
import './OrderCard.css'
import { orderStatus } from '../../functions/OrderStatus';


function OrderCard({ order}) {

    const toggleMenuItems =(i)=>{
            var element = document.getElementById("menu-item"+i)
            element.classList.toggle("expanded");
           
            if (element.style.maxHeight){
              element.style.maxHeight = null;
            } else {
              element.style.maxHeight = element.scrollHeight + "px";
            } 
          
    }

    return (
        <>
            {order ?
                <Card style={{ width: '100%', height: "fit-content", backgroundColor: "#272D2F" }} >

                    <Card.Header >
                    <Card.Title style={{ fontSize: "18px",float:"left", fontWeight: "bold", position:"relative",top:5,left:10,color:"#FE724C" }}>{order.orderCode}</Card.Title>
                    <span style={{float:"right"}}>{orderStatus(order.orderStatus)}</span>
                    <span style={{clear:"both",color:"white",float:"right",fontSize:"14px"}}>20/05/2023</span>
                    </Card.Header>
                    
                    <Card.Body className="p-2" >
                        <Row>
                        <Col className='col-8'>
                        <div className='menu-items content' id={"menu-item" + order.id} onClick={()=>toggleMenuItems(order.id)}>
                            {order.menuItems.map(i =>
                                <Card.Text>{i.name} x {i.quantity}</Card.Text>)}

                        </div>
                        </Col>
                        <Col className="col-4">
                        <Card.Text style={{color:"#FE724C",position:"absolute",bottom:5}}>
                            <span style={{fontWeight:"bold"}}>Total price:</span> {order.totalPrice} KM
                        </Card.Text>
                        </Col>
                        </Row>

                    </Card.Body>
                </Card>
                 : <></>}

        </>
    )
}

export default OrderCard