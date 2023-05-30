import React, { useState, useEffect } from 'react'
import NotFound from '../../shared/util/NotFound';
import { Container } from 'react-bootstrap'
import { useLocation } from 'react-router-dom';
import restaurantService from '../../service/restaurant.service';
import Loader from '../../shared/util/Loader/Loader';
import CustomAlert from '../../shared/util/Alert';
import Gallery from './Gallery';
import defaultImage from "../../images/default.png"
import Image from 'react-bootstrap/Image'
import StarRatings from 'react-star-ratings';
import { HeartFill } from 'react-bootstrap-icons';

function RestaurantOverview() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const id = searchParams.get("id");
    const [notFound,setNotFound] = useState(false);
    const [loading, setLoading] = useState(true);
    const [restaurant, setRestaurant] = useState();
    const [alert, setAlert] = useState({});
    const [showAlert, setShowAlert] = useState(false);
    const [categories, setCategories] = useState("")

    var mounted = false;
    useEffect(() => {
        if (!mounted) {
            mounted = true

            if (id != null) {
                
                restaurantService.getRestaurant(id).then((res) => {
                    setLoading(false)
                    if (res.status == 200) {
                        setRestaurant(res.data)
                        setCategories(res.data.categories.join(", "))
                    } else {
                        if(res.status==404) {
                            setNotFound(true)
                        }
                        setAlert({ ...alert, msg: res.data.errors, type: "error" })
                        setShowAlert(true)
                        console.log(res)
                    }
                })
            } else {
                setLoading(false)
            }
        }
    }, [])

    return (
        <>
            {notFound? <NotFound header={false}/> : <></>}
            {id ?
                <Loader isOpen={loading}>
                    <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
                    {restaurant ?
                        <div>
                            <Container style={{ width: "95%", padding: 0, position: "relative", backgroundColor: "#F5F5F4", minHeight: "500px" }}>
                                <Gallery id={restaurant.id} />
                                <div style={{ zIndex: 1 }}>
                                    <Image fluid thumbnail src={restaurant.logo ? `data:image/jpeg;base64,${restaurant.logo} ` : defaultImage} style={{ objectFit: "cover", height: "250px", width: "250px", zIndex: 1, position: "absolute", top: 220, left: 70 }} className='shadow-lg' />
                                    <div style={{ marginLeft: "340px", marginTop: "15px" }}>
                                        <h2 >{restaurant.name}</h2>
                                        <h6 >{restaurant.address}</h6>
                                        <h6 style={{ color: "#fe724c", fontWeight: "bold" }}>{categories}</h6>
                                        <div style={{ }}>
                                            <StarRatings
                                                rating={restaurant.rating ? restaurant.rating : 0}
                                                starRatedColor="#FE724C"
                                                numberOfStars={5}
                                                name='rating'
                                                starDimension="20px"
                                                starSpacing="2px"
                                                starEmptyColor='grey'

                                            />
                                            <span style={{ color: "#FE724C", verticalAlign: "middle" }}> {restaurant.rating ? restaurant.rating : 0} </span>
                                            <span style={{ color: "grey", verticalAlign: "middle" }}>{"(" + restaurant.customersRated + ")"}</span>

                                        </div>
                                        <div style={{ }}>
                                            <HeartFill style={{ color: "#fe724c", verticalAlign: "middle" }}></HeartFill>
                                            <span style={{ color: "grey", verticalAlign: "middle" }}> {restaurant.customersFavorited} customers love this</span>
                                        </div>
                                    </div>
                                </div>
                            </Container>

                        </div> : <></>}
                </Loader>
                : <NotFound header={false}></NotFound>}
        </>
    )
}

export default RestaurantOverview