import orderService from '../../service/order.service'
import React from 'react'
import { ButtonGroup, Button } from 'react-bootstrap'
import authService from '../../service/auth.service'
import { Check2Circle } from 'react-bootstrap-icons'
import { XCircleFill } from 'react-bootstrap-icons'

function Actions({ status,setNoActions }) {
    const user = authService.getCurrentUser();

    const restaurantManagerActions = () => {
        switch (status) {
            case "Pending":
                setNoActions(false)
                return(
                <ButtonGroup >
                    <Button style={{ borderTopLeftRadius:0,borderTopRightRadius:0,borderBottomLeftRadius: 5,borderBottomRightRadius: 0,width:"100px" }} variant="secondary" onClick={()=>orderService.acceptOrderForRestaurant()}>Accept <Check2Circle/></Button>
                    <Button style={{ borderTopLeftRadius:0,borderTopRightRadius:0,borderBottomLeftRadius: 0,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"100px" }} variant="secondary" onClick={()=>orderService.rejectOrder()}>Reject <XCircleFill/></Button>
                </ButtonGroup>)
                break
            case "In preparation":
                setNoActions(false)
                return(
                <ButtonGroup>
                    <Button style={{ borderTopLeftRadius:0,borderTopRightRadius:0,borderBottomLeftRadius: 5,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"100px" }} variant="secondary" onClick={()=>orderService.orderReady()}>Ready <Check2Circle/></Button>
                </ButtonGroup>)
            break
            default:
                setNoActions(true)
        }
    }

    const courierActions = () => {
       
        switch (status) {
            case "Ready for delivery":
                setNoActions(false)
                return (
                <ButtonGroup>
                    <Button style={{borderTopLeftRadius:0,borderTopRightRadius:0, borderBottomLeftRadius: 5,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"100px"  }} variant="secondary" onClick={()=>orderService.acceptOrderForCourier()}>Accept <Check2Circle/></Button>
                </ButtonGroup>)
                break
            case "In delivery":
                setNoActions(false)
                return(
                <ButtonGroup>
                    <Button style={{ borderTopLeftRadius:0,borderTopRightRadius:0,borderBottomLeftRadius: 5,borderBottomRightRadius: 5,backgroundColor:"#FE724C",border:"#FE724C",width:"120px" }} variant="secondary" onClick={()=>orderService.orderDelivered()}>Delivered <Check2Circle/></Button>
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
            : setNoActions(true)}
           
        </div>
    )
}

export default Actions