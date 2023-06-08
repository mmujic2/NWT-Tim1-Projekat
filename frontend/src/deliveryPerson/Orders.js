import React, { useEffect, useState } from 'react'
import { Spinner, Container, Col, Row } from 'react-bootstrap';
import Loader from '../shared/util/Loader/Loader';
import Map2 from '../shared/MapModal/Map2';
import ListContainer from '../shared/util/ListContainer/ListContainer';
import restaurantService from '../service/restaurant.service';
import CustomAlert from '../shared/util/Alert';
import orderService from '../service/order.service';

function Orders() {
    var mounted = false;
    const [favorites, setFavorites] = useState();
    const [readyfordelivery, setReadyForDelivery] = useState()
    const [inDelivery, setInDelivery] = useState()
    const [searchResults, setSearchResults] = useState();
    const [categories, setCategories] = useState();
    const [loading, setLoading] = useState(true);
    const [alert, setAlert] = useState({});
    const [showAlert, setShowAlert] = useState(false);


    useEffect(() => {
        if(!mounted) {
            mounted = true;
            orderService.getReadyForDeliveryOrders().then(res => {
                orderService.getDeliveryPersonOrders().then(res2 => {
                    if(res2.status == 200) {
                        setInDelivery(res2.data)
                    }
                    else {
                        setAlert({ ...alert, msg: [res2.data], type: "error" })
                        setShowAlert(true)
                    }
                })
                if(res.status == 200) {
                    setReadyForDelivery(res.data)
                }
                else {
                    setAlert({ ...alert, msg: [res.data], type: "error" })
                    setShowAlert(true)
                }
            })
        }
    }, [])

    useEffect(() => {
        if(readyfordelivery != undefined && inDelivery != undefined) {
            setLoading(false);
        }
    }, [readyfordelivery, inDelivery])

    const acceptForDelivery = (oldOrder,newOrder) => {
        setReadyForDelivery(readyfordelivery.filter(o=>o.id!=oldOrder.id))
        setInDelivery([newOrder,...inDelivery])
    }

    const deliver = (oldOrder) => {
        setInDelivery(inDelivery.filter(o => o.id!=oldOrder.id))
    }

    return (
        <>
            <Loader isOpen={loading} >
                <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
                {
                    (readyfordelivery != undefined && inDelivery != undefined)
                    ?
                        <Row>
                            <Col>
                                <ListContainer title={"Ready for delivery"} type="order" grid={false} items={readyfordelivery} perPage={5} moveOrder={acceptForDelivery} setItems={setReadyForDelivery}/>
                            </Col>
                            <Col>
                                <ListContainer title={"In delivery"} type="order" grid={false} items={inDelivery} perPage={5} moveOrder={deliver} setItems={setInDelivery}/>
                            </Col>
                        </Row>
                    :
                        <></>
                }
            </Loader>
        </>
    )
}

export default Orders