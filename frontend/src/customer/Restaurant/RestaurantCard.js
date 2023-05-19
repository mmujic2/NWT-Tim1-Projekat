import React from 'react'
import Card from 'react-bootstrap/Card';
import { Col, Row } from 'react-bootstrap';
import defaultImage from "../../images/default.png"
import StarRatings from 'react-star-ratings';
import { HeartFill } from 'react-bootstrap-icons';

function RestaurantCard({ res }) {
    return (
        <>
            {res ?
                <Card style={{ width: '33rem', height: "10rem", overflow: 'hidden',backgroundColor:"#D9D9D9" }} className="hover-shadow">

                    <Row >
                        <Col className="col-5 px-2">
                            <Card.Img variant="top" src={res.logo? `data:image/jpeg;base64,${res.logo} ` : defaultImage} style={{ objectFit: "cover", height: "10rem", width: "100%" }} />
                        </Col>
                        <Col className="col-7 p-0">
                            <Card.Body className="p-2" >
                                <Card.Title style={{ fontSize: "16px", fontWeight: "bold" }}>{res.name}</Card.Title>
                                <Card.Text style={{ fontSize: "14px" }}>
                                    {res.address}
                                    <br />
                                    <strong>Categories: </strong>
                                    {res.categories[0] + " "}
                                    {res.categories.length > 1 ? "and " : ""}
                                    {res.categories.length > 1 ? <u>{(res.categories.length - 1).toString() + " more"}</u> : ""}
                                    <br/>
                                    {res.open?
                                    <strong className='text-success'>OPEN</strong>
                                    : <strong className='text-danger'>CLOSED</strong>}
                                    <br/>
                                    <div style={{position:"absolute",bottom:2}}>
                                    <StarRatings
                                        rating={res.rating}
                                        starRatedColor="#FE724C"
                                        numberOfStars={5}
                                        name='rating'
                                        starDimension="20px"
                                        starSpacing="2px"
                                    />
                                    <span style={{color:"#FE724C",verticalAlign:"middle"}}> {res.rating} </span>
                                    <span style={{color:"grey",verticalAlign:"middle"}}>{"("+res.customersRated+")"}</span>
                                    
                                    </div>
                                    <div style={{position:"absolute",bottom:2,right:5}}>
                                    <HeartFill style={{color:"#fe724c",verticalAlign:"middle"}}></HeartFill>
                                    <span style={{color:"grey",verticalAlign:"middle"}}> {res.customersFavorited}</span>
                                    </div>

                                </Card.Text>
                            </Card.Body>
                        </Col>
                    </Row>
                </Card> : <></>}

        </>
    )
}

export default RestaurantCard