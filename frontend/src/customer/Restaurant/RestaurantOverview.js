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

function RestaurantOverview() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const id = searchParams.get("id");
    const [loading, setLoading] = useState(false);
    const [restaurant, setRestaurant] = useState();
    const [alert, setAlert] = useState({});
    const [showAlert, setShowAlert] = useState(false);
    const [categories,setCategories] = useState("")

    var mounted = false;
    useEffect(() => {
        if (!mounted) {
            mounted = true

            if (id != null) {
                setLoading(true)
                restaurantService.getRestaurant(id).then((res) => {
                    setLoading(false)
                    if (res.status == 200) {
                        setRestaurant(res.data)
                        setCategories(res.data.categories.map(c=>c.name).join(", "))
                    } else {
                        setAlert({ ...alert, msg: res.data, type: "error" })
                        setShowAlert(true)
                        console.log(res)
                    }
                })
            }
        }
    }, [])

    return (
        <>
            {id ?
                <Loader loading={loading}>
                    <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
                    {restaurant ?
                        <div>
                            <Container style={{ width: "95%",padding:0,position:"relative",backgroundColor:"#F5F5F4",minHeight:"500px" }}>
                               <Gallery/>
                               <div style={{zIndex:1}}>
                               <Image fluid thumbnail src={restaurant.logo ? `data:image/jpeg;base64,${restaurant.logo} ` : defaultImage} style={{ objectFit: "cover", height: "200px", width: "250px",zIndex:1,position:"absolute",top:220,left:70 }} className='shadow-lg'/>
                               <div style={{marginLeft:"340px",marginTop:"15px"}}>
                               <h2 >{restaurant.name}</h2> 
                               <h6 >{restaurant.address}</h6> 
                               <h6 style={{color:"#fe724c",fontWeight:"bold"}}>{categories}</h6>
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