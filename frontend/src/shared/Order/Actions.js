import orderService from '../../service/order.service'
import React from 'react'
import { ButtonGroup, Button } from 'react-bootstrap'
import authService from '../../service/auth.service'
import { Check2Circle } from 'react-bootstrap-icons'
import { XCircleFill } from 'react-bootstrap-icons'

function Actions({ order,setNoActions,moveOrder,setOrder }) {
    const user = authService.getCurrentUser();

    const acceptOrder = ()=> {
        document.body.style.cursor = "wait"
        orderService.acceptOrderForRestaurant(order.id).then((res)=> {
            document.body.style.cursor = "default"
            if(res.status==200) {
                moveOrder(order,res.data,"Accept")
            } else {
                console.log(res)
            }
        })
    }

    const rejectOrder = ()=> {
        document.body.style.cursor = "wait"
        orderService.rejectOrder(order.id).then((res)=> {
            document.body.style.cursor = "default"

            if(res.status==200) {
                moveOrder(order,res.data,"Reject")
            } else {
                console.log(res)
            }
        })

    }

    const cancelOrder = () => {
        document.body.style.cursor = "wait"
        orderService.cancelOrder(order.id).then((res)=> {
            document.body.style.cursor = "default"

            if(res.status==200) {
                setOrder(res.data)
            } else {
                console.log(res)
            }
        })

    }

    const readyForDelivery = () => {
        document.body.style.cursor = "wait"
        orderService.orderReady(order.id).then((res)=> {
            document.body.style.cursor = "default"

            if(res.status==200) {
                moveOrder(order,res.data)
            } else {
                console.log(res)
            }
        })

    }

    const acceptForDelivery = () => {
        document.body.style.cursor = "wait"
        orderService.acceptOrderForCourier(order.id).then((res)=> {
            document.body.style.cursor = "default"

            if(res.status==200) {
                moveOrder(order,res.data)
            } else {
                console.log(res)
            }
        })

    }

    const deliver = () => {
        document.body.style.cursor = "wait"
        orderService.orderDelivered(order.id).then((res)=> {
            document.body.style.cursor = "default"

            if(res.status==200) {
                moveOrder(res.data)
            } else {
                console.log(res)
            }
        })
    }

    const restaurantManagerActions = () => {
        switch (order.orderStatus) {
            case "Pending":
                setNoActions(false)
                return(
                <ButtonGroup >
                    <Button style={{ borderTopLeftRadius:0,borderTopRightRadius:0,borderBottomLeftRadius: 5,borderBottomRightRadius: 0,width:"100px" }} variant="secondary" onClick={acceptOrder}>Accept <Check2Circle/></Button>
                    <Button style={{ borderTopLeftRadius:0,borderTopRightRadius:0,borderBottomLeftRadius: 0,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"100px" }} variant="secondary" onClick={rejectOrder}>Reject <XCircleFill/></Button>
                </ButtonGroup>)
                break
            case "In preparation":
                setNoActions(false)
                return(
                <ButtonGroup>
                    <Button style={{ borderTopLeftRadius:0,borderTopRightRadius:0,borderBottomLeftRadius: 5,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"100px" }} variant="secondary" onClick={readyForDelivery}>Ready <Check2Circle/></Button>
                </ButtonGroup>)
            break
            default:
                setNoActions(true)
        }
    }

    const courierActions = () => {
       
        switch (order.orderStatus) {
            case "Ready for delivery":
                setNoActions(false)
                return (
                <ButtonGroup>
                    <Button style={{borderTopLeftRadius:0,borderTopRightRadius:0, borderBottomLeftRadius: 5,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"100px"  }} variant="secondary" onClick={acceptForDelivery}>Accept <Check2Circle/></Button>
                </ButtonGroup>)
                break
            case "In delivery":
                setNoActions(false)
                return(
                <ButtonGroup>
                    <Button style={{ borderTopLeftRadius:0,borderTopRightRadius:0,borderBottomLeftRadius: 5,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"120px" }} variant="secondary" onClick={deliver}>Delivered <Check2Circle/></Button>
                </ButtonGroup>)
            break
            default:
                setNoActions(true)
        }
    }

    const customerActions = () => {
       
        switch (order.orderStatus) {
            case "Pending":
                setNoActions(false)
                return (
                <ButtonGroup>
                    <Button style={{borderTopLeftRadius:0,borderTopRightRadius:0, borderBottomLeftRadius: 5,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"100px"  }} variant="secondary" onClick={cancelOrder}>Cancel <XCircleFill/></Button>
                </ButtonGroup>)
                break
            default:
                setNoActions(true)
        }
    }

    return (
        <div>
            {user.role =="RESTAURANT_MANAGER" ? restaurantManagerActions() 
            : user.role=="COURIER" ? courierActions()
            : user.role=="CUSTOMER" ? customerActions()
            : setNoActions(true)}
           
        </div>
    )
}

export default Actions