import React from 'react'
import Card from 'react-bootstrap/Card';
import { Col, Row, Button, Container } from 'react-bootstrap';
import { Add, Edit, Delete } from '@mui/icons-material'
import './CouponCard.css'
import { right, left } from "@popperjs/core";
import authService from '../../service/auth.service'
import { useNavigate } from 'react-router-dom';
import discountService from '../../service/discount.service';




function CouponCard({ res, grid = true }) {
    const user = authService.getCurrentUser();
    const navigate = useNavigate();

    const deleteCoupon=(id)=>{
        discountService.deleteCoupon(id).then(res => {
            if (res.status == 200) {
                console.log(res.data)
                window.location.reload(false);
            }
            else
                console.log(res)
    
        })
        window.location.reload(false);
    }

    return (
        <>
            {res ?
                <Card onClick={() => console.log(res.id)} style={{ width: '100%', height: "10rem", overflow: 'hidden', backgroundColor: "#D9D9D9" }} className="box">

                    <Row >
                        <Col className="col-7 p-0">
                            <Card.Body className="p-2" >
                                <Card.Title style={{ fontSize: "20px", fontWeight: "bold", float: left, paddingBottom:"10%"}}>Coupon code: {res.code}</Card.Title>
                                <Card.Text style={{ clear: left, fontSize: "16px" }}>
                                    Discount percentage: {res.discount_percentage}%
                                    <br />
                                    Amount left: {res.quantity}
                                    <div style={{ position: "absolute", bottom: "5%", right: "5%" }}>
                                        
                                        <Button onClick={(e) => { deleteCoupon(res.id); e.stopPropagation() }} style={{backgroundColor: "#fe724c", borderColor: "#fe724c",color: "white"}} class="rounded"> Delete<Delete fontSize="medium"></Delete></Button>
                                    </div>
                                </Card.Text>
                            </Card.Body>
                        </Col>
                        
                    </Row>
                </Card> : <></>}

        </>
    )
}

export default CouponCard